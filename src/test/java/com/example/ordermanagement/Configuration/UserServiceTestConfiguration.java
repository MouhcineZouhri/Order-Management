package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.RoleRepository;
import com.example.ordermanagement.Service.UserService;
import com.example.ordermanagement.Service.Impl.UserServiceImpl;
import com.example.ordermanagement.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@Import(MapperTestConfiguration.class)
@AllArgsConstructor
public class UserServiceTestConfiguration {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserService setup(){
        return new UserServiceImpl(appUserRepository , roleRepository ,userMapper , passwordEncoder);
    }


    @Bean
    public UserFactory factory(){
        return new UserFactory();
    }

}
