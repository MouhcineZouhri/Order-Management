package com.example.ordermanagement.Service;

import com.example.ordermanagement.Entity.Order;

public interface orderService {
    public void payOrder(String username , Order order);

    public void cancelOrder(String username , Order order);

    public void shipOrder(String username, Order order);
}
