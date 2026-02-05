package com.stackscout.service.impl;

import com.stackscout.dto.CreateLicenseRequest;
import com.stackscout.dto.LicenseDto;
import com.stackscout.exception.ResourceNotFoundException;
import com.stackscout.model.License;
import com.stackscout.repository.LicenseRepository;
import com.stackscout.service.LicenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с лицензиями
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LicenseServiceImpl implements LicenseService {
    
    private final LicenseRepository licenseRepository;
    
    @Override
    public Page<LicenseDto> getAllLicenses(Pageable pageable) {
        log.debug("Получение всех лицензий с пагинацией: {}", pageable);
        return licenseRepository.findAll(pageable)
                .map(this::toDto);
    }
    
    @Override
    public LicenseDto getLicenseById(Long id) {
        log.debug("Поиск лицензии с ID: {}", id);
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лицензия не найдена с ID: " + id));
        return toDto(license);
    }
    
    @Override
    @Transactional
    public LicenseDto createLicense(CreateLicenseRequest request) {
        log.info("Создание новой лицензии: {}", request.getName());
        
        License license = toEntity(request);
        License savedLicense = licenseRepository.save(license);
        
        log.info("Лицензия успешно создана с ID: {}", savedLicense.getId());
        return toDto(savedLicense);
    }
    
    @Override
    @Transactional
    public LicenseDto updateLicense(Long id, CreateLicenseRequest request) {
        log.info("Обновление лицензии с ID: {}", id);
        
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лицензия не найдена с ID: " + id));
        
        updateEntityFromDto(license, request);
        License updatedLicense = licenseRepository.save(license);
        
        log.info("Лицензия успешно обновлена: {}", id);
        return toDto(updatedLicense);
    }
    
    @Override
    @Transactional
    public void deleteLicense(Long id) {
        log.info("Удаление лицензии с ID: {}", id);
        
        if (!licenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Лицензия не найдена с ID: " + id);
        }
        
        licenseRepository.deleteById(id);
        log.info("Лицензия успешно удалена: {}", id);
    }
    
    @Override
    public LicenseDto findByName(String name) {
        log.debug("Поиск лицензии по имени: {}", name);
        License license = licenseRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Лицензия не найдена с именем: " + name));
        return toDto(license);
    }
    
    @Override
    public List<LicenseDto> getLicensesByType(License.LicenseType type) {
        log.debug("Получение лицензий по типу: {}", type);
        return licenseRepository.findByLicenseType(type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<LicenseDto> getOsiApprovedLicenses() {
        log.debug("Получение OSI-одобренных лицензий");
        return licenseRepository.findByIsOsiApproved(true)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    // Вспомогательные методы для маппинга
    
    private LicenseDto toDto(License license) {
        return new LicenseDto(
            license.getId(),
            license.getName(),
            license.getLicenseType(),
            license.getDescription(),
            license.getIsOsiApproved(),
            license.getCommercialUseAllowed(),
            license.getUrl(),
            license.getCreatedAt(),
            license.getUpdatedAt()
        );
    }
    
    private License toEntity(CreateLicenseRequest request) {
        License license = new License();
        license.setName(request.getName());
        license.setLicenseType(request.getLicenseType());
        license.setDescription(request.getDescription());
        license.setIsOsiApproved(request.getIsOsiApproved());
        license.setCommercialUseAllowed(request.getCommercialUseAllowed());
        license.setUrl(request.getUrl());
        return license;
    }
    
    private void updateEntityFromDto(License license, CreateLicenseRequest request) {
        if (request.getName() != null) {
            license.setName(request.getName());
        }
        if (request.getLicenseType() != null) {
            license.setLicenseType(request.getLicenseType());
        }
        if (request.getDescription() != null) {
            license.setDescription(request.getDescription());
        }
        if (request.getIsOsiApproved() != null) {
            license.setIsOsiApproved(request.getIsOsiApproved());
        }
        if (request.getCommercialUseAllowed() != null) {
            license.setCommercialUseAllowed(request.getCommercialUseAllowed());
        }
        if (request.getUrl() != null) {
            license.setUrl(request.getUrl());
        }
    }

    @Override
    public String normalizeLicense(String license) {
        if (license == null) return "Unknown";
        String normalized = license.trim().toUpperCase();
        if (normalized.contains("MIT")) return "MIT";
        if (normalized.contains("APACHE")) return "Apache-2.0";
        if (normalized.contains("GPL")) return "GPL";
        if (normalized.contains("BSD")) return "BSD";
        return license;
    }
}

