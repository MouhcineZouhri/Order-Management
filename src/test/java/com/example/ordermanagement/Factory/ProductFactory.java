package com.example.ordermanagement.Factory;

import com.example.ordermanagement.Entity.PriceTo;
import com.example.ordermanagement.Entity.Product;
import com.example.ordermanagement.Repository.PriceRepository;
import com.example.ordermanagement.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class ProductFactory {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    public Product createProduct(){
        Product product = new Product();
        product.setName("Product 1");
        product.setQuantity(100);
        product.setCategory("Category 1");
        Product savedProduct = productRepository.save(product);

        PriceTo priceTo = new PriceTo();
        priceTo.setLast(true);
        priceTo.setDateFrom(new Date());
        priceTo.setPrice(BigDecimal.valueOf(10000));
        priceTo.setProduct(product);
        PriceTo savedPrice = priceRepository.save(priceTo);

        savedProduct.getPrices().add(savedPrice);

        return productRepository.save(savedProduct);

    }
}
