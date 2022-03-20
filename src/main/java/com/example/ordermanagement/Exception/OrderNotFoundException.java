package com.example.ordermanagement.Exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long orderID){
        super("Order with id " + orderID  + " Not found");
    }
}

