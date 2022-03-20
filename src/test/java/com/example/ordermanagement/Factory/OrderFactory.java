package com.example.ordermanagement.Factory;

import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Order;
import com.example.ordermanagement.Enums.OrderStatus;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderFactory {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public Order createOrder(AppUser user){
        Order order = new Order();

        order.setCreatedAt(new Date());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setUser(user);

        user.addOrder(order);

        appUserRepository.save(user);
        return orderRepository.save(order);

    }

}
