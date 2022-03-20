package com.example.ordermanagement.Exception;

public class MismatchPasswordException extends RuntimeException {
    public MismatchPasswordException() {
        super("Password Mismatch");
    }
}
