package com.stackscout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class StackScoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(StackScoutApplication.class, args);
    }

}
