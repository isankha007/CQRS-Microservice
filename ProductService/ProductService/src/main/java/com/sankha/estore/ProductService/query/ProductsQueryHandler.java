package com.sankha.estore.ProductService.query;

import com.sankha.estore.ProductService.core.data.ProductEntity;
import com.sankha.estore.ProductService.core.data.ProductRepository;
import com.sankha.estore.ProductService.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {
    private ProductRepository productRepository;

    public ProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query){
        List<ProductRestModel> productsRest = new ArrayList<>();

        List<ProductEntity> storedProducts =  productRepository.findAll();

        for (ProductEntity storedProduct : storedProducts) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(storedProduct, productRestModel);
            productsRest.add(productRestModel);
        }

        return productsRest;

    }
}
