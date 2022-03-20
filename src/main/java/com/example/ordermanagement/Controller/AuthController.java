package com.example.ordermanagement.Controller;

import com.example.ordermanagement.AppConstants.SecurityParams;
import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Service.JwtService;
import com.example.ordermanagement.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    private JwtService jwtService;


    @PostMapping("/register")
    public UserResponseDto save(@Valid @RequestBody UserRegisterRequest request){
        return userService.save(request);
    }


    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request , HttpServletResponse response){
        String authorization = request.getHeader(SecurityParams.AUTHORIZATION_HEADER);

        if(authorization !=null && authorization.startsWith(SecurityParams.TOKEN_PREFIX)){
            String refreshToken = authorization.substring(SecurityParams.TOKEN_PREFIX.length());

            String username = jwtService.getUsername(refreshToken);

            UserDetails user = userService.getUserByUserame(username);

            String accessToken = jwtService.accessToken(user);

            response.addHeader("access_token" , accessToken);
            response.addHeader("refresh_token" ,refreshToken);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        userService.logout(request);
    }

}
