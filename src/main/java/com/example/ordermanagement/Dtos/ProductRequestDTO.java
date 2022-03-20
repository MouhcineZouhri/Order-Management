package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data @AllArgsConstructor @NoArgsConstructor
public class ProductRequestDTO {
    private Long id;
    private String name;
    private String category;
    private int quantity;
    private BigDecimal price;
}
