package com.sankkha.estore.OrdersService.query;


import com.sankkha.estore.OrdersService.core.data.OrderEntity;
import com.sankkha.estore.OrdersService.core.data.OrderRepository;
import com.sankkha.estore.OrdersService.core.event.OrderCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    public OrderEventsHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent)throws Exception{
        OrderEntity order=new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent,order);
        this.orderRepository.save(order);
    }
}
