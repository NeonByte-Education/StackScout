package com.stackscout.controller;

import com.stackscout.dto.CreateLibraryRequest;
import com.stackscout.dto.LibraryDto;
import com.stackscout.dto.UpdateLibraryRequest;
import com.stackscout.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для управления библиотеками
 */
@RestController
@RequestMapping("/api/v1/libraries")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Libraries", description = "API для управления библиотеками")
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(summary = "Получить все библиотеки", description = "Возвращает список всех библиотек с пагинацией")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        String direction = sortDirection != null ? sortDirection : "ASC";
        Sort.Direction sortDirEnum = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirEnum, sortBy));
        
        Page<LibraryDto> librariesPage = libraryService.getAllLibraries(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("libraries", librariesPage.getContent());
        response.put("totalElements", librariesPage.getTotalElements());
        response.put("currentPage", librariesPage.getNumber());
        response.put("pageSize", librariesPage.getSize());
        response.put("totalPages", librariesPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить библиотеку по ID", description = "Возвращает детали конкретной библиотеки")
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable Long id) {
        LibraryDto library = libraryService.getLibraryById(id);
        return ResponseEntity.ok(library);
    }

    @Operation(summary = "Поиск библиотек", description = "Поиск библиотек по названию и/или источнику")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchLibraries(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) String source,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LibraryDto> result;
        
        if (query != null && !query.trim().isEmpty() && source != null && !source.trim().isEmpty()) {
            result = libraryService.searchLibrariesBySource(query, source, pageable);
        } else if (query != null && !query.trim().isEmpty()) {
            result = libraryService.searchLibraries(query, pageable);
        } else if (source != null && !source.trim().isEmpty()) {
            result = libraryService.getLibrariesBySource(source, pageable);
        } else {
            result = libraryService.getAllLibraries(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("libraries", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("currentPage", result.getNumber());
        response.put("totalPages", result.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Создать новую библиотеку", description = "Добавляет новую библиотеку в систему")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createLibrary(@Valid @RequestBody CreateLibraryRequest request) {
        LibraryDto createdLibrary = libraryService.createLibrary(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Библиотека успешно создана");
        response.put("library", createdLibrary);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Обновить библиотеку", description = "Обновляет существующую библиотеку")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLibrary(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateLibraryRequest request) {
        
        LibraryDto updatedLibrary = libraryService.updateLibrary(id, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Библиотека успешно обновлена");
        response.put("library", updatedLibrary);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удалить библиотеку", description = "Удаляет библиотеку из системы")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Библиотека успешно удалена");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить здоровые библиотеки", description = "Возвращает библиотеки с оценкой здоровья выше заданного порога")
    @GetMapping("/healthy")
    public ResponseEntity<List<LibraryDto>> getHealthyLibraries(
            @RequestParam(defaultValue = "80") Integer minScore) {
        List<LibraryDto> healthyLibraries = libraryService.getHealthyLibraries(minScore);
        return ResponseEntity.ok(healthyLibraries);
    }

    @Operation(summary = "Статистика библиотек", description = "Возвращает общую статистику по библиотекам")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Page<LibraryDto> allLibraries = libraryService.getAllLibraries(Pageable.unpaged());
        List<LibraryDto> libraries = allLibraries.getContent();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLibraries", libraries.size());
        stats.put("sources", Map.of(
            "pypi", libraries.stream().filter(l -> "pypi".equals(l.getSource())).count(),
            "npm", libraries.stream().filter(l -> "npm".equals(l.getSource())).count(),
            "dockerhub", libraries.stream().filter(l -> "dockerhub".equals(l.getSource())).count()
        ));
        stats.put("averageHealthScore", 
            libraries.stream()
                .filter(l -> l.getHealthScore() != null)
                .mapToInt(LibraryDto::getHealthScore)
                .average()
                .orElse(0.0)
        );
        
        return ResponseEntity.ok(stats);
    }
}