// File: HealthScoreService.java
package com.stackscout.service;

import com.stackscout.model.Library;

/**
 * Сервис для расчета рейтинга здоровья библиотеки на основе различных метрик.
 */
public interface HealthScoreService {
	/**
	 * Рассчитывает рейтинг здоровья (0-100) для указанной библиотеки.
	 * 
	 * @param library Сущность библиотеки для оценки.
	 * @return Рейтинг здоровья от 0 до 100.
	 */
	int calculateScore(Library library);
}
