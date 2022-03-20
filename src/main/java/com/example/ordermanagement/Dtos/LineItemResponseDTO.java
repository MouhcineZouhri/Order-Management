package com.example.ordermanagement.Dtos;

import com.example.ordermanagement.Entity.LineItemKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class LineItemResponseDTO {
    private LineItemKey lineItemKey;
    private int quantity;
}
