package com.example.ordermanagement.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserUpdateInfoRequest {
    @Email(message = "username should be an Email")
    private String username;

    private String fullName;
    private String image;
    @Pattern(regexp="(^$|[0-9]{10})" , message = "mobile should be a phone number")
    private String mobile;
    private String address;
}
