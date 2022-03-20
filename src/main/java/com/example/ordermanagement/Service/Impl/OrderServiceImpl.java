package com.example.ordermanagement.Service.Impl;

import com.example.ordermanagement.Dtos.OrderResponse;
import com.example.ordermanagement.Dtos.PaymentRequestDTO;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Order;
import com.example.ordermanagement.Entity.Payment;
import com.example.ordermanagement.Enums.OrderStatus;
import com.example.ordermanagement.Exception.OrderNotFoundException;
import com.example.ordermanagement.Exception.OrderNotInPaymentStatus;
import com.example.ordermanagement.Exception.PaymentException;
import com.example.ordermanagement.Exception.UserNotFoundException;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.LineItemRepository;
import com.example.ordermanagement.Repository.OrderRepository;
import com.example.ordermanagement.Repository.PaymentRepository;
import com.example.ordermanagement.Service.OrderService;
import com.example.ordermanagement.mapper.OrderMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private AppUserRepository appUserRepository;
    private LineItemRepository lineItemRepository;
    private PaymentRepository paymentRepository;

    private OrderMapper orderMapper;
    @Override
    public OrderResponse payOrder(String username, PaymentRequestDTO paymentRequestDTO) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order order = orderRepository.findOrderByUserAndOrderStatus(user, OrderStatus.CREATED)
                .orElseThrow(() -> new OrderNotFoundException(username));

        Map<String ,Object> map= new HashMap<>();

        BigDecimal amount = BigDecimal.ZERO;

        List<Object[]> info = lineItemRepository.getQuantityAndPriceLineItemOfOrder(order.getId(), OrderStatus.CREATED);

        for(Object[] item : info){

            int quantity =  (int) item[0];
            BigDecimal price = (BigDecimal) item[1];

            amount = amount.add(price.multiply(BigDecimal.valueOf(quantity)));
        }


        map.put("amount" , amount.intValue());
        map.put("currency" ,"usd");
        map.put("source"  , paymentRequestDTO.getToken());

        try {
            Charge charge = Charge.create(map);

            // create a payment for order.
            Payment payment = new Payment();
            payment.setAmount(BigDecimal.valueOf(charge.getAmount()));
            payment.setDate(new Date());
            payment.setUser(user);
            payment.setOrder(order);
            payment.setType(paymentRequestDTO.getType());
            payment.setPaymentDescription(charge.getDescription());

            paymentRepository.save(payment);

            order.setOrderStatus(OrderStatus.PAYED);

            Order savedOrder = orderRepository.save(order);

            return orderMapper.toOrderResponse(savedOrder);
        } catch (StripeException e) {
            throw new PaymentException();
        }
    }

    @Override
    public OrderResponse shipOrder(Long orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new OrderNotFoundException(orderID));

        if(order.getOrderStatus() != OrderStatus.PAYED) throw new OrderNotInPaymentStatus(order.getId());

        order.setOrderStatus(OrderStatus.SHIPPED);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }
}
