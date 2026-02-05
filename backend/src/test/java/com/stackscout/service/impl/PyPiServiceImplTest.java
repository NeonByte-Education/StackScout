// File: PyPiServiceImplTest.java
package com.stackscout.service.impl;

import com.stackscout.model.Library;
import com.stackscout.service.PyPiService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;

/**
 * Тесты для PyPiServiceImpl.
 * Проверяют корректность получения информации о пакетах из PyPI API.
 */
class PyPiServiceImplTest {

	private PyPiService pyPiService;
	private MockRestServiceServer mockServer;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		mockServer = MockRestServiceServer.bindTo(builder).build();
		pyPiService = new PyPiServiceImpl(builder);
	}

	@Test
	void getPackageInfo_ShouldReturnLibrary_WhenPackageExists() {
		// Arrange
		String jsonResponse = """
				{
				    "info": {
				        "name": "requests",
				        "version": "2.31.0",
				        "summary": "Python HTTP for Humans.",
				        "license": "Apache 2.0",
				        "home_page": "https://requests.readthedocs.io",
				        "project_urls": {
				            "Source": "https://github.com/psf/requests"
				        }
				    },
				    "releases": {
				        "2.31.0": [
				            { "upload_time": "2023-05-22T10:00:00" }
				        ]
				    }
				}
				""";

		mockServer.expect(requestTo("https://pypi.org/pypi/requests/json"))
				.andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

		// Act
		Library result = pyPiService.collect("requests");

		// Assert
		assertNotNull(result);
		assertEquals("requests", result.getName());
		assertEquals("2.31.0", result.getVersion());
		assertEquals("pypi", result.getSource());
		assertEquals("Apache 2.0", result.getLicense());
		assertEquals("https://github.com/psf/requests", result.getRepository());
		assertEquals("2023-05-22T10:00:00", result.getLastRelease());
	}

	@Test
	void getPackageInfo_ShouldReturnEmpty_WhenPackageNotFound() {
		// Arrange
		mockServer.expect(requestTo("https://pypi.org/pypi/unknown-package/json"))
				.andRespond(withResourceNotFound());

		// Act
		Library result = pyPiService.collect("unknown-package");

		// Assert
		assertNull(result);
	}
}
