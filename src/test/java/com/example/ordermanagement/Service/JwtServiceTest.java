package com.example.ordermanagement.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.AppConstants.SecurityParams;
import com.example.ordermanagement.Configuration.JwtServiceConfiguration;
import com.example.ordermanagement.Entity.AppUser;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.Repository.RoleRepository;
import com.example.ordermanagement.Service.Impl.JwtServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;



@Import(JwtServiceConfiguration.class)
@DataJpaTest
class JwtServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    public void test_generate_access_token(){
        AppUser user= new AppUser();
        user.setUsername("mohsin");
        Role role = new Role();
        role.setName(RoleConstants.ADMIN);
        roleRepository.save(role);

        user.getRoles().add(role);

        Collection<SimpleGrantedAuthority> authorities =(Collection<SimpleGrantedAuthority>) user.getAuthorities();

        authorities.add(new SimpleGrantedAuthority(role.getName()));

        String accessToken = jwtService.accessToken(user);

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);

        List<String> roles = decodedJWT.getClaim(SecurityParams.ROLES_HEADER_CLAIM).asList(String.class);

        Assertions.assertThat(decodedJWT).extracting("subject")
                .isEqualTo(user.getUsername());

        Assertions.assertThat(roles).contains(role.getName());

    }


    @Test
    public void test_generate_refresh_token(){
        AppUser user= new AppUser();
        user.setUsername("mohsin");

        String refreshToken = jwtService.refreshToken(user);

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);

        List<String> roles = decodedJWT.getClaim(SecurityParams.ROLES_HEADER_CLAIM).asList(String.class);

        Assertions.assertThat(decodedJWT).extracting("subject")
                .isEqualTo(user.getUsername());

    }


}