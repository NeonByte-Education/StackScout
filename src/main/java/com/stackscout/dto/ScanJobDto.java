// File: ScanJobDto.java
package com.stackscout.dto;

import com.stackscout.model.ScanJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Объект передачи данных (DTO) с информацией о состоянии и параметрах задачи
 * сканирования.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanJobDto {

    private Long id;
    private String source;
    private ScanJob.ScanStatus status;
    private Integer packagesCount;
    private Integer processedCount;
    private Integer failedCount;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Прогресс выполнения в процентах
     */
    public Double getProgress() {
        if (packagesCount == null || packagesCount == 0) {
            return 0.0;
        }
        int processed = processedCount != null ? processedCount : 0;
        return (processed * 100.0) / packagesCount;
    }
}
