package com.example.ordermanagement.Service.Impl;

import com.example.ordermanagement.Exception.ProductAlreadyInLineItemException;
import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Dtos.LineItemRequestDTO;
import com.example.ordermanagement.Dtos.LineItemResponseDTO;
import com.example.ordermanagement.Entity.*;
import com.example.ordermanagement.Enums.OrderStatus;
import com.example.ordermanagement.Exception.*;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.LineItemRepository;
import com.example.ordermanagement.Repository.OrderRepository;
import com.example.ordermanagement.Repository.ProductRepository;
import com.example.ordermanagement.Service.LineItemService;
import com.example.ordermanagement.mapper.LineItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class LineItemServiceImpl implements LineItemService {
    private AppUserRepository appUserRepository;
    private LineItemRepository lineItemRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private LineItemMapper lineItemMapper;

    @Override
    public Page<CartItemResponseDTO> getAllLineItem(String username , Pageable pageable) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order order = orderRepository.findOrderByUserAndOrderStatus(user, OrderStatus.CREATED)
                .orElse(null);

        if(order == null) return Page.empty();

        Page<Object[]> responseQuery = lineItemRepository.getCartInfoByOrderAndOrderStatus(order.getId(), OrderStatus.CREATED, pageable);

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

        Page page = new PageImpl(response ,pageable , responseQuery.getTotalElements());

        return page;
    }

    @Override
    public LineItemResponseDTO saveLineItem(String username,LineItemRequestDTO itemRequestDTO) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Product product = productRepository.findById(itemRequestDTO.getProductID())
                .orElseThrow(() -> new ProductNotFoundException(itemRequestDTO.getProductID()));


        Order order = orderRepository.findOrderByUserAndOrderStatus(user , OrderStatus.CREATED)
                .orElse(null);


        if(order == null){
            order = new Order();
            order.setOrderStatus(OrderStatus.CREATED);
            order.setCreatedAt(new Date());
            order = orderRepository.save(order);
            user.addOrder(order);
        }


        LineItemKey lineItemKey = new LineItemKey();
        lineItemKey.setOrderID(order.getId());
        lineItemKey.setProductID(product.getId());

        // check if quantity sufficient
        if(product.getQuantity() < itemRequestDTO.getQuantity()) throw new QuantityNotSufficientException();

        LineItem checkLineItem = lineItemRepository.findById(lineItemKey).orElse(null);
        // check if product already demanded .
        if(checkLineItem != null) throw new ProductAlreadyInLineItemException();

        LineItem lineItem = new LineItem();
        lineItem.setLineItemKey(lineItemKey);
        lineItem.setQuantity(itemRequestDTO.getQuantity());
        lineItem.setProduct(product);
        lineItem.setOrder(order);
        LineItem savedLineItem = lineItemRepository.save(lineItem);

        product.addLineItem(savedLineItem);

        order.addLineItem(lineItem);

        orderRepository.save(order);
        productRepository.save(product);
        lineItemRepository.save(savedLineItem);

        return lineItemMapper.toLineItemResponseDTO(savedLineItem);
    }

    @Override
    public void removeLineItem(String username, LineItemKey lineItemKey) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order order = orderRepository.findById(lineItemKey.getOrderID())
                .orElseThrow(() -> new OrderNotFoundException(lineItemKey.getOrderID()));

        if(!order.getUser().getId().equals(user.getId())) throw new ForbiddenException();

        LineItem lineItem = lineItemRepository.findById(lineItemKey)
                .orElseThrow(() -> new LineItemNotFoundException(lineItemKey));

        lineItemRepository.delete(lineItem);

    }


    @Override
    public LineItemResponseDTO changeQuantity(String username,  LineItemRequestDTO itemRequestDTO) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        LineItemKey lineItemKey = new LineItemKey();

        lineItemKey.setOrderID(itemRequestDTO.getOrderID());
        lineItemKey.setProductID(itemRequestDTO.getProductID());

        Order order = orderRepository.findById(lineItemKey.getOrderID())
                .orElseThrow(() -> new OrderNotFoundException(lineItemKey.getOrderID()));

        // check if the user who create order.
        if(!order.getUser().getId().equals(user.getId())) throw new ForbiddenException();

        Product product = productRepository.findById(lineItemKey.getProductID())
                .orElseThrow(() -> new ProductNotFoundException(lineItemKey.getProductID()));

        // check if quantity sufficient
        if(product.getQuantity() < itemRequestDTO.getQuantity())
            throw new QuantityNotSufficientException();

        LineItem lineItem = lineItemRepository.findById(lineItemKey)
                .orElseThrow(() -> new LineItemNotFoundException(lineItemKey));

        lineItem.setQuantity(itemRequestDTO.getQuantity());

        LineItem savedLineItem = lineItemRepository.save(lineItem);

        return lineItemMapper.toLineItemResponseDTO(savedLineItem);
    }
}
