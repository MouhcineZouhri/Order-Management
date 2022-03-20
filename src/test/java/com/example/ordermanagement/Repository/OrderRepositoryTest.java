package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Order;
import com.example.ordermanagement.Enums.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void test_find_order_by_user_and_order_status(){
        AppUser appUser = new AppUser();
        appUser.setUsername("mohsin");

        AppUser user = appUserRepository.save(appUser);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);

        user.addOrder(savedOrder);

        appUserRepository.save(user);

        Order dbOrder =
                orderRepository.findOrderByUserAndOrderStatus(user, OrderStatus.CREATED)
                                .orElse(null);


        assertThat(dbOrder).isNotNull();
        assertThat(dbOrder.getUser().getId()).isEqualTo(user.getId());
    }
}