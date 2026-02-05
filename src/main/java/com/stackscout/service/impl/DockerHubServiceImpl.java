package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.service.DockerHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerHubServiceImpl implements DockerHubService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Library collect(String name) {
        String url = "https://hub.docker.com/v2/repositories/" + name;
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                return null;
            }

            return Library.builder()
                    .name(name)
                    .source("dockerhub")
                    .description((String) response.get("description"))
                    .version("latest")
                    .repository("https://hub.docker.com/r/" + name)
                    .build();
        } catch (Exception e) {
            log.error("Error fetching data from Docker Hub for {}: {}", name, e.getMessage());
            return null;
        }
    }
}
