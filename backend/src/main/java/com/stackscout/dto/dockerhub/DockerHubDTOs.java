// File: DockerHubDTOs.java
package com.stackscout.dto.dockerhub;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Объекты передачи данных для взаимодействия с Docker Hub API.
 */
public class DockerHubDTOs {

	public record DockerHubRepository(
			String user,
			String name,
			String namespace,
			String description,
			@JsonProperty("star_count") int starCount,
			@JsonProperty("pull_count") long pullCount,
			@JsonProperty("last_updated") LocalDateTime lastUpdated,
			@JsonProperty("hub_user") String hubUser) {
	}
}
