package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.Factory.LineItemFactory;
import com.example.ordermanagement.Factory.OrderFactory;
import com.example.ordermanagement.Factory.ProductFactory;
import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.LineItemRepository;
import com.example.ordermanagement.Repository.OrderRepository;
import com.example.ordermanagement.Repository.ProductRepository;
import com.example.ordermanagement.Service.Impl.LineItemServiceImpl;
import com.example.ordermanagement.Service.LineItemService;
import com.example.ordermanagement.mapper.LineItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(MapperTestConfiguration.class)
@AllArgsConstructor
public class LineItemServiceConfiguration {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LineItemMapper lineItemMapper;

    @Bean
    public LineItemService lineItemService(){
        return new LineItemServiceImpl(appUserRepository ,lineItemRepository
                ,productRepository,orderRepository,lineItemMapper);
    }

    @Bean
    public UserFactory userFactory(){
        return new UserFactory();
    }

    @Bean
    public ProductFactory productFactory(){
        return new ProductFactory();
    }

    @Bean
    public OrderFactory orderFactory(){
        return new OrderFactory();
    }

    @Bean
    public LineItemFactory lineItemFactory(){
        return new LineItemFactory();
    }
}
