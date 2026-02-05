// File: LicenseService.java
package com.stackscout.service;

import com.stackscout.dto.CreateLicenseRequest;
import com.stackscout.dto.LicenseDto;
import com.stackscout.model.License;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Сервис для работы с лицензиями
 */
public interface LicenseService {

    /**
     * Получить все лицензии с пагинацией
     */
    Page<LicenseDto> getAllLicenses(Pageable pageable);

    /**
     * Получить лицензию по ID
     */
    LicenseDto getLicenseById(Long id);

    /**
     * Создать новую лицензию
     */
    LicenseDto createLicense(CreateLicenseRequest request);

    /**
     * Обновить существующую лицензию
     */
    LicenseDto updateLicense(Long id, CreateLicenseRequest request);

    /**
     * Удалить лицензию
     */
    void deleteLicense(Long id);

    /**
     * Найти лицензию по имени
     */
    LicenseDto findByName(String name);

    /**
     * Получить лицензии по типу
     */
    List<LicenseDto> getLicensesByType(License.LicenseType type);

    /**
     * Получить OSI-одобренные лицензии
     */
    List<LicenseDto> getOsiApprovedLicenses();

    /**
     * Нормализовать название лицензии
     */
    String normalizeLicense(String license);
}
