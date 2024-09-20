package com.ridango.payment; // Ensure this is the correct package for your application

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ridango") // Scans all sub-packages under com.ridango
@EnableJpaRepositories(basePackages = "com.ridango.game.repository")
@EntityScan(basePackages = "com.ridango.game.model")
public class CocktailGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailGameApplication.class, args);
    }
}