// File: HealthScoreServiceImpl.java
package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.service.HealthScoreService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class HealthScoreServiceImpl implements HealthScoreService {

	@Override
	public int calculateScore(Library library) {
		if (library == null) {
			return 0;
		}

		int score = 0;

		// 1. Recency (0-40 points)
		score += calculateRecencyScore(library.getLastRelease());

		// 2. Repository (0-20 points)
		if (hasValue(library.getRepository())) {
			score += 20;
		}

		// 3. Documentation/Description (0-10 points)
		if (hasValue(library.getDescription())) {
			score += 10;
		}

		// 4. License (0-10 points) - Basic check
		if (hasValue(library.getLicense())) {
			score += 10;
		}

		// Future: Community activity, etc.

		return Math.min(100, Math.max(0, score));
	}

	private int calculateRecencyScore(String lastReleaseDate) {
		if (lastReleaseDate == null || lastReleaseDate.isBlank()) {
			return 0;
		}

		try {
			// Ry to parse flexible format, usually ISO from APIs
			LocalDateTime releaseDate = LocalDateTime.parse(lastReleaseDate, DateTimeFormatter.ISO_DATE_TIME);
			long daysDiff = ChronoUnit.DAYS.between(releaseDate, LocalDateTime.now());

			if (daysDiff < 180) { // < 6 months
				return 40;
			} else if (daysDiff < 365) { // < 1 year
				return 30;
			} else if (daysDiff < 730) { // < 2 years
				return 20;
			} else {
				return 5;
			}
		} catch (Exception e) {
			// Parsing failed, return 0 or try other format
			return 0;
		}
	}

	private boolean hasValue(String value) {
		return value != null && !value.isBlank();
	}
}
