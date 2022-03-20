package com.example.ordermanagement.Service.Impl;

import com.example.ordermanagement.Dtos.ProductRequestDTO;
import com.example.ordermanagement.Dtos.ProductResponseDTO;
import com.example.ordermanagement.Entity.PriceTo;
import com.example.ordermanagement.Entity.Product;
import com.example.ordermanagement.Exception.ProductNotFoundException;
import com.example.ordermanagement.Repository.PriceRepository;
import com.example.ordermanagement.Repository.ProductRepository;
import com.example.ordermanagement.Service.ProductService;
import com.example.ordermanagement.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@AllArgsConstructor
@Transactional
public class productServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private PriceRepository priceRepository;
    private ProductMapper productMapper;

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductResponseDTO> productResponse = products
                .map(product -> productMapper.productToProductResponseDTO(product));
        return productResponse;
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.productToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.productRequestDTOToProduct(productRequestDTO);
        Product dbProduct = productRepository.save(product);

        PriceTo price = new PriceTo();
        price.setPrice(productRequestDTO.getPrice());
        price.setDateFrom(new Date());

        PriceTo dbPrice = priceRepository.save(price);

        dbProduct.getPrices().add(dbPrice);

        Product save = productRepository.save(dbProduct);

        return productMapper.productToProductResponseDTO(save);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        if(productRequestDTO.getName() != null) product.setName(productRequestDTO.getName());
        if(productRequestDTO.getQuantity() != 0) product.setQuantity(productRequestDTO.getQuantity());
        if(productRequestDTO.getCategory() != null) product.setCategory(productRequestDTO.getCategory());

        Product savedProduct = productRepository.save(product);

        return productMapper.productToProductResponseDTO(savedProduct);
    }


    @Override
    public ProductResponseDTO removeProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
        return productMapper.productToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO changePrice(Long id, BigDecimal price) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        PriceTo lastPrice = priceRepository.findPriceToByProductAndLast(product, false);

        if(lastPrice !=null) {
            lastPrice.setLast(false);
            priceRepository.save(lastPrice);
        }

        PriceTo priceTo =new PriceTo();
        priceTo.setPrice(price);
        priceTo.setDateFrom(new Date());
        priceTo.setLast(true);

        PriceTo savedPrice = priceRepository.save(priceTo);
        product.addPrice(savedPrice);

        Product savedProduct = productRepository.save(product);

        return productMapper.productToProductResponseDTO(savedProduct);
    }
}
