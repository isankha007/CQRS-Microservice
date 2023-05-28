package com.sankha.estore.ProductService.query;

import com.sankha.estore.ProductService.core.data.ProductEntity;
import com.sankha.estore.ProductService.core.data.ProductRepository;
import com.sankha.estore.ProductService.core.event.ProductCreateEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductEventHandler {

    private ProductRepository productRepository;

    @Autowired
    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreateEvent event){
        ProductEntity product=new ProductEntity();
        BeanUtils.copyProperties(event,product);
        productRepository.save(product);

    }
}
