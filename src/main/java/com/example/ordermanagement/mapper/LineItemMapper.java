package com.example.ordermanagement.mapper;

import com.example.ordermanagement.Dtos.LineItemResponseDTO;
import com.example.ordermanagement.Entity.LineItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LineItemMapper {
    LineItemResponseDTO toLineItemResponseDTO(LineItem lineItem);
}
