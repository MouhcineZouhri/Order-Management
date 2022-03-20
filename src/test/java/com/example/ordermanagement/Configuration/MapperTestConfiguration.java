package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.mapper.LineItemMapper;
import com.example.ordermanagement.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MapperTestConfiguration {

    @Bean
    public UserMapper userMapper(){
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public LineItemMapper lineItemMapper(){
        return Mappers.getMapper(LineItemMapper.class);
    }
}
