package com.example.ordermanagement.Controller;

import com.example.ordermanagement.Dtos.OrderResponse;
import com.example.ordermanagement.Dtos.PaymentRequestDTO;
import com.example.ordermanagement.Service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @PostMapping("/pay")
    public OrderResponse payOrder(Principal principal , @RequestBody PaymentRequestDTO paymentRequestDTO){
        return orderService.payOrder(principal.getName() , paymentRequestDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/ship")
    public OrderResponse shipOrder(@PathVariable Long id){
        return  orderService.shipOrder(id);
    }
}
