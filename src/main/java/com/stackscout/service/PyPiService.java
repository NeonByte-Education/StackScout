// File: PyPiService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.Optional;

/**
 * Service for interacting with the PyPI JSON API to fetch package metadata.
 */
public interface PyPiService {
	/**
	 * Fetches metadata for a given Python package.
	 * 
	 * @param packageName The name of the package.
	 * @return Optional containing Library entity with metadata.
	 */
	Optional<Library> getPackageInfo(String packageName);
}
