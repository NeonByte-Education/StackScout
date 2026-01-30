package com.stackscout.dto;

import com.stackscout.model.License;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания новой лицензии
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLicenseRequest {
    
    @NotBlank(message = "Название лицензии обязательно")
    private String name;
    
    @NotNull(message = "Тип лицензии обязателен")
    private License.LicenseType licenseType;
    
    private String description;
    
    private Boolean isOsiApproved;
    
    private Boolean commercialUseAllowed;
    
    private String url;
}
