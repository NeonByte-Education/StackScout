// File: PyPiDTOs.java
package com.stackscout.dto.pypi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Объекты передачи данных для взаимодействия с PyPI API.
 */
public class PyPiDTOs {

	public record PyPiResponse(
			PyPiInfo info,
			Map<String, List<PyPiRelease>> releases) {
	}

	public record PyPiInfo(
			String name,
			String version,
			String summary,
			String license,
			@JsonProperty("home_page") String homePage,
			@JsonProperty("project_urls") Map<String, String> projectUrls) {
	}

	public record PyPiRelease(
			@JsonProperty("upload_time") String uploadTime) {
	}
}
