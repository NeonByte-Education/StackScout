// File: CreateLibraryRequest.java
package com.stackscout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания новой библиотеки
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLibraryRequest {
    
    @NotBlank(message = "Название библиотеки обязательно")
    private String name;
    
    @NotBlank(message = "Версия обязательна")
    private String version;
    
    @NotBlank(message = "Источник обязателен (pypi, npm, dockerhub)")
    @Pattern(regexp = "^(pypi|npm|dockerhub)$", message = "Источник должен быть: pypi, npm или dockerhub")
    private String source;
    
    private String license;
    
    @Min(value = 0, message = "Оценка здоровья должна быть >= 0")
    private Integer healthScore;
    
    private String lastRelease;
    
    private String repository;
    
    private String description;
}
