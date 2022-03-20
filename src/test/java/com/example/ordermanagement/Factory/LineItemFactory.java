package com.example.ordermanagement.Factory;

import com.example.ordermanagement.Exception.ProductAlreadyInLineItemException;
import com.example.ordermanagement.Entity.*;
import com.example.ordermanagement.Exception.OrderNotFoundException;
import com.example.ordermanagement.Exception.ProductNotFoundException;
import com.example.ordermanagement.Repository.LineItemRepository;
import com.example.ordermanagement.Repository.OrderRepository;
import com.example.ordermanagement.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LineItemFactory {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LineItemRepository lineItemRepository;

    public LineItem createLineItem(Order  order , Product product , int quantity){
        orderRepository.findById(order.getId()).orElseThrow(() -> new OrderNotFoundException(order.getId()));
        productRepository.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException(product.getId()));

        LineItemKey lineItemKey = new LineItemKey();
        lineItemKey.setProductID(product.getId());
        lineItemKey.setOrderID(order.getId());

        LineItem dbLineItem = lineItemRepository.findById(lineItemKey).orElse(null);

        if(dbLineItem != null) throw new ProductAlreadyInLineItemException();

        LineItem lineItem = new LineItem();
        lineItem.setLineItemKey(lineItemKey);
        lineItem.setProduct(product);
        lineItem.setOrder(order);
        lineItem.setQuantity(quantity);

        LineItem savedLineItem = lineItemRepository.save(lineItem);

        product.addLineItem(savedLineItem);
        order.addLineItem(savedLineItem);

        productRepository.save(product);
        orderRepository.save(order);

        return savedLineItem;
    }


}
