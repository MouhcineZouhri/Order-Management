package com.example.ordermanagement.Dtos;

import com.example.ordermanagement.Entity.PriceTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String category;
    private int quantity;
    private Set<PriceTo> prices ;
}
