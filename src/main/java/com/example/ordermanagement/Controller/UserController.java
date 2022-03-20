package com.example.ordermanagement.Controller;

import com.example.ordermanagement.Dtos.AddRoleToUserRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController
{
    private UserService userService;

    @GetMapping
    public Page<UserResponseDto> all(@RequestParam(defaultValue = "10") int size ,
                                     @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAllUsers(pageable);
    }

    @GetMapping("/{id}")
    public UserResponseDto one(@PathVariable Long id){
        return userService.getUser(id);
    }


    @PutMapping
    public UserResponseDto update(@Valid @RequestBody UserUpdateInfoRequest request){
        return userService.updateUser(request);
    }

    @DeleteMapping
    public UserResponseDto delete(@Valid @RequestBody String username){
        return userService.deleteUser(username);
    }

    @PostMapping("/role")
    public UserResponseDto addRoleToUser(@Valid @RequestBody AddRoleToUserRequest request){
        return userService.addRoleToUser(request.getUsername() ,request.getRoleName());
    }
}