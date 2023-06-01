package com.sankha.estore.ProductService;

import com.sankha.estore.ProductService.command.interceptors.CreateProductCommandInterceptor;
import com.sankha.estore.ProductService.config.AxonXstreamConfig;
import com.sankha.estore.ProductService.core.errorhandling.ProductServiceEventErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@Import({ AxonXstreamConfig.class })
//https://stackoverflow.com/questions/75604172/forbiddenclassexception-appears-during-handling-axon-event
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context,
														CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));

	}

	@Autowired
	public void configure(EventProcessingConfigurer eventProcessingConfigurer){
		eventProcessingConfigurer.registerListenerInvocationErrorHandler("product-group",
				configuration -> new ProductServiceEventErrorHandler());
	}

}
