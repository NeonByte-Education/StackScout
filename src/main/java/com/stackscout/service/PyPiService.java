// File: PyPiService.java
package com.stackscout.service;

import com.stackscout.model.Library;

/**
 * Сервис для взаимодействия с PyPI JSON API для получения метаданных пакетов.
 */
public interface PyPiService {
	/**
	 * Получает метаданные для указанного Python-пакета.
	 * 
	 * @param packageName Имя пакета.
	 * @return Объект Library с метаданными или null, если пакет не найден.
	 */
	Library collect(String packageName);
}
