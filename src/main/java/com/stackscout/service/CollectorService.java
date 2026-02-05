// File: CollectorService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.List;

/**
 * Сервис для сбора и обработки метаданных библиотек из различных источников.
 */
public interface CollectorService {

	/**
	 * Сбор метаданных для одного пакета.
	 * 
	 * @param source Источник (например, "pypi", "dockerhub")
	 * @param name   Имя пакета
	 * @return Обработанная и сохраненная сущность Library
	 */
	Library collect(String source, String name);

	/**
	 * Массовый сбор метаданных для нескольких пакетов.
	 * 
	 * @param source Источник
	 * @param names  Список имен пакетов
	 */
	void collectBulk(String source, List<String> names);
}
