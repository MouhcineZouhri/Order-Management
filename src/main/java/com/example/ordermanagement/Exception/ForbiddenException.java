package com.example.ordermanagement.Exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super("You are Not Allowed");
    }
}
