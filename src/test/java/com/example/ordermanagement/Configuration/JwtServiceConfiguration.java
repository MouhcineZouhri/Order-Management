package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.Repository.RoleRepository;
import com.example.ordermanagement.Service.Impl.JwtServiceImpl;
import com.example.ordermanagement.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JwtServiceConfiguration {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public JwtService jwtService(){
        return new JwtServiceImpl(roleRepository);
    }
}


