package com.example.ordermanagement.mapper;


import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser registerRequestToAppUser(UserRegisterRequest request);


    // use for testing only.
    UserResponseDto toUserResponseDtoFromUserRegisterRequest(UserRegisterRequest request);
    UserResponseDto toUserResponseDtoFromUserUpdateInfoRequest(UserUpdateInfoRequest request);


    UserResponseDto toUserResponseDto(AppUser user);
}
