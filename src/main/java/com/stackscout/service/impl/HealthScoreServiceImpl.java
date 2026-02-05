package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.service.HealthScoreService;
import org.springframework.stereotype.Service;

@Service
public class HealthScoreServiceImpl implements HealthScoreService {

    @Override
    public Integer calculateScore(Library library) {
        int score = 50; // Base score

        if (library.getDescription() != null && library.getDescription().length() > 20) {
            score += 10;
        }
        if (library.getLicense() != null && !library.getLicense().equalsIgnoreCase("unknown")) {
            score += 15;
        }
        if (library.getRepository() != null) {
            score += 15;
        }
        if (library.getVersion() != null) {
            score += 10;
        }

        return Math.min(100, score);
    }
}
