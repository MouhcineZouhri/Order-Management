package com.example.ordermanagement.Controller;

import com.example.ordermanagement.Dtos.ChangePriceRequest;
import com.example.ordermanagement.Dtos.ProductRequestDTO;
import com.example.ordermanagement.Dtos.ProductResponseDTO;
import com.example.ordermanagement.Entity.Product;
import com.example.ordermanagement.Service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping
    public Page<ProductResponseDTO> all(@RequestParam(defaultValue = "10") int size ,
                                        @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getOne(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PostMapping
    public ProductResponseDTO save(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        return productService.saveProduct(productRequestDTO);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id ,
                                     @Valid @RequestBody ProductRequestDTO productRequestDTO){
        return productService.updateProduct(id , productRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ProductResponseDTO deleteOne(@PathVariable Long id){
        return productService.removeProduct(id);
    }


    @PostMapping("/{id}/prices")
    public ProductResponseDTO changePrice(@PathVariable Long id , @Valid @RequestBody ChangePriceRequest changePriceRequest){
        return productService.changePrice(id, changePriceRequest.getPrice());
    }
}
