package com.sankha.estore.ProductService.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductResrModel {
 private String title;
 private BigDecimal price;
 private Integer quantity;
}
