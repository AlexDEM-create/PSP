package com.flacko.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.common.role.UserRole;
import com.flacko.security.filter.AuthenticationFilter;
import com.flacko.security.filter.AuthorizationFilter;
import com.flacko.security.filter.ResponseHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String JWT_ALGORITHM_SECRET = "secret";
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_PATH = "/login";

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationProvider);
        authenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(LOGIN_PATH).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS).permitAll())

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/appeals")
                        .hasAnyAuthority(UserRole.MERCHANT.name(), UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/appeals", "/appeals/*")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name(),
                                UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/appeals/*/review", "/appeals/*/reject", "/appeals/*/resolve")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/merchants")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/merchants", "/merchants/*")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name(),
                                UserRole.MERCHANT.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/merchants/*")
                        .hasAnyAuthority(UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/trader-teams")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/trader-teams", "/trader-teams/*")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name(),
                                UserRole.TRADER_TEAM_LEADER.name(), UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/trader-teams/*")
                        .hasAnyAuthority(UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/trader-teams/*/incoming-online", "/trader-teams/*/incoming-offline",
                        "/trader-teams/*/outgoing-online", "/trader-teams/*/outgoing-offline")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/trader-teams/*/kick-out", "/trader-teams/*/get-back")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/users/me")
                        .permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/users")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/users", "/users/*")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/users/*")
                        .hasAnyAuthority(UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/users/*/ban")
                        .hasAnyAuthority(UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/payment-methods")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/payment-methods", "/payment-methods/*")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name(), UserRole.USER_SUPPORT.name(),
                                UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/payment-methods/*")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/payment-methods/*/enable", "/payment-methods/*/disable")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/terminals")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/terminals", "/terminals/*")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name(), UserRole.USER_SUPPORT.name(),
                                UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/terminals/*")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/terminals/*/enable", "/terminals/*/disable", "/terminals/*/verify",
                        "/terminals/*/healthcheck")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,
                        "/payment-verifications/receipts")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/payment-verifications/receipts", "/payment-verifications/receipts/*")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/balances/my")
                        .hasAnyAuthority(UserRole.MERCHANT.name(), UserRole.TRADER_TEAM.name(),
                                UserRole.TRADER_TEAM_LEADER.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/balances/trader-teams/*", "/balances/merchants/*/incoming",
                        "/balances/merchants/*/outgoing")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/balances/trader-teams/*/deposit", "/balances/trader-teams/*/withdraw",
                        "/balances/merchants/*/incoming/deposit", "/balances/merchants/*/incoming/withdraw",
                        "/balances/merchants/*/outgoing/deposit", "/balances/merchants/*/outgoing/withdraw")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))

                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/outgoing-payments/test")
                        .hasAnyAuthority(UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/outgoing-payments")
                        .hasAnyAuthority(UserRole.MERCHANT.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                        "/outgoing-payments", "/outgoing-payments/*")
                        .hasAnyAuthority(UserRole.MERCHANT.name(), UserRole.TRADER_TEAM.name(),
                                UserRole.USER_SUPPORT.name(), UserRole.USER_ADMIN.name()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,
                        "/outgoing-payments/*/reassign")
                        .hasAnyAuthority(UserRole.TRADER_TEAM.name(), UserRole.USER_SUPPORT.name(),
                                UserRole.USER_ADMIN.name()))

                // incoming payments
                // stats

                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ResponseHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
