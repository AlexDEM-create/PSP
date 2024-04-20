package com.flacko.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.auth.security.filter.AuthenticationFilter;
import com.flacko.auth.security.filter.AuthorizationFilter;
import com.flacko.auth.security.filter.ResponseHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.flacko.auth.security.user.Role.*;
import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String JWT_ALGORITHM_SECRET = "secret";
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_PATH = "/login";
    public static final String TOKEN_REFRESH_PATH = "/token/refresh";
    public static final String USERS_PATH = "/users";

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationProvider);
        authenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(LOGIN_PATH, TOKEN_REFRESH_PATH).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(OPTIONS).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, USERS_PATH))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/merchants", "/traders")
                        .hasAuthority(USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PUT, "/merchants/*", "/traders/*")
                        .hasAuthority(USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/merchants", "/merchants/*", "/traders", "/traders/*")
                        .hasAnyAuthority(USER_ADMIN.name(), USER_SUPPORT.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/cards")
                        .hasAnyAuthority(TRADER_TEAM_LEADER.name(), TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/payments", "/appeals")
                        .hasAuthority(MERCHANT.name()))
                // change to authenticated()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .authenticationProvider(authenticationProvider)
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ResponseHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
