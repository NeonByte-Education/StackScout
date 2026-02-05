// File: LicenseServiceImplTest.java
package com.stackscout.service.impl;

import com.stackscout.repository.LicenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для LicenseServiceImpl.
 * Проверяют логику нормализации названий лицензий.
 */
@ExtendWith(MockitoExtension.class)
class LicenseServiceImplTest {

	@Mock
	private LicenseRepository licenseRepository;

	@InjectMocks
	private LicenseServiceImpl licenseService;

	@Test
	void normalize_ShouldReturnUnknown_WhenNullOrBlank() {
		assertEquals("Unknown", licenseService.normalizeLicense(null));
		assertEquals("Unknown", licenseService.normalizeLicense(""));
		assertEquals("Unknown", licenseService.normalizeLicense("   "));
	}

	@Test
	void normalize_ShouldNormalizeCommonLicenses() {
		assertEquals("MIT", licenseService.normalizeLicense("MIT License"));
		assertEquals("MIT", licenseService.normalizeLicense("The MIT License"));
		assertEquals("Apache-2.0", licenseService.normalizeLicense("Apache License 2.0"));
		assertEquals("Apache-2.0", licenseService.normalizeLicense("Apache 2.0"));
		assertEquals("GPL-3.0", licenseService.normalizeLicense("GPLv3"));
	}

	@Test
	void normalize_ShouldReturnOriginal_WhenNoMatch() {
		assertEquals("Custom License", licenseService.normalizeLicense("Custom License"));
	}

}
