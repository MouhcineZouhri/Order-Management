package com.example.ordermanagement.Exception;

public class ProductAlreadyInLineItemException extends RuntimeException{

    public ProductAlreadyInLineItemException(){
        super("Product Already in cart");
    }
}
