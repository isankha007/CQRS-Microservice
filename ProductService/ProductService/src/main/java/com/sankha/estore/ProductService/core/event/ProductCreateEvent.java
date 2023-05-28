package com.sankha.estore.ProductService.core.event;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateEvent {
    private  String productId;
    private  String title;
    private  BigDecimal price;
    private  Integer quantity;
}
