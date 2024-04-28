package com.flacko.reporting;

import com.flacko.security.AuthorizationConfig;
import com.flacko.security.SecurityConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@Import({AuthorizationConfig.class, SecurityConfig.class})
@EnableJpaRepositories(basePackages = {"com.flacko"})
@EntityScan(basePackages = {"com.flacko"})
@ComponentScan(basePackages = {"com.flacko"})
public class ReportingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }

}
