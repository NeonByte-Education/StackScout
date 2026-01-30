package com.stackscout.controller;

import com.stackscout.dto.CreateLicenseRequest;
import com.stackscout.dto.LicenseDto;
import com.stackscout.model.License;
import com.stackscout.service.LicenseService;
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
 * REST контроллер для управления лицензиями
 */
@RestController
@RequestMapping("/api/v1/licenses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Licenses", description = "API для управления лицензиями")
public class LicenseController {

    private final LicenseService licenseService;

    @Operation(summary = "Получить все лицензии", description = "Возвращает список всех лицензий с пагинацией")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLicenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        String direction = sortDirection != null ? sortDirection : "ASC";
        Sort.Direction sortDirEnum = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirEnum, sortBy));
        
        Page<LicenseDto> licensesPage = licenseService.getAllLicenses(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("licenses", licensesPage.getContent());
        response.put("totalElements", licensesPage.getTotalElements());
        response.put("currentPage", licensesPage.getNumber());
        response.put("pageSize", licensesPage.getSize());
        response.put("totalPages", licensesPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить лицензию по ID", description = "Возвращает детали конкретной лицензии")
    @GetMapping("/{id}")
    public ResponseEntity<LicenseDto> getLicenseById(@PathVariable Long id) {
        LicenseDto license = licenseService.getLicenseById(id);
        return ResponseEntity.ok(license);
    }

    @Operation(summary = "Создать новую лицензию", description = "Добавляет новую лицензию в систему")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createLicense(@Valid @RequestBody CreateLicenseRequest request) {
        LicenseDto createdLicense = licenseService.createLicense(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Лицензия успешно создана");
        response.put("license", createdLicense);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Обновить лицензию", description = "Обновляет существующую лицензию")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLicense(
            @PathVariable Long id, 
            @Valid @RequestBody CreateLicenseRequest request) {
        
        LicenseDto updatedLicense = licenseService.updateLicense(id, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Лицензия успешно обновлена");
        response.put("license", updatedLicense);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удалить лицензию", description = "Удаляет лицензию из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLicense(@PathVariable Long id) {
        licenseService.deleteLicense(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Лицензия успешно удалена");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Найти лицензию по имени", description = "Возвращает лицензию с указанным именем")
    @GetMapping("/search")
    public ResponseEntity<LicenseDto> findByName(@RequestParam String name) {
        LicenseDto license = licenseService.findByName(name);
        return ResponseEntity.ok(license);
    }

    @Operation(summary = "Получить лицензии по типу", description = "Возвращает все лицензии указанного типа")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LicenseDto>> getLicensesByType(@PathVariable License.LicenseType type) {
        List<LicenseDto> licenses = licenseService.getLicensesByType(type);
        return ResponseEntity.ok(licenses);
    }

    @Operation(summary = "Получить OSI-одобренные лицензии", description = "Возвращает все OSI-одобренные лицензии")
    @GetMapping("/osi-approved")
    public ResponseEntity<List<LicenseDto>> getOsiApprovedLicenses() {
        List<LicenseDto> licenses = licenseService.getOsiApprovedLicenses();
        return ResponseEntity.ok(licenses);
    }
}
