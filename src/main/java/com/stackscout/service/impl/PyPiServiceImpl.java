// File: PyPiServiceImpl.java
package com.stackscout.service.impl;

import com.stackscout.dto.pypi.PyPiDTOs;
import com.stackscout.model.Library;
import com.stackscout.service.PyPiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PyPiServiceImpl implements PyPiService {

	private final RestClient restClient;
	private static final Logger logger = LoggerFactory.getLogger(PyPiServiceImpl.class);

	public PyPiServiceImpl() {
		this.restClient = RestClient.builder()
				.baseUrl("https://pypi.org/pypi")
				.build();
	}

	// Constructor for testing injectability
	public PyPiServiceImpl(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("https://pypi.org/pypi")
				.build();
	}

	@Override
	public Optional<Library> getPackageInfo(String packageName) {
		try {
			return Optional.ofNullable(restClient.get()
					.uri("/{package}/json", packageName)
					.retrieve()
					.body(PyPiDTOs.PyPiResponse.class))
					.map(this::mapToLibrary);
		} catch (Exception e) {
			logger.warn("Failed to fetch PyPI package info for: {}", packageName, e);
			return Optional.empty();
		}
	}

	private Library mapToLibrary(PyPiDTOs.PyPiResponse response) {
		PyPiDTOs.PyPiInfo info = response.info();

		Library lib = new Library();
		lib.setName(info.name());
		lib.setVersion(info.version());
		lib.setSource("pypi");
		lib.setLicense(info.license());
		lib.setHealthScore(0); // Calculator will be implemented later
		lib.setDescription(info.summary());

		// Find repository URL
		String repoUrl = info.homePage();
		if (info.projectUrls() != null) {
			for (Map.Entry<String, String> entry : info.projectUrls().entrySet()) {
				String key = entry.getKey().toLowerCase();
				String value = entry.getValue();
				if (key.contains("source") || key.contains("repository") || key.contains("github")
						|| key.contains("gitlab")) {
					repoUrl = value;
					break;
				}
			}
		}
		lib.setRepository(repoUrl);

		// Find last release date
		if (response.releases() != null && response.releases().containsKey(info.version())) {
			var releases = response.releases().get(info.version());
			if (releases != null && !releases.isEmpty()) {
				String uploadTime = releases.get(0).uploadTime();
				lib.setLastRelease(uploadTime);
			}
		}

		return lib;
	}
}
