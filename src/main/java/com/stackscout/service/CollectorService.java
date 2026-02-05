package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.List;

public interface CollectorService {
    Library collect(String source, String name);
    void collectBulk(String source, List<String> names);
}
