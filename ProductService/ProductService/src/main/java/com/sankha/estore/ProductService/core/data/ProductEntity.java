package com.sankha.estore.ProductService.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="products")
public class ProductEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -227264951080660124L;
    @Id
    @Column(unique = true)
    private  String productId;
    @Column(unique = true)
    private  String title;
    private BigDecimal price;
    private  Integer quantity;

}
