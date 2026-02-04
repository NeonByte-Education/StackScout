// File: License.java
package com.stackscout.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Модель лицензии
 */
@Entity
@Table(name = "licenses", indexes = {
    @Index(name = "idx_license_name", columnList = "name", unique = true),
    @Index(name = "idx_license_type", columnList = "license_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "license_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_osi_approved")
    private Boolean isOsiApproved;
    
    @Column(name = "commercial_use_allowed")
    private Boolean commercialUseAllowed;
    
    @Column(length = 500)
    private String url;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Типы лицензий
     */
    public enum LicenseType {
        PERMISSIVE,  // MIT, Apache, BSD
        COPYLEFT,    // GPL, AGPL, MPL
        PROPRIETARY, // Проприетарные
        PUBLIC_DOMAIN, // Public Domain
        OTHER        // Другие
    }
}
