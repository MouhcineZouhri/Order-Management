package com.example.ordermanagement.Exception;

import com.example.ordermanagement.Entity.LineItemKey;

public class LineItemNotFoundException extends RuntimeException{

    public LineItemNotFoundException(LineItemKey lineItemKey){
        super("cart item of product " + lineItemKey.getProductID() + " Not Found") ;
    }
}
