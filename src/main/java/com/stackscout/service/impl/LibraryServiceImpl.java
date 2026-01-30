package com.stackscout.service.impl;

import com.stackscout.dto.CreateLibraryRequest;
import com.stackscout.dto.LibraryDto;
import com.stackscout.dto.UpdateLibraryRequest;
import com.stackscout.exception.ResourceNotFoundException;
import com.stackscout.mapper.LibraryMapper;
import com.stackscout.model.Library;
import com.stackscout.repository.LibraryRepository;
import com.stackscout.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с библиотеками
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryServiceImpl implements LibraryService {
    
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;
    
    @Override
    public Page<LibraryDto> getAllLibraries(Pageable pageable) {
        log.debug("Получение всех библиотек с пагинацией: {}", pageable);
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable не может быть null");
        }
        return libraryRepository.findAll(pageable)
                .map(libraryMapper::toDto);
    }
    
    @Override
    public LibraryDto getLibraryById(Long id) {
        log.debug("Поиск библиотеки с ID: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Библиотека не найдена с ID: " + id));
        return libraryMapper.toDto(library);
    }
    
    @Override
    @Transactional
    public LibraryDto createLibrary(CreateLibraryRequest request) {
        log.info("Создание новой библиотеки: {}", request.getName());
        
        Library library = libraryMapper.toEntity(request);
        if (library == null) {
            throw new IllegalArgumentException("Library не может быть null");
        }
        Library savedLibrary = libraryRepository.save(library);
        
        log.info("Библиотека успешно создана с ID: {}", savedLibrary.getId());
        return libraryMapper.toDto(savedLibrary);
    }
    
    @Override
    @Transactional
    public LibraryDto updateLibrary(Long id, UpdateLibraryRequest request) {
        log.info("Обновление библиотеки с ID: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Библиотека не найдена с ID: " + id));
        
        libraryMapper.updateEntityFromDto(library, request);
        @SuppressWarnings("null")
        Library updatedLibrary = libraryRepository.save(library);
        
        log.info("Библиотека успешно обновлена: {}", id);
        return libraryMapper.toDto(updatedLibrary);
    }
    
    @Override
    @Transactional
    public void deleteLibrary(Long id) {
        log.info("Удаление библиотеки с ID: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        
        if (!libraryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Библиотека не найдена с ID: " + id);
        }
        
        libraryRepository.deleteById(id);
        log.info("Библиотека успешно удалена: {}", id);
    }
    
    @Override
    public Page<LibraryDto> searchLibraries(String query, Pageable pageable) {
        log.debug("Поиск библиотек по запросу: {}", query);
        return libraryRepository.searchByName(query, pageable)
                .map(libraryMapper::toDto);
    }
    
    @Override
    public Page<LibraryDto> searchLibrariesBySource(String query, String source, Pageable pageable) {
        log.debug("Поиск библиотек по запросу: {} и источнику: {}", query, source);
        return libraryRepository.searchByNameAndSource(query, source, pageable)
                .map(libraryMapper::toDto);
    }
    
    @Override
    public Page<LibraryDto> getLibrariesBySource(String source, Pageable pageable) {
        log.debug("Получение библиотек по источнику: {}", source);
        return libraryRepository.findBySource(source, pageable)
                .map(libraryMapper::toDto);
    }
    
    @Override
    public List<LibraryDto> getHealthyLibraries(Integer minScore) {
        log.debug("Получение библиотек с минимальной оценкой: {}", minScore);
        return libraryRepository.findByHealthScoreGreaterThanEqual(minScore)
                .stream()
                .map(libraryMapper::toDto)
                .collect(Collectors.toList());
    }
}
