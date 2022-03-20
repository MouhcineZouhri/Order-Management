package com.example.ordermanagement.Service;

import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Dtos.LineItemRequestDTO;
import com.example.ordermanagement.Dtos.LineItemResponseDTO;
import com.example.ordermanagement.Entity.LineItemKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LineItemService {
    Page<CartItemResponseDTO> getAllLineItem(String username , Pageable pageable);

    LineItemResponseDTO saveLineItem(String username ,LineItemRequestDTO itemRequestDTO);

    void  removeLineItem(String username , LineItemKey lineItemKey);

    LineItemResponseDTO changeQuantity(String username,  LineItemRequestDTO itemRequestDTO);


}

