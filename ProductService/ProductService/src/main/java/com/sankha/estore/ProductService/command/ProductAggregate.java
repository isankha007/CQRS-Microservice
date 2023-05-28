package com.sankha.estore.ProductService.command;

import com.sankha.estore.ProductService.core.event.ProductCreateEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private  String productId;
    private  String title;
    private  BigDecimal price;
    private  Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        //validate create product command
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Price can not be negative or zero");
        }
        if(createProductCommand.getTitle()==null
        ||createProductCommand.getTitle().isBlank()){
            throw new IllegalArgumentException("Title can not be empty");
        }
        ProductCreateEvent productCreateEvent=new ProductCreateEvent();
        BeanUtils.copyProperties(createProductCommand,productCreateEvent);
        AggregateLifecycle.apply(productCreateEvent);
    }
    @EventSourcingHandler
    public void on(ProductCreateEvent productCreateEvent){
        this.productId=productCreateEvent.getProductId();
        this.price=productCreateEvent.getPrice();
        this.title=productCreateEvent.getTitle();
        this.quantity=productCreateEvent.getQuantity();
    }
}
