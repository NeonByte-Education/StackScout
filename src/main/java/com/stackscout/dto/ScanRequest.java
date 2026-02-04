// File: ScanRequest.java
package com.stackscout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanRequest {
    private String source; // pypi, npm, dockerhub
    private List<String> packages;
}
