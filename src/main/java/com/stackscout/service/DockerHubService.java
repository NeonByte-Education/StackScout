package com.stackscout.service;

import com.stackscout.model.Library;

public interface DockerHubService {
    Library collect(String name);
}
