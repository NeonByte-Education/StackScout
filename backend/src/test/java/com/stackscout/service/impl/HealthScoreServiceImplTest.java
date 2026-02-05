// File: HealthScoreServiceImplTest.java
package com.stackscout.service.impl;

import com.stackscout.model.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для HealthScoreServiceImpl.
 * Проверяют алгоритм расчета рейтинга здоровья библиотеки.
 */
class HealthScoreServiceImplTest {

	private HealthScoreServiceImpl healthScoreService;

	@BeforeEach
	void setUp() {
		healthScoreService = new HealthScoreServiceImpl();
	}

	@Test
	void calculateScore_ShouldReturnMaxScore_ForPerfectLibrary() {
		Library lib = new Library();
		lib.setLastRelease(LocalDateTime.now().minusDays(10).format(DateTimeFormatter.ISO_DATE_TIME));
		lib.setRepository("https://github.com/test/repo");
		lib.setDescription("A great library");
		lib.setLicense("MIT");

		// 40 + 20 + 10 + 10 = 80
		assertEquals(80, healthScoreService.calculateScore(lib));
	}

	@Test
	void calculateScore_ShouldReturnScore_ForOlderLibrary() {
		Library lib = new Library();
		lib.setLastRelease(LocalDateTime.now().minusDays(400).format(DateTimeFormatter.ISO_DATE_TIME)); // > 1 year
		lib.setRepository("https://github.com/test/repo");
		lib.setDescription("A great library");
		lib.setLicense("MIT");

		// 20 (Recency) + 20 + 10 + 10 = 60
		assertEquals(60, healthScoreService.calculateScore(lib));
	}

	@Test
	void calculateScore_ShouldReturnZero_ForEmptyLibrary() {
		Library lib = new Library();
		assertEquals(0, healthScoreService.calculateScore(lib));
	}
}
