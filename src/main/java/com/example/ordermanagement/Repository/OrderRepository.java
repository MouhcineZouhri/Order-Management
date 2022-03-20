package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.Order;
import com.example.ordermanagement.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order  , Long> {
    Optional<Order> findOrderByUserAndOrderStatus(UserDetails user , OrderStatus orderStatus);
}
