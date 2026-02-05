// File: StackScoutApplication.java
package com.stackscout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Главный класс приложения StackScout.
 * Инициализирует и запускает Spring Boot приложение.
 */
@SpringBootApplication
@EnableScheduling
public class StackScoutApplication {

    /**
     * Точка входа в приложение.
     * 
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(StackScoutApplication.class, args);
    }

}
