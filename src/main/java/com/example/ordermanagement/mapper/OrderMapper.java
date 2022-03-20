package com.example.ordermanagement.mapper;


import com.example.ordermanagement.Dtos.OrderResponse;
import com.example.ordermanagement.Entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
     OrderResponse toOrderResponse(Order order);
}
