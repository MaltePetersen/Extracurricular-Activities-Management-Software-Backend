package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class FjoerdeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FjoerdeBackendApplication.class, args);
    }
}
