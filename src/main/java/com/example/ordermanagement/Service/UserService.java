package com.example.ordermanagement.Service;


import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


public interface UserService {
    AppUser loadUserByUsername(String username);
    Page<UserResponseDto> findAllUsers(Pageable pageable);
    UserResponseDto getUser(Long id);
    UserDetails getUserByUserame(String username);
    UserResponseDto save(UserRegisterRequest request);
    UserResponseDto updateUser(UserUpdateInfoRequest user);
    UserResponseDto deleteUser(String username);
    UserResponseDto addRoleToUser(String username, String roleName);
    void logout(HttpServletRequest request) throws ServletException;
}
