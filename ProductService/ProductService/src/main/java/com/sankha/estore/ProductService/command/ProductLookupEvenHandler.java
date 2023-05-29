package com.sankha.estore.ProductService.command;

import com.sankha.estore.ProductService.core.data.ProductLookUpRepository;
import com.sankha.estore.ProductService.core.data.ProductLookupEntity;
import com.sankha.estore.ProductService.core.event.ProductCreateEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEvenHandler {
    private ProductLookUpRepository productLookUpRepository;

    public ProductLookupEvenHandler(ProductLookUpRepository productLookUpRepository) {
        this.productLookUpRepository = productLookUpRepository;
    }

    @EventHandler
    public void on(ProductCreateEvent event){
        ProductLookupEntity productLookupEntity=new ProductLookupEntity(
                event.getProductId(),event.getTitle()
        );
        productLookUpRepository.save(productLookupEntity);
    }
}
