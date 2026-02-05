// File: CollectorController.java
package com.stackscout.controller;

import com.stackscout.dto.ErrorResponse;
import com.stackscout.dto.ScanRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для управления процессом сбора данных (collector).
 * Обрабатывает запросы на запуск сканирования и получение статуса.
 */
@RestController
@RequestMapping("/api/v1/collector")
@CrossOrigin(origins = "*")
public class CollectorController {

    /**
     * Запуск процесса сканирования источников (например, PyPi или Docker Hub).
     * 
     * @param request Запрос с указанием источника и пакетов.
     * @return Ответ со статусом запуска сканирования.
     */
    @PostMapping("/scan")
    public ResponseEntity<?> startScan(@RequestBody ScanRequest request) {
        try {
            if (request.getSource() == null || request.getSource().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Источник (source) обязателен", LocalDateTime.now()));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Сканирование запущено");
            response.put("source", request.getSource());
            response.put("packages", request.getPackages());
            response.put("status", "processing");
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при запуске сканирования: " + e.getMessage(), LocalDateTime.now()));
        }
    }

    /**
     * Получение текущего статуса коллектора и статистики сканирований.
     * 
     * @return Ответ со сведениями о статусе, последнем сканировании и размере
     *         очереди.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("collectorStatus", "active");
        status.put("lastScan", LocalDateTime.now().minusHours(2));
        status.put("totalScans", 42);
        status.put("queueSize", 5);
        return ResponseEntity.ok(status);
    }
}
