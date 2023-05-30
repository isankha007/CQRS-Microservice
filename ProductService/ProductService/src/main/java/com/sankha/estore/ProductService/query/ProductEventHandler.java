package com.sankha.estore.ProductService.query;

import com.sankha.estore.ProductService.core.data.ProductEntity;
import com.sankha.estore.ProductService.core.data.ProductRepository;
import com.sankha.estore.ProductService.core.event.ProductCreateEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private ProductRepository productRepository;

    @Autowired
    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @ExceptionHandler(resultType=Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }
    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException e){

    }

    @EventHandler
    public void on(ProductCreateEvent event) throws Exception {
        ProductEntity product=new ProductEntity();
        BeanUtils.copyProperties(event,product);

        try{
            productRepository.save(product);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }

       // throw new Exception("Forcing exception error took place in ....");
    }
}
