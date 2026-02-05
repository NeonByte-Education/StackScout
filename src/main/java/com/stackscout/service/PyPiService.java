// File: PyPiService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.Optional;

/**
 * Сервис для взаимодействия с PyPI JSON API для получения метаданных пакетов.
 */
public interface PyPiService {
	/**
	 * Получает метаданные для указанного Python-пакета.
	 * 
	 * @param packageName Имя пакета.
	 * @return Опциональный объект Library с метаданными.
	 */
	Optional<Library> getPackageInfo(String packageName);
}
