// File: CollectorServiceImpl.java
package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.repository.LibraryRepository;
import com.stackscout.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис-оркестратор, координирующий сбор данных из различных источников.
 * Выполняет нормализацию данных, расчет рейтинга и сохранение результатов в
 * базу данных.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

	private final PyPiService pyPiService;
	private final DockerHubService dockerHubService;
	private final LicenseService licenseService;
	private final HealthScoreService healthScoreService;
	private final LibraryRepository libraryRepository;

	@Override
	@Transactional
	public Library collect(String source, String name) {
		log.info("Collecting metadata for {} from {}", name, source);

		Optional<Library> optionalLibrary = switch (source.toLowerCase()) {
			case "pypi" -> pyPiService.getPackageInfo(name);
			case "docker", "dockerhub" -> dockerHubService.getImageInfo(name);
			default -> {
				log.warn("Unsupported source: {}", source);
				yield Optional.empty();
			}
		};

		if (optionalLibrary.isEmpty()) {
			log.warn("Could not find metadata for {} in {}", name, source);
			return null;
		}

		Library library = optionalLibrary.get();

		// 1. Normalize License
		if (library.getLicense() != null) {
			String normalizedLicense = licenseService.normalize(library.getLicense());
			library.setLicense(normalizedLicense);
		}

		// 2. Calculate Health Score
		int score = healthScoreService.calculateScore(library);
		library.setHealthScore(score);

		// 3. Persist (Update if exists, or Save new)
		return saveOrUpdate(library);
	}

	@Override
	@Transactional
	public void collectBulk(String source, List<String> names) {
		log.info("Starting bulk collection for {} packages from {}", names.size(), source);
		for (String name : names) {
			try {
				collect(source, name);
			} catch (Exception e) {
				log.error("Failed to collect {} from {}: {}", name, source, e.getMessage());
			}
		}
	}

	private Library saveOrUpdate(Library library) {
		Optional<Library> existing = libraryRepository.findByName(library.getName());

		if (existing.isPresent()) {
			Library entity = existing.get();
			entity.setVersion(library.getVersion());
			entity.setSource(library.getSource());
			entity.setLicense(library.getLicense());
			entity.setHealthScore(library.getHealthScore());
			entity.setLastRelease(library.getLastRelease());
			entity.setRepository(library.getRepository());
			entity.setDescription(library.getDescription());
			entity.setUpdatedAt(LocalDateTime.now());
			return libraryRepository.save(entity);
		} else {
			return libraryRepository.save(library);
		}
	}
}
