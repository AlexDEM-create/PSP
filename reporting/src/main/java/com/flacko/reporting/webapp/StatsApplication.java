package com.flacko.reporting.webapp;

import com.flacko.security.AuthorizationConfig;
import com.flacko.security.SecurityConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@Import({AuthorizationConfig.class, SecurityConfig.class})
@EnableJpaRepositories(basePackages = {"com.flacko"})
@EntityScan(basePackages = {"com.flacko"})
@ComponentScan(basePackages = {"com.flacko"})
public class StatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }

}
