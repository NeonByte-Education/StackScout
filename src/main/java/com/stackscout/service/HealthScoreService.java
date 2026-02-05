package com.stackscout.service;

import com.stackscout.model.Library;

public interface HealthScoreService {
    Integer calculateScore(Library library);
}
