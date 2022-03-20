package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class ChangePriceRequest {
    @NotNull
    private BigDecimal price;
}
