// File: LicenseDto.java
package com.stackscout.dto;

import com.stackscout.model.License;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Объект передачи данных (DTO) для представления информации о лицензии в
 * ответах API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDto {

    private Long id;
    private String name;
    private License.LicenseType licenseType;
    private String description;
    private Boolean isOsiApproved;
    private Boolean commercialUseAllowed;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
