package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.AppConstants.SecurityParams;
import com.example.ordermanagement.Service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    public JwtAuthorizationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(SecurityParams.AUTHORIZATION_HEADER);

        if(authorization !=null && authorization.startsWith(SecurityParams.TOKEN_PREFIX)){
            String token = authorization.substring(SecurityParams.TOKEN_PREFIX.length());

            String username = jwtService.getUsername(token);

            List<String> roles = jwtService.getRolesName(token);

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList
                    (roles.toArray(new String[roles.size()]));

            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(username , null , grantedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        }else filterChain.doFilter(request, response);
    }
}
