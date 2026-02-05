// File: DockerHubService.java
package com.stackscout.service;

import com.stackscout.model.Library;

/**
 * Сервис для взаимодействия с Docker Hub V2 API для получения метаданных образов.
 */
public interface DockerHubService {
	/**
	 * Получает метаданные для указанного Docker-образа.
	 * 
	 * @param imageName Имя образа (например, "nginx" или "mysql/mysql-server").
	 * @return Объект Library с метаданными или null, если образ не найден.
	 */
	Library collect(String imageName);
}
