package com.example.ordermanagement.Service;


import com.example.ordermanagement.Dtos.OrderResponse;
import com.example.ordermanagement.Dtos.PaymentRequestDTO;

public interface OrderService {
    public OrderResponse payOrder(String username , PaymentRequestDTO paymentRequestDTO);

    public OrderResponse shipOrder(Long orderID);
}
