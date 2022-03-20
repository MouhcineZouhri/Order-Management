package com.example.ordermanagement.Exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Role with name " + roleName + " Not Found");
    }
}
