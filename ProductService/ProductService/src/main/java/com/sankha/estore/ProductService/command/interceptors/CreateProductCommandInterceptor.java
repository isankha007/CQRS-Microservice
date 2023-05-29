package com.sankha.estore.ProductService.command.interceptors;

import com.sankha.estore.ProductService.command.CreateProductCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);


    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {
        return (index,command)->{
            LOGGER.info("Intercepted command: " + command.getPayloadType());
            CreateProductCommand createProductCommand= (CreateProductCommand) command.getPayload();
            if(CreateProductCommand.class.equals((command.getPayload()))){
                if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0){
                    throw new IllegalArgumentException("Price can not be negative or zero");
                }
                if(createProductCommand.getTitle()==null
                        ||createProductCommand.getTitle().isBlank()){
                    throw new IllegalArgumentException("Title can not be empty");
                }
            }
            return command;
        };
    }
}
