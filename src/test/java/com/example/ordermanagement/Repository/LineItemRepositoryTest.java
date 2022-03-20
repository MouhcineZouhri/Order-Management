package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Entity.*;
import com.example.ordermanagement.Enums.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class LineItemRepositoryTest
{
    @Autowired
    private LineItemRepository lineItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PriceRepository priceRepository;

    @Test
    public void test_find_line_item_by_order(){
        Product product = new Product();
        product.setName("Product 1");
        product.setQuantity(10);
        product.setCategory("Category 1");
        Product savedProduct = productRepository.save(product);

        PriceTo priceTo = new PriceTo();
        priceTo.setLast(true);
        priceTo.setProduct(savedProduct);
        priceTo.setPrice(new BigDecimal(1000));

        PriceTo savedPrice = priceRepository.save(priceTo);

        product.addPrice(savedPrice);

        AppUser appUser = new AppUser();
        appUser.setUsername("mohsin");
        AppUser user = appUserRepository.save(appUser);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setCreatedAt(new Date());
        Order savedOrder = orderRepository.save(order);

        user.addOrder(savedOrder);

        appUserRepository.save(user);

        LineItemKey lineItemKey = new LineItemKey();
        lineItemKey.setProductID(savedProduct.getId());
        lineItemKey.setOrderID(savedOrder.getId());

        LineItem lineItem = new LineItem();
        lineItem.setLineItemKey(lineItemKey);
        lineItem.setQuantity(5);
        lineItem.setOrder(savedOrder);
        lineItem.setProduct(savedProduct);
        LineItem savedLineItem = lineItemRepository.save(lineItem);

        product.addLineItem(savedLineItem);
        order.addLineItem(savedLineItem);

        int page =  0 , size = 2;
        Pageable pageable = PageRequest.of(page, size);

        Page<Object[]> responseQuery = lineItemRepository.getCartInfoByOrderAndOrderStatus(order.getId(),OrderStatus.CREATED ,pageable);

        List<CartItemResponseDTO> response = new ArrayList<>();

        for(Object[] item : responseQuery.getContent()){
            CartItemResponseDTO responseDTO = new CartItemResponseDTO();
            responseDTO.setOrderID(Long.parseLong(String.valueOf(item[0])));
            responseDTO.setProductID(Long.parseLong(String.valueOf(item[1])));
            responseDTO.setQuantity(Integer.parseInt(String.valueOf(item[2])));
            responseDTO.setName(String.valueOf(item[3]));
            responseDTO.setCategory(String.valueOf(item[4]));
            responseDTO.setPrice((BigDecimal)item[5]);
            response.add(responseDTO);
        }


        assertThat(response).isNotEmpty();
        assertThat(response.get(0).getOrderID()).isEqualTo(savedOrder.getId());
        assertThat(response.get(0).getProductID()).isEqualTo(savedProduct.getId());
        assertThat(response.get(0).getName()).isEqualTo(product.getName());
        assertThat(response.get(0).getCategory()).isEqualTo(product.getCategory());


    }

}