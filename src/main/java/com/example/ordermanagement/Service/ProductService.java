package com.example.ordermanagement.Service;


import com.example.ordermanagement.Dtos.ProductRequestDTO;
import com.example.ordermanagement.Dtos.ProductResponseDTO;
import com.example.ordermanagement.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    ProductResponseDTO getProduct(Long id);

    ProductResponseDTO saveProduct(ProductRequestDTO product);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO product);

    ProductResponseDTO removeProduct(Long id);

    ProductResponseDTO changePrice(Long id, BigDecimal price);
}
