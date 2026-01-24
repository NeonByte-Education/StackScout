package com.stackscout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Library {
    
    private Long id;
    private String name;
    private String version;
    private String source; // pypi, npm, dockerhub
    private String license;
    private Integer healthScore;
    private String lastRelease;
    private String repository;
    private String description;
    
}
