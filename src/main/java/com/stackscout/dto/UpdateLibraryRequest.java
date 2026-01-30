package com.stackscout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления существующей библиотеки
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLibraryRequest {
    
    private String name;
    
    private String version;
    
    @Pattern(regexp = "^(pypi|npm|dockerhub)$", message = "Источник должен быть: pypi, npm или dockerhub")
    private String source;
    
    private String license;
    
    @Min(value = 0, message = "Оценка здоровья должна быть >= 0")
    private Integer healthScore;
    
    private String lastRelease;
    
    private String repository;
    
    private String description;
}
