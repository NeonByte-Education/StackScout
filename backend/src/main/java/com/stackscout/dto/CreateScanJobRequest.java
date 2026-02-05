// File: CreateScanJobRequest.java
package com.stackscout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект передачи данных (DTO) для запроса на создание новой задачи
 * сканирования.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateScanJobRequest {

    @NotBlank(message = "Источник обязателен (pypi, npm, dockerhub)")
    @Pattern(regexp = "^(pypi|npm|dockerhub)$", message = "Источник должен быть: pypi, npm или dockerhub")
    private String source;

    private Integer packagesCount;
}
