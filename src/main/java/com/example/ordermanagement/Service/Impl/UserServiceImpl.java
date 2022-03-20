package com.example.ordermanagement.Service.Impl;

import com.example.ordermanagement.Service.UserService;
import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.Exception.MismatchPasswordException;
import com.example.ordermanagement.Exception.RoleNotFoundException;
import com.example.ordermanagement.Exception.UserExistException;
import com.example.ordermanagement.Exception.UserNotFoundException;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.RoleRepository;
import com.example.ordermanagement.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser loadUserByUsername(String username)
    {
        return appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

    }

    @Override
    public Page<UserResponseDto> findAllUsers(Pageable pageable) {
        Page<AppUser> all = appUserRepository.findAll(pageable);

        Page<UserResponseDto> result = all.map(user -> userMapper.toUserResponseDto(user));

        return result;
    }

    @Override
    public UserResponseDto getUser(Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
        return  userMapper.toUserResponseDto(user);
    }

    @Override
    public UserDetails getUserByUserame(String username) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return user;
    }

    @Override
    public UserResponseDto save(UserRegisterRequest request) {
        if(!request.getPassword().equals(request.getRepassword())){
            throw new MismatchPasswordException();
        }

        try {
            // Generate Exception if Exist a User with that Username.
            this.loadUserByUsername(request.getUsername());
            throw new UserExistException(request.getUsername());

        }catch (UserNotFoundException e){
            // create  a User if Not Found.
            AppUser user = userMapper.registerRequestToAppUser(request);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // attach User Role to User by Default.
            Role role = roleRepository.findRolesByName(RoleConstants.USER)
                    .orElseThrow(() -> new RoleNotFoundException(RoleConstants.USER));

            user.getRoles().add(role);

            AppUser savedUser = appUserRepository.save(user);

            return userMapper.toUserResponseDto(savedUser);
        }

    }

    @Override
    public UserResponseDto updateUser(UserUpdateInfoRequest user) {
        AppUser dbUser = appUserRepository.findAppUserByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()))
                ;

        if(user.getFullName() != null) dbUser.setFullName(user.getFullName());
        if(user.getAddress() != null) dbUser.setAddress(user.getAddress());
        if(user.getMobile() != null) dbUser.setMobile(user.getMobile());
        if(user.getImage() !=null) dbUser.setImage(user.getImage());

        AppUser saved = appUserRepository.save(dbUser);

        return userMapper.toUserResponseDto(saved);
    }

    @Override
    public UserResponseDto deleteUser(String username)  {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        appUserRepository.delete(user);

        return  userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepository.findAppUserByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));

        Role role = roleRepository.findRolesByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        user.getRoles().add(role);

        roleRepository.save(role);
        AppUser savedUser = appUserRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }


    @Override
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
