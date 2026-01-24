package com.stackscout.controller;

import com.stackscout.dto.ErrorResponse;
import com.stackscout.model.Library;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/libraries")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final List<Library> libraries = new ArrayList<>();
    private Long nextId = 1L;

    public LibraryController() {
        libraries.add(new Library(
            nextId++, "requests", "2.31.0", "pypi", 
            "Apache-2.0", 95, "2023-05-22",
            "https://github.com/psf/requests",
            "HTTP библиотека для Python"
        ));
        
        libraries.add(new Library(
            nextId++, "django", "5.0.0", "pypi",
            "BSD-3-Clause", 98, "2023-12-04",
            "https://github.com/django/django",
            "Веб-фреймворк для Python"
        ));
        
        libraries.add(new Library(
            nextId++, "react", "18.2.0", "npm",
            "MIT", 99, "2023-06-14",
            "https://github.com/facebook/react",
            "JavaScript библиотека для UI"
        ));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int start = Math.min(page * size, libraries.size());
        int end = Math.min(start + size, libraries.size());
        List<Library> paginatedLibraries = libraries.subList(start, end);
        
        Map<String, Object> response = new HashMap<>();
        response.put("libraries", paginatedLibraries);
        response.put("totalElements", libraries.size());
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", (int) Math.ceil((double) libraries.size() / size));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable Long id) {
        return libraries.stream()
            .filter(lib -> lib.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Library>> searchLibraries(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) String source
    ) {
        List<Library> result = libraries.stream()
            .filter(lib -> {
                boolean matchQuery = query == null ||
                    lib.getName().toLowerCase().contains(query.toLowerCase());
                boolean matchSource = source == null ||
                    lib.getSource().equalsIgnoreCase(source);
                return matchQuery && matchSource;
            })
            .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createLibrary(@RequestBody Library library) {
        try {
            if (library.getName() == null || library.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Имя библиотеки обязательно", LocalDateTime.now()));
            }
            
            library.setId(nextId++);
            libraries.add(library);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Библиотека успешно создана");
            response.put("library", library);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Ошибка при создании библиотеки: " + e.getMessage(), LocalDateTime.now()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibrary(@PathVariable Long id, @RequestBody Library updatedLibrary) {
        return libraries.stream()
            .filter(lib -> lib.getId().equals(id))
            .findFirst()
            .<ResponseEntity<?>>map(library -> {
                if (updatedLibrary.getName() != null) library.setName(updatedLibrary.getName());
                if (updatedLibrary.getVersion() != null) library.setVersion(updatedLibrary.getVersion());
                if (updatedLibrary.getSource() != null) library.setSource(updatedLibrary.getSource());
                if (updatedLibrary.getLicense() != null) library.setLicense(updatedLibrary.getLicense());
                if (updatedLibrary.getHealthScore() != null) library.setHealthScore(updatedLibrary.getHealthScore());
                if (updatedLibrary.getLastRelease() != null) library.setLastRelease(updatedLibrary.getLastRelease());
                if (updatedLibrary.getRepository() != null) library.setRepository(updatedLibrary.getRepository());
                if (updatedLibrary.getDescription() != null) library.setDescription(updatedLibrary.getDescription());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Библиотека успешно обновлена");
                response.put("library", library);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Библиотека с ID " + id + " не найдена", LocalDateTime.now())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLibrary(@PathVariable Long id) {
        boolean removed = libraries.removeIf(lib -> lib.getId().equals(id));
        if (removed) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Библиотека успешно удалена");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Библиотека с ID " + id + " не найдена", LocalDateTime.now()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLibraries", libraries.size());
        stats.put("sources", Map.of(
            "pypi", libraries.stream().filter(l -> "pypi".equals(l.getSource())).count(),
            "npm", libraries.stream().filter(l -> "npm".equals(l.getSource())).count(),
            "dockerhub", libraries.stream().filter(l -> "dockerhub".equals(l.getSource())).count()
        ));
        stats.put("averageHealthScore", 
            libraries.stream()
                .mapToInt(l -> l.getHealthScore() != null ? l.getHealthScore() : 0)
                .average()
                .orElse(0.0)
        );
        return ResponseEntity.ok(stats);
    }
}