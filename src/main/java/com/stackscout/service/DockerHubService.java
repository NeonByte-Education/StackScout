// File: DockerHubService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.Optional;

/**
 * Service for interacting with the Docker Hub V2 API to fetch image metadata.
 */
public interface DockerHubService {
	/**
	 * Fetches metadata for a given Docker image.
	 * 
	 * @param imageName The name of the image (e.g., "nginx" or
	 *                  "mysql/mysql-server").
	 * @return Optional containing Library entity with metadata.
	 */
	Optional<Library> getImageInfo(String imageName);
}
