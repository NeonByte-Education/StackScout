// File: ScanJobService.java
package com.stackscout.service;

import com.stackscout.dto.CreateScanJobRequest;
import com.stackscout.dto.ScanJobDto;
import com.stackscout.model.ScanJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления жизненным циклом задач сканирования.
 */
public interface ScanJobService {

    /**
     * Получить все задачи с пагинацией
     */
    Page<ScanJobDto> getAllScanJobs(Pageable pageable);

    /**
     * Получить задачу по ID
     */
    ScanJobDto getScanJobById(Long id);

    /**
     * Создать новую задачу сканирования
     */
    ScanJobDto createScanJob(CreateScanJobRequest request);

    /**
     * Обновить статус задачи
     */
    ScanJobDto updateScanJobStatus(Long id, ScanJob.ScanStatus status);

    /**
     * Удалить задачу
     */
    void deleteScanJob(Long id);

    /**
     * Получить задачи по статусу
     */
    Page<ScanJobDto> getScanJobsByStatus(ScanJob.ScanStatus status, Pageable pageable);

    /**
     * Получить задачи по источнику
     */
    Page<ScanJobDto> getScanJobsBySource(String source, Pageable pageable);

    /**
     * Получить последние задачи по источнику
     */
    List<ScanJobDto> getRecentJobsBySource(String source, int limit);

    /**
     * Получить статистику по статусам
     */
    Map<String, Long> getStatusStatistics();
}
