package com.bmilab.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BmiLabBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmiLabBackendApplication.class, args);
    }

}
