package com.example.ordermanagement.Service;

import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.Exception.MismatchPasswordException;
import com.example.ordermanagement.Exception.UserExistException;
import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Configuration.UserServiceTestConfiguration;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.Exception.RoleNotFoundException;
import com.example.ordermanagement.Exception.UserNotFoundException;
import com.example.ordermanagement.Repository.AppUserRepository;
import com.example.ordermanagement.Repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({UserServiceTestConfiguration.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    // loadUserByUsername Method
    @Test
    public void test_load_by_user_name(){
        AppUser createUser = userFactory.createUser();

        AppUser user = appUserRepository.save(createUser);

        AppUser testUser = userService.loadUserByUsername(user.getUsername());

        assertThat(testUser.getId()).isNotNull();

        assertThat(testUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(testUser.getImage()).isEqualTo(user.getImage());
        assertThat(testUser.getMobile()).isEqualTo(user.getMobile());
        assertThat(testUser.getAddress()).isEqualTo(user.getAddress());

    }

    // loadUserByUsername Method
    @Test
    public void test_generate_exception_for_user_not_exist(){
       assertThatThrownBy(() -> {
            userService.loadUserByUsername("mohsin");
       }).isInstanceOf(UserNotFoundException.class)
               .hasMessageContaining("User with username mohsin Not Found");
    }


    @Test
    // getUser by Id
    public void test_get_user_by_id(){
        AppUser createdUser = userFactory.createUser();
        AppUser user = appUserRepository.save(createdUser);

        UserResponseDto testedUser = userService.getUser(user.getId());

        assertThat(testedUser).extracting("id").isEqualTo(user.getId());
        assertThat(testedUser).extracting("image").isEqualTo(user.getImage());
        assertThat(testedUser).extracting("mobile").isEqualTo(user.getMobile());
        assertThat(testedUser).extracting("address").isEqualTo(user.getAddress());

    }

    @Test
    public void test_generate_exception_for_get_user_not_exist(){
        assertThatThrownBy(() -> {
                    userService.getUser(1L);
                }).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with username 1 Not Found");
    }
    // save

    @Test
    public void test_save_user(){
        UserRegisterRequest request  = userFactory.createValidRegisterRequest();

        roleRepository.save(new Role(null , RoleConstants.USER,null));

        UserResponseDto register = userService.save(request);

        Assertions.assertThat(register.getUsername()).isEqualTo(request.getUsername());
        Assertions.assertThat(register.getAddress()).isEqualTo(request.getAddress());
        Assertions.assertThat(register.getMobile()).isEqualTo(request.getMobile());

    }

    @Test
    public void test_generate_password_mismatch_exception_when_save_user_with_different_password(){
        UserRegisterRequest request   = new UserRegisterRequest();
        request.setPassword("1234");
        request.setRepassword("12345");

        Assertions.assertThatThrownBy(() -> {
                    userService.save(request);
                }).isInstanceOf(MismatchPasswordException.class)
                .hasMessageContaining("Password Mismatch");

    }


    @Test
    public void test_generate_user_exist_exception(){
        UserRegisterRequest request   = new UserRegisterRequest();
        String username = "mohsin@gmail.com";

        request.setUsername(username);
        request.setPassword("1234");
        request.setRepassword("1234");


        AppUser user= new AppUser();
        user.setUsername(username);

        appUserRepository.save(user);

        Assertions.assertThatThrownBy(() -> {
                    userService.save(request);
                }).isInstanceOf(UserExistException.class)
                .hasMessageContaining("User with username " + request.getUsername() + " Already Exsist");
    }



    // UpdateUser Method
    @Test
    public void test_update_user_exist_in_db(){
       AppUser createdUser = userFactory.createUser();
       AppUser user = appUserRepository.save(createdUser);

       UserUpdateInfoRequest request = new UserUpdateInfoRequest();

       String number ="1234" ;
       String image ="image1";
       request.setMobile(number);
       request.setImage(image);
       request.setUsername(user.getUsername());

       UserResponseDto savedUser = userService.updateUser(request);

       assertThat(savedUser)
               .extracting("mobile")
               .isEqualTo(number);

       assertThat(savedUser)
               .extracting("image")
               .isEqualTo(image);
    }

    // UpdateUser Method
    @Test
    public void test_generate_exception_for_update_user_not_exist(){
        UserUpdateInfoRequest request = new UserUpdateInfoRequest();
        request.setUsername("mohsin");
        assertThatThrownBy(() -> {
                    userService.updateUser(request);
                }).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with username "+request.getUsername()+ " Not Found");
    }
    // DeleteUser Method
    @Test
    public void test_delete_user_exist_in_db(){
        AppUser createdUser = userFactory.createUser();

        AppUser user = appUserRepository.save(createdUser);

        userService.deleteUser(user.getUsername());

        assertThatThrownBy(() -> {
            appUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user.getUsername()));
        }).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with username "+user.getUsername()+ " Not Found");
    }

    // DeleteUser Method
    @Test
    public void test_generate_exception_for_delete_user_not_exist(){
        AppUser user = new AppUser();
        user.setUsername("mohsin");
        assertThatThrownBy(() -> {
                    userService.deleteUser(user.getUsername());
                }).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with username "+user.getUsername()+ " Not Found");
    }


    // AddRoleToUser Method
    @Test
    public void test_that_can_add_role_to_user(){
        AppUser createdUser = userFactory.createUser();
        AppUser user = appUserRepository.save(createdUser);

        Role role = new Role();
        role.setName(RoleConstants.ADMIN);
        Role savedRole = roleRepository.save(role);

        // Add admin role to user.
        UserResponseDto testUser = userService.addRoleToUser(user.getUsername() ,RoleConstants.ADMIN);

        AppUser userWithRole = appUserRepository.findAppUserByUsername(user.getUsername()).orElse(null);

        Assertions.assertThat(userWithRole).isNotNull();
        Assertions.assertThat(userWithRole.getRoles()).contains(savedRole);
    }

    // AddRoleToUser Method
    @Test
    public void test_that_generate_exception_if_role_not_exist(){
        AppUser createdUser = userFactory.createUser();
        AppUser user = appUserRepository.save(createdUser);

        assertThatThrownBy(() -> {
            userService.addRoleToUser(user.getUsername(), RoleConstants.USER);
        }).isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role with name " + RoleConstants.USER.toString() + " Not Found");

    }

}
