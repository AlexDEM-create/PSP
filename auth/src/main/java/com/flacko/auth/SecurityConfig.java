package com.flacko.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.auth.filter.AuthenticationFilter;
import com.flacko.auth.filter.AuthorizationFilter;
import com.flacko.auth.filter.ResponseHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String JWT_ALGORITHM_SECRET = "secret";
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_PATH = "/login";
    public static final String TOKEN_REFRESH_PATH = "/token/refresh";
    public static final String USERS_PATH = "/users";

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(httpSecurity));
        authenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(LOGIN_PATH, TOKEN_REFRESH_PATH).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(POST, USERS_PATH).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(OPTIONS).permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ResponseHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)
            throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
