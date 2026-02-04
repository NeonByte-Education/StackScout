// File: CollectorService.java
package com.stackscout.service;

import com.stackscout.model.Library;
import java.util.List;

/**
 * Service for collecting and processing library metadata from various sources.
 */
public interface CollectorService {

	/**
	 * Collect metadata for a single package.
	 * 
	 * @param source The source (e.g., "pypi", "dockerhub")
	 * @param name   The package name
	 * @return The processed and saved Library entity
	 */
	Library collect(String source, String name);

	/**
	 * Collect metadata for multiple packages.
	 * 
	 * @param source The source
	 * @param names  List of package names
	 */
	void collectBulk(String source, List<String> names);
}
