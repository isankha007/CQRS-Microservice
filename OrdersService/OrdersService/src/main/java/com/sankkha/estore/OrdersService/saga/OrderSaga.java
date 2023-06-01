package com.sankkha.estore.OrdersService.saga;


import com.sankha.estore.core.commands.ReserveProductCommand;
import com.sankha.estore.core.events.ProductReservedEvent;
import com.sankkha.estore.OrdersService.core.event.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Saga
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;
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
    }

  /*  @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){


    }*/

}
