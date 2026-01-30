package com.stackscout.controller;

import com.stackscout.dto.CreateScanJobRequest;
import com.stackscout.dto.ScanJobDto;
import com.stackscout.model.ScanJob;
import com.stackscout.service.ScanJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для управления задачами сканирования
 */
@RestController
@RequestMapping("/api/v1/scan-jobs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Scan Jobs", description = "API для управления задачами сканирования")
public class ScanJobController {

    private final ScanJobService scanJobService;

    @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач сканирования с пагинацией")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllScanJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ScanJobDto> jobsPage = scanJobService.getAllScanJobs(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobsPage.getContent());
        response.put("totalElements", jobsPage.getTotalElements());
        response.put("currentPage", jobsPage.getNumber());
        response.put("pageSize", jobsPage.getSize());
        response.put("totalPages", jobsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает детали конкретной задачи сканирования")
    @GetMapping("/{id}")
    public ResponseEntity<ScanJobDto> getScanJobById(@PathVariable Long id) {
        ScanJobDto scanJob = scanJobService.getScanJobById(id);
        return ResponseEntity.ok(scanJob);
    }

    @Operation(summary = "Создать новую задачу", description = "Создает новую задачу сканирования")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createScanJob(@Valid @RequestBody CreateScanJobRequest request) {
        ScanJobDto createdJob = scanJobService.createScanJob(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Задача сканирования успешно создана");
        response.put("job", createdJob);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Обновить статус задачи", description = "Обновляет статус задачи сканирования")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateScanJobStatus(
            @PathVariable Long id,
            @RequestParam ScanJob.ScanStatus status) {
        
        ScanJobDto updatedJob = scanJobService.updateScanJobStatus(id, status);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Статус задачи успешно обновлен");
        response.put("job", updatedJob);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удалить задачу", description = "Удаляет задачу сканирования из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteScanJob(@PathVariable Long id) {
        scanJobService.deleteScanJob(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Задача сканирования успешно удалена");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить задачи по статусу", description = "Возвращает задачи с указанным статусом")
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getScanJobsByStatus(
            @PathVariable ScanJob.ScanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ScanJobDto> jobsPage = scanJobService.getScanJobsByStatus(status, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobsPage.getContent());
        response.put("totalElements", jobsPage.getTotalElements());
        response.put("currentPage", jobsPage.getNumber());
        response.put("totalPages", jobsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить задачи по источнику", description = "Возвращает задачи для указанного источника")
    @GetMapping("/source/{source}")
    public ResponseEntity<Map<String, Object>> getScanJobsBySource(
            @PathVariable String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ScanJobDto> jobsPage = scanJobService.getScanJobsBySource(source, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobsPage.getContent());
        response.put("totalElements", jobsPage.getTotalElements());
        response.put("currentPage", jobsPage.getNumber());
        response.put("totalPages", jobsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить последние задачи", description = "Возвращает последние задачи для источника")
    @GetMapping("/recent")
    public ResponseEntity<List<ScanJobDto>> getRecentJobs(
            @RequestParam String source,
            @RequestParam(defaultValue = "5") int limit) {
        
        List<ScanJobDto> recentJobs = scanJobService.getRecentJobsBySource(source, limit);
        return ResponseEntity.ok(recentJobs);
    }

    @Operation(summary = "Получить статистику", description = "Возвращает статистику по статусам задач")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        Map<String, Long> stats = scanJobService.getStatusStatistics();
        return ResponseEntity.ok(stats);
    }
}
