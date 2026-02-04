// File: HealthScoreService.java
package com.stackscout.service;

import com.stackscout.model.Library;

/**
 * Service for calculating the health score of a library based on various
 * metrics.
 */
public interface HealthScoreService {
	/**
	 * Calculates a health score (0-100) for the given library.
	 * 
	 * @param library The library entity to evaluate.
	 * @return The health score.
	 */
	int calculateScore(Library library);
}
