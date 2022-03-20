package com.example.ordermanagement.Exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long orderID){
        super("Order with id " + orderID  + " Not found");
    }

    public OrderNotFoundException(String username){
        super("User with username "+ username + " has no Order");
    }
}

