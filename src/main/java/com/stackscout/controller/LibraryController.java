package com.stackscout.controller;

import com.stackscout.model.Library;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/libraries")
public class LibraryController {

    // Временное хранилище (вместо БД для простоты)
    private final List<Library> libraries = new ArrayList<>();
    private Long nextId = 1L;

    public LibraryController() {
        // Добавим несколько примеров библиотек
        libraries.add(new Library(
            nextId++,
            "requests",
            "2.31.0",
            "pypi",
            "Apache-2.0",
            95,
            "2023-05-22",
            "https://github.com/psf/requests",
            "HTTP библиотека для Python"
        ));
        
        libraries.add(new Library(
            nextId++,
            "django",
            "5.0.0",
            "pypi",
            "BSD-3-Clause",
            98,
            "2023-12-04",
            "https://github.com/django/django",
            "Веб-фреймворк для Python"
        ));
        
        libraries.add(new Library(
            nextId++,
            "react",
            "18.2.0",
            "npm",
            "MIT",
            99,
            "2023-06-14",
            "https://github.com/facebook/react",
            "JavaScript библиотека для UI"
        ));
    }

    @GetMapping
    public ResponseEntity<List<Library>> getAllLibraries() {
        return ResponseEntity.ok(libraries);
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
    public ResponseEntity<Library> createLibrary(@RequestBody Library library) {
        library.setId(nextId++);
        libraries.add(library);
        return ResponseEntity.ok(library);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        boolean removed = libraries.removeIf(lib -> lib.getId().equals(id));
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("StackScout API работает! Всего библиотек: " + libraries.size());
    }

}
