package com.example.ordermanagement.Controller;

import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Dtos.LineItemRequestDTO;
import com.example.ordermanagement.Dtos.LineItemResponseDTO;
import com.example.ordermanagement.Entity.LineItemKey;
import com.example.ordermanagement.Service.LineItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/lineItem")
@AllArgsConstructor
public class LineItemController {
    private LineItemService lineItemService;

    @GetMapping
    public Page<CartItemResponseDTO> getAll(Principal principal ,
                                            @RequestParam(defaultValue = "10") int size ,
                                            @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, size);

        return lineItemService.getAllLineItem(principal.getName(),pageable);
    }

    @PostMapping
    public LineItemResponseDTO addLineItem(Principal principal ,@Valid @RequestBody LineItemRequestDTO itemRequestDTO){
        return lineItemService.saveLineItem(principal.getName(), itemRequestDTO);
    }

    @PutMapping
    public LineItemResponseDTO changeQuantity(Principal principal ,@Valid @RequestBody LineItemRequestDTO itemRequestDTO ){
        return lineItemService.changeQuantity(principal.getName(),itemRequestDTO);
    }

    @DeleteMapping
    public void delete(Principal principal ,@RequestBody LineItemKey lineItemKey){
        lineItemService.removeLineItem(principal.getName(), lineItemKey);
    }
}
