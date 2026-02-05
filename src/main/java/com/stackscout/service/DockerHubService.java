// File: DockerHubService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.Optional;

/**
 * Сервис для взаимодействия с Docker Hub V2 API для получения метаданных
 * образов.
 */
public interface DockerHubService {
	/**
	 * Получает метаданные для указанного Docker-образа.
	 * 
	 * @param imageName Имя образа (например, "nginx" или "mysql/mysql-server").
	 * @return Опциональный объект Library с метаданными.
	 */
	Optional<Library> getImageInfo(String imageName);
}
