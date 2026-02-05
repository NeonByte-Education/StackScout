// File: ScanJobRepository.java
package com.stackscout.repository;

import com.stackscout.model.ScanJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для управления задачами сканирования в базе данных.
 */
@Repository
public interface ScanJobRepository extends JpaRepository<ScanJob, Long> {

    /**
     * Найти задачи по статусу
     */
    List<ScanJob> findByStatus(ScanJob.ScanStatus status);

    /**
     * Найти задачи по статусу с пагинацией
     */
    Page<ScanJob> findByStatus(ScanJob.ScanStatus status, Pageable pageable);

    /**
     * Найти задачи по источнику
     */
    List<ScanJob> findBySource(String source);

    /**
     * Найти задачи по источнику с пагинацией
     */
    Page<ScanJob> findBySource(String source, Pageable pageable);

    /**
     * Найти задачи, созданные после определенной даты
     */
    List<ScanJob> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Получить последние задачи по источнику
     */
    @Query("SELECT s FROM ScanJob s WHERE s.source = :source ORDER BY s.createdAt DESC")
    List<ScanJob> findRecentJobsBySource(String source, Pageable pageable);

    /**
     * Получить статистику по статусам
     */
    @Query("SELECT s.status, COUNT(s) FROM ScanJob s GROUP BY s.status")
    List<Object[]> getStatusStatistics();
}
