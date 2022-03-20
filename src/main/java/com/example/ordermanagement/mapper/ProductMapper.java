package com.example.ordermanagement.mapper;

import com.example.ordermanagement.Dtos.ProductRequestDTO;
import com.example.ordermanagement.Dtos.ProductResponseDTO;
import com.example.ordermanagement.Entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO productToProductResponseDTO(Product product);
    Product productRequestDTOToProduct(ProductRequestDTO productRequestDTO);
}
