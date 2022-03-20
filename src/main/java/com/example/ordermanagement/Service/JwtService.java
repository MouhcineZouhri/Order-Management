package com.example.ordermanagement.Service;


import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String accessToken(UserDetails user);
    String refreshToken(UserDetails user);

    DecodedJWT verifyToken(String token);
    String getUsername(String token);
    List<String> getRolesName(String token);
}
