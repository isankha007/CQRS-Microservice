package com.sankkha.estore.OrdersService.command.rest;

import com.sankkha.estore.OrdersService.command.commands.CreateOrderCommand;
import com.sankkha.estore.OrdersService.core.model.OrderStatus;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    public OrdersCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderRestModel order){
       String userId=" 27b95829-4f3f-4ddf-8983-151ba010e35b";
        CreateOrderCommand createOrderCommand=CreateOrderCommand.builder()
                .addressId(order.getAddressId())
                .quantity(order.getQuantity())
                .productId(order.getProductId())
                .orderStatus(OrderStatus.CREATED)
                .userId(userId)
                .orderId(UUID.randomUUID().toString())
                .build();
        return commandGateway.sendAndWait(createOrderCommand);
    }
}
