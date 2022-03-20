package com.example.ordermanagement.Factory;

import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    @Autowired
    private AppUserRepository appUserRepository;

     public AppUser createUser(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AppUser user = new AppUser();
        user.setUsername("mohsin"); user.setAddress("Address 1");
        user.setMobile("0612233453");user.setPassword(passwordEncoder.encode("1234"));

        return appUserRepository.save(user);
     }

    public AppUser createUser(String username){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AppUser user = new AppUser();
        user.setUsername(username); user.setAddress("Address 1");
        user.setMobile("0612233453");user.setPassword(passwordEncoder.encode("1234"));

        return appUserRepository.save(user);
    }

    public UserRegisterRequest createValidRegisterRequest(){
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("mohsin@gmail.com");
        request.setPassword("12345678");
        request.setRepassword("12345678");
        request.setFullName("mohsin zouhri");
        request.setMobile("0622604230");
        request.setAddress("Address 1");
        return request;
    }

     public UserUpdateInfoRequest createValidUpdateInfoRequest(){
         UserUpdateInfoRequest updateUserInfo = new UserUpdateInfoRequest();
         updateUserInfo.setUsername("mohsin@gmail.com");
         updateUserInfo.setImage("image1");
         updateUserInfo.setFullName("omar");
         updateUserInfo.setMobile("0612141516");
         updateUserInfo.setAddress("Address 1234");
         return updateUserInfo;
     }



}

