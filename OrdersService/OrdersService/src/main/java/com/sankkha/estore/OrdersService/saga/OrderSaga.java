package com.sankkha.estore.OrdersService.saga;


import com.sankha.estore.core.commands.ProcessPaymentCommand;
import com.sankha.estore.core.commands.ReserveProductCommand;
import com.sankha.estore.core.events.PaymentProcessedEvent;
import com.sankha.estore.core.events.ProductReservedEvent;
import com.sankha.estore.core.model.User;
import com.sankha.estore.core.query.FetchUserPaymentDetailsQuery;
import com.sankkha.estore.OrdersService.core.event.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;
    private static final Logger LOGGER= LoggerFactory.getLogger(OrderSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
        ReserveProductCommand reserveProductCommand=ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        LOGGER.info(" OrderCreatedEvent Handled for orderId:  "+reserveProductCommand.getOrderId()
        +" and productId: "+reserveProductCommand.getProductId());
        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage, @Nonnull CommandResultMessage<?> commandResultMessage) {
               if(commandResultMessage.isExceptional()){
                   //start rollback,compensating transaction
                   System.out.println();
               }
            }
        });


    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent){
        //process user payment
        LOGGER.info(" ProductReservedEvent is called for orderId:  "+productReservedEvent.getOrderId()
                +" and productId: "+productReservedEvent.getProductId());
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery=new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User userPaymentDetails =  null;
        try {
            userPaymentDetails= queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();

        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
            //start compensating transaction
            return;
        }
        if(userPaymentDetails==null){
            //start compensating transaction
            return;
        }
        LOGGER.info("Successfully fetched user payment details for user "+userPaymentDetails.getFirstName());


        ProcessPaymentCommand processPaymentCommand=ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand,10, TimeUnit.SECONDS);
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage());
            // Start compensating transaction
           // cancelProductReservation(productReservedEvent,ex.getMessage());
            return;
        }

        if(result == null) {
            LOGGER.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
            // Start compensating transaction
            //cancelProductReservation(productReservedEvent, "Could not proccess user payment with provided payment details");
        }
    }

    @SagaEventHandler(associationProperty="orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {

       // cancelDeadline();

        // Send an ApproveOrderCommand
        //ApproveOrderCommand approveOrderCommand =
               // new ApproveOrderCommand(paymentProcessedEvent.getOrderId());

        //commandGateway.send(approveOrderCommand);
    }

  /*  @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){


    }*/

}
