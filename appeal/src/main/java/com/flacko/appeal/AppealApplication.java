package com.flacko.appeal;

import com.flacko.auth.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@Import(SecurityConfig.class)
public class AppealApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppealApplication.class, args);
    }

}
