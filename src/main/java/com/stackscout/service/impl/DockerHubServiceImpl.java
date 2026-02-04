// File: DockerHubServiceImpl.java
package com.stackscout.service.impl;

import com.stackscout.dto.dockerhub.DockerHubDTOs;
import com.stackscout.model.Library;
import com.stackscout.service.DockerHubService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DockerHubServiceImpl implements DockerHubService {

	private final RestClient restClient;
	private static final Logger logger = LoggerFactory.getLogger(DockerHubServiceImpl.class);

	public DockerHubServiceImpl() {
		this.restClient = RestClient.builder()
				.baseUrl("https://hub.docker.com/v2/repositories")
				.build();
	}

	// Constructor for testing injectability
	public DockerHubServiceImpl(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("https://hub.docker.com/v2/repositories")
				.build();
	}

	@Override
	public Optional<Library> getImageInfo(String imageName) {
		String namespace = "library";
		String repository = imageName;

		if (imageName.contains("/")) {
			String[] parts = imageName.split("/", 2);
			namespace = parts[0];
			repository = parts[1];
		}

		try {
			return Optional.ofNullable(restClient.get()
					.uri("/{namespace}/{repository}", namespace, repository)
					.retrieve()
					.body(DockerHubDTOs.DockerHubRepository.class))
					.map(this::mapToLibrary);
		} catch (Exception e) {
			logger.warn("Failed to fetch Docker Hub image info for: {}/{}", namespace, repository, e);
			return Optional.empty();
		}
	}

	private Library mapToLibrary(DockerHubDTOs.DockerHubRepository repo) {
		Library lib = new Library();
		lib.setName(repo.namespace().equals("library") ? repo.name() : repo.namespace() + "/" + repo.name());
		lib.setVersion("latest"); // Docker Hub API doesn't give a single version, defaulting to latest logic
		lib.setSource("docker");
		lib.setLicense(""); // Docker Hub API doesn't consistently provide license in this endpoint
		lib.setHealthScore(0);
		lib.setDescription(repo.description());
		lib.setRepository("https://hub.docker.com/r/" + repo.namespace() + "/" + repo.name());

		if (repo.lastUpdated() != null) {
			lib.setLastRelease(repo.lastUpdated().toString());
		}

		return lib;
	}
}
