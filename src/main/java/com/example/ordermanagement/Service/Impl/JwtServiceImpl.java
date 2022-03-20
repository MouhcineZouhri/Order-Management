package com.example.ordermanagement.Service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ordermanagement.AppConstants.SecurityParams;
import com.example.ordermanagement.Service.JwtService;
import com.example.ordermanagement.Entity.Role;
import com.example.ordermanagement.Exception.RoleNotFoundException;
import com.example.ordermanagement.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private RoleRepository roleRepository;

    @Override
    public String accessToken(UserDetails user) {
        List<String> roles = user.getAuthorities().stream()
                .map(grantedAuthority -> {
                    Role role = roleRepository.findRolesByName(grantedAuthority.getAuthority())
                            .orElseThrow(() -> new RoleNotFoundException(grantedAuthority.getAuthority()));
                    return role.getName();
                })
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.ACCESS_TOKEN_TIMING))
                .withIssuer(SecurityParams.issuer)
                .withArrayClaim(SecurityParams.ROLES_HEADER_CLAIM , roles.toArray(new String[roles.size()]))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
    }

    @Override
    public String refreshToken(UserDetails user) {
          return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.REFRESH_TOKEN_TIMING))
                .withIssuer(SecurityParams.issuer)
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
        DecodedJWT verify = jwtVerifier.verify(token);

        return  verify;
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT verifier = verifyToken(token);
        return verifier.getSubject();
    }

    @Override
    public List<String> getRolesName(String token) {
        DecodedJWT verifier = verifyToken(token);

        List<String> roles = verifier.getClaim(SecurityParams.ROLES_HEADER_CLAIM).asList(String.class);

        return roles;
    }


}
