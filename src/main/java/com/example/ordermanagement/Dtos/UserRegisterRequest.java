package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserRegisterRequest {
    @Email(message = "username should be an email")
    @NotNull(message = "username is obligated")
    private String username;

    @NotNull
    private String fullName;

    @Length(min = 8 , message = "password should have minimum 8")
    @NotBlank
    private String password;

    @NotNull
    private String repassword;

    @NotNull
    private String address;

    @Pattern(regexp="(^$|[0-9]{10})" , message = "mobile should be a phone number")
    private String mobile;
}
