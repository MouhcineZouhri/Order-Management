package com.example.ordermanagement.AppConstants;

public class SecurityParams {
    public static final String TOKEN_PREFIX  = "Bearer ";
    public static final  String  SECRET = "Secret";
    public static final String  AUTHORIZATION_HEADER = "Authorization";
    public static final String  ROLES_HEADER_CLAIM = "roles";
    public static final String issuer ="localhost:8080/auth";
    public static final int  ACCESS_TOKEN_TIMING  =  365* 24* 60 * 60  * 1000 ;
    public static final  int  REFRESH_TOKEN_TIMING  =  365* 24  * 60 * 60  * 1000  ;
}
