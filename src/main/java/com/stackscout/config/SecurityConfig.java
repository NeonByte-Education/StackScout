// File: SecurityConfig.java
package com.stackscout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 * В текущей реализации разрешает все запросы и отключает CSRF для упрощения
 * разработки.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Определение фильтров безопасности.
     * 
     * @param http Объект для настройки HTTP безопасности.
     * @return Сконфигурированная цепочка фильтров.
     * @throws Exception Если возникла ошибка при настройке.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
