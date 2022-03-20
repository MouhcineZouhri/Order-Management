package com.example.ordermanagement.Controller;

public class ProductAlreadyInLineItemException extends RuntimeException{

    public ProductAlreadyInLineItemException(){
        super("Product Already in cart");
    }
}
