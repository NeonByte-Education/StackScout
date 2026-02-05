package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.service.PyPiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PyPiServiceImpl implements PyPiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Library collect(String name) {
        String url = "https://pypi.org/pypi/" + name + "/json";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("info")) {
                return null;
            }

            Map<String, Object> info = (Map<String, Object>) response.get("info");
            
            return Library.builder()
                    .name(name)
                    .source("pypi")
                    .description((String) info.get("summary"))
                    .version((String) info.get("version"))
                    .license((String) info.get("license"))
                    .repository((String) info.get("home_page"))
                    .build();
        } catch (Exception e) {
            log.error("Error fetching data from PyPI for {}: {}", name, e.getMessage());
            return null;
        }
    }
}
