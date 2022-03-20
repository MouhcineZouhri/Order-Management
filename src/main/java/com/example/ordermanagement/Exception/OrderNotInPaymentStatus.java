package com.example.ordermanagement.Exception;

public class OrderNotInPaymentStatus extends RuntimeException{

    public OrderNotInPaymentStatus(Long orderID){
        super("Order with id " + orderID + " Not in Payment Level");
    }

}
