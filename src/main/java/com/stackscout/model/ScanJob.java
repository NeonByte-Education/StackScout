package com.stackscout.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Модель задачи сканирования
 */
@Entity
@Table(name = "scan_jobs", indexes = {
    @Index(name = "idx_scan_job_status", columnList = "status"),
    @Index(name = "idx_scan_job_source", columnList = "source"),
    @Index(name = "idx_scan_job_created", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String source; // pypi, npm, dockerhub
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ScanStatus status;
    
    @Column(name = "packages_count")
    private Integer packagesCount;
    
    @Column(name = "processed_count")
    private Integer processedCount;
    
    @Column(name = "failed_count")
    private Integer failedCount;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Статусы задачи сканирования
     */
    public enum ScanStatus {
        PENDING,      // Ожидает запуска
        RUNNING,      // Выполняется
        COMPLETED,    // Завершено
        FAILED,       // Ошибка
        CANCELLED     // Отменено
    }
}
