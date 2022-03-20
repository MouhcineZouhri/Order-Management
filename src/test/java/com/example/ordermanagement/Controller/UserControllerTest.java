package com.example.ordermanagement.Controller;

import com.example.ordermanagement.Configuration.MapperTestConfiguration;
import com.example.ordermanagement.Dtos.AddRoleToUserRequest;
import com.example.ordermanagement.Dtos.UserRegisterRequest;
import com.example.ordermanagement.Dtos.UserResponseDto;
import com.example.ordermanagement.Dtos.UserUpdateInfoRequest;
import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.Factory.UserFactory;
import com.example.ordermanagement.Service.JwtService;
import com.example.ordermanagement.Service.UserService;
import com.example.ordermanagement.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = {UserController.class , AuthController.class})
@Import({MapperTestConfiguration.class, UserFactory.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserMapper userMapper;


    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    public void test_create_user_by_anyone() throws Exception {
        UserRegisterRequest request = userFactory.createValidRegisterRequest();

        UserResponseDto responseDto = userMapper.toUserResponseDtoFromUserRegisterRequest(request);

        Mockito.when(userService.save(request)).thenReturn(responseDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                post("/register")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(responseDto.getUsername()));

        Mockito.verify(userService).save(request);
    }




    @WithMockUser(authorities = {RoleConstants.ADMIN})
    @Test
    public void test_delete_user_by_admin() throws Exception {
        UserRegisterRequest request = userFactory.createValidRegisterRequest();

        UserResponseDto responseDto = userMapper.toUserResponseDtoFromUserRegisterRequest(request);

        String username ="mohsin";

        Mockito.when(userService.deleteUser(username)).thenReturn(responseDto);

        mockMvc.perform(
                        delete("/users")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(username)
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(responseDto.getUsername()));

        Mockito.verify(userService).deleteUser(username);
    }


    @Test
    public void test_connot_delete_user_by_non_admin() throws Exception {
        UserRegisterRequest request = userFactory.createValidRegisterRequest();

        UserResponseDto responseDto = userMapper.toUserResponseDtoFromUserRegisterRequest(request);

        String username ="mohsin";

        Mockito.when(userService.deleteUser(username)).thenReturn(responseDto);

        mockMvc.perform(
                        delete("/users")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(username)
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @WithMockUser(authorities = {RoleConstants.ADMIN})
    @Test
    public void test_update_user_info_by_admin() throws Exception {

        UserUpdateInfoRequest updateInfoRequest = userFactory.createValidUpdateInfoRequest();

        UserResponseDto userResponseDto = userMapper.toUserResponseDtoFromUserUpdateInfoRequest(updateInfoRequest);

        Mockito.when(userService.updateUser(updateInfoRequest)).thenReturn(userResponseDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        put("/users")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(updateInfoRequest))
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userResponseDto.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobile").value(userResponseDto.getMobile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(userResponseDto.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(userResponseDto.getImage()));
    }



    @WithMockUser(authorities = {RoleConstants.ADMIN})
    @Test
    public void test_add_role_to_user_info_by_admin() throws Exception {
        AddRoleToUserRequest request = new AddRoleToUserRequest("mohsin@gmail.com" , RoleConstants.USER);

        UserResponseDto responseDto = new UserResponseDto();

        Mockito.when(userService.addRoleToUser(request.getUsername() , request.getRoleName()))
                .thenReturn(responseDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        post("/users/role")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @WithMockUser(authorities = {RoleConstants.USER})
    @Test
    public void test_cannot_add_role_to_user_info_by_non_admin() throws Exception {
        AddRoleToUserRequest request = new AddRoleToUserRequest("mohsin@gmail.com" , RoleConstants.USER);

        UserResponseDto responseDto = new UserResponseDto();

        Mockito.when(userService.addRoleToUser(request.getUsername() , request.getRoleName()))
                .thenReturn(responseDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        post("/users/role")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}