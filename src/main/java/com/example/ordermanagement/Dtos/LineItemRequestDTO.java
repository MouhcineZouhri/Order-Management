package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LineItemRequestDTO {
    private Long productID;
    private Long orderID;
    private int quantity;
}
