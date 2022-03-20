package com.example.ordermanagement.Dtos;

import com.example.ordermanagement.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor @NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String image;
    private String mobile;
    private String address;
    private List<Role> roles;
}
