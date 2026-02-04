// File: LicenseRepository.java
package com.stackscout.repository;

import com.stackscout.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с лицензиями
 */
@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    
    /**
     * Найти лицензию по имени
     */
    Optional<License> findByName(String name);
    
    /**
     * Найти лицензии по типу
     */
    List<License> findByLicenseType(License.LicenseType licenseType);
    
    /**
     * Найти OSI-одобренные лицензии
     */
    List<License> findByIsOsiApproved(Boolean isOsiApproved);
    
    /**
     * Найти лицензии, разрешающие коммерческое использование
     */
    List<License> findByCommercialUseAllowed(Boolean commercialUseAllowed);
    
    /**
     * Проверить существование лицензии по имени
     */
    boolean existsByName(String name);
}
