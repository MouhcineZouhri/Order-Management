package com.example.ordermanagement.Dtos;

import com.example.ordermanagement.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
}
