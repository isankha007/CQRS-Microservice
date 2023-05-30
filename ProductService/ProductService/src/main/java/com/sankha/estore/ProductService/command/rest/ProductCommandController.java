package com.sankha.estore.ProductService.command.rest;

import com.sankha.estore.ProductService.command.CreateProductCommand;
import com.sankha.estore.ProductService.command.CreateProductRestModel;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductCommandController {

    private final CommandGateway commandGateway;
    private final Environment env;

    @Autowired
    public ProductCommandController(CommandGateway commandGateway, Environment env) {
        this.commandGateway = commandGateway;
        this.env = env;
    }

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductResrModel){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductResrModel.getPrice())
                .quantity(createProductResrModel.getQuantity())
                .title(createProductResrModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue;
        returnValue = commandGateway.sendAndWait(createProductCommand);
      /* try {
           returnValue = commandGateway.sendAndWait(createProductCommand);
       }catch (Exception e){
            returnValue=e.getLocalizedMessage();
       }*/


        return returnValue;  //"HTTP POST Handled"+createProductResrModel.getTitle();
    }

 /*   @GetMapping
    public String getProduct(){
        return "HTTP Get Handled"+env.getProperty("local.server.port");
    }

    @PutMapping
    public String updateProduct(){
        return "HTTP PUT Handled";
    }

    @DeleteMapping
    public String deleteProduct(){
        return "HTTP DELETE Handled";
    }*/

}
