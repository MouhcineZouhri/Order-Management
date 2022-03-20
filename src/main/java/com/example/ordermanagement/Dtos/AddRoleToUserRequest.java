package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor @NoArgsConstructor
public class AddRoleToUserRequest {
    @Email(message = "username should be an Email")
    @NotBlank(message = "username should be Defined")
    private String username;
    @NotBlank(message = "roleName should be Defined")
    private String roleName;
}

