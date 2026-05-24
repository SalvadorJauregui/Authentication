package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class AuthApplication {

    private static final Logger LOGGER = LogManager.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting Auth Application");
        SpringApplication.run(AuthApplication.class, args);
    }
}
