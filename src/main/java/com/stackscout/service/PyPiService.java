package com.stackscout.service;

import com.stackscout.model.Library;

public interface PyPiService {
    Library collect(String name);
}
