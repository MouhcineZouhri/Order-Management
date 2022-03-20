package com.example.ordermanagement;

import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.Dtos.ProductRequestDTO;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.RoleRepository;
import com.example.ordermanagement.Service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class OrderManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



   // @Bean
    public CommandLineRunner runner(RoleRepository roleRepository
            , AppUserRepository appUserRepository, PasswordEncoder passwordEncoder
            , ProductService productService
    ){
        return args -> {
            Role admin = roleRepository.save(new Role(null, RoleConstants.ADMIN, null));
            Role userRole = roleRepository.save(new Role(null, RoleConstants.USER, null));
            AppUser user = new AppUser();
            user.setUsername("mohsin@gmail.com");
            user.setPassword(passwordEncoder.encode("12345678"));

            user.getRoles().add(admin);
            user.getRoles().add(userRole);
            appUserRepository.save(user);

            productService.saveProduct(
                    new ProductRequestDTO(null , "product 1" , "cat 1" , 10, BigDecimal.valueOf(1000)));
            
        };
    }

}
