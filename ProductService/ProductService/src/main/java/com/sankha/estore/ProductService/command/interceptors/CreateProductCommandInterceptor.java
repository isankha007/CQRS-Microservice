package com.sankha.estore.ProductService.command.interceptors;

import com.sankha.estore.ProductService.command.CreateProductCommand;
import com.sankha.estore.ProductService.core.data.ProductLookUpRepository;
import com.sankha.estore.ProductService.core.data.ProductLookupEntity;
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

   private ProductLookUpRepository productLookUpRepository;

    public CreateProductCommandInterceptor(ProductLookUpRepository productLookUpRepository) {
        this.productLookUpRepository = productLookUpRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {
        return (index,command)->{
            LOGGER.info("Intercepted command: " + command.getPayloadType());

            if(CreateProductCommand.class.equals(command.getPayloadType())) {

                CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();

                ProductLookupEntity productLookupEntity =  productLookUpRepository.findByProductIdOrTitle(createProductCommand.getProductId(),
                        createProductCommand.getTitle());

                if(productLookupEntity != null) {
                    throw new IllegalStateException(
                            String.format("Product with productId %s or title %s already exist",
                                    createProductCommand.getProductId(), createProductCommand.getTitle())
                    );
                }

            }

            return command;
        };
    }
}
