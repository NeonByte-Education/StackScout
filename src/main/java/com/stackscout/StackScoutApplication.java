// File: StackScoutApplication.java
package com.stackscout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения StackScout.
 * Инициализирует и запускает Spring Boot приложение.
 */
@SpringBootApplication
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
