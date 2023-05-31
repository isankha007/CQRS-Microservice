package com.sankkha.estore.OrdersService.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRestModel {
    @NotBlank(message = "Order productId is a required field")
    private String productId;
    @Min(value = 1,message = "quantity can not be lower than 1")
    @Max(value = 5,message = "quantity can not be more than 5")
    private int quantity;
    private String  addressId;
}
