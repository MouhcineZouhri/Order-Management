package com.example.ordermanagement.Configuration;

import com.example.ordermanagement.AppConstants.RoleConstants;
import com.example.ordermanagement.Service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilter(new JwtAuthenticationFilter(authenticationManager(),jwtService));
        http.addFilterBefore(new JwtAuthorizationFilter(jwtService) ,  UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests().antMatchers("/users/**" , "/products/**").hasAuthority(RoleConstants.ADMIN);
        http.authorizeRequests().antMatchers("/register/**","/refresh/**" ,"/h2-console/**" ,"/login/**")
                .permitAll();

        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**" , "/swagger-ui/**" , "/v3/api-docs/**");
        super.configure(web);
    }
}
