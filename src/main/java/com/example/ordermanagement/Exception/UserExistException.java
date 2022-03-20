package com.example.ordermanagement.Exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String username) {
        super("User with username " + username + " Already Exsist");
    }
}
