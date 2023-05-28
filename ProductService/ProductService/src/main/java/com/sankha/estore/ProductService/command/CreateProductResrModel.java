package com.sankha.estore.ProductService.command;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductResrModel {
 private String title;
 private BigDecimal price;
 private Integer quantity;
}
