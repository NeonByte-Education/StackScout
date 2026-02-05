// File: CollectorScheduler.java
package com.stackscout.scheduler;

import com.stackscout.model.Library;
import com.stackscout.repository.LibraryRepository;
import com.stackscout.service.CollectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorScheduler {

    private final LibraryRepository libraryRepository;
    private final CollectorService collectorService;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshPyPiLibraries() {
        log.info("Starting scheduled refresh of PyPI libraries");
        List<String> pypiLibraries = libraryRepository.findBySource("pypi")
                .stream()
                .map(Library::getName)
                .toList();
        
        if (!pypiLibraries.isEmpty()) {
            collectorService.collectBulk("pypi", pypiLibraries);
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void refreshDockerHubLibraries() {
        log.info("Starting scheduled refresh of Docker Hub libraries");
        List<String> dockerLibraries = libraryRepository.findBySource("dockerhub")
                .stream()
                .map(Library::getName)
                .toList();
        
        if (!dockerLibraries.isEmpty()) {
            collectorService.collectBulk("dockerhub", dockerLibraries);
        }
    }
}
