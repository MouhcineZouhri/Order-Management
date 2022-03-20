package com.example.ordermanagement.mapper;

import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void test_mapper_from_appUser_to_userResponseDto(){
        AppUser user = new AppUser();
        user.setUsername("mohsin");
        user.setPassword("1234");
        user.setImage("image1");

        Role role = new Role();
        role.setName("ADMIN");

        user.getRoles().add(role);

        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);

        assertThat(userResponseDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userResponseDto.getImage()).isEqualTo(user.getImage());

        assertThat(userResponseDto.getRoles()).contains(role);
    }
}