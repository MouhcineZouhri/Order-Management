package com.example.ordermanagement.Exception;

public class QuantityNotSufficientException extends RuntimeException{

    public QuantityNotSufficientException(){
        super("quantity demanding not sufficient");
    }
}
