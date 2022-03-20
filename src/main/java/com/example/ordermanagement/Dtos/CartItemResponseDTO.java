package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CartItemResponseDTO {
    private Long orderID;
    private Long productID;
    private int quantity;
    private String name;
    private String category;
    private BigDecimal price;
}
