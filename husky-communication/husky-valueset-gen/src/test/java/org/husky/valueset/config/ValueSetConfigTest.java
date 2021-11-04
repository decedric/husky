/*
 * The authorship of this project and accompanying materials is held by medshare GmbH, Switzerland.
 * All rights reserved. https://medshare.net
 *
 * Source code, documentation and other resources have been contributed by various people.
 * Project Team: https://gitlab.com/ehealth-connector/api/wikis/Team/
 * For exact developer information, please refer to the commit history of the forge.
 *
 * This code is made available under the terms of the Eclipse Public License v1.0.
 *
 * Accompanying materials are made available under the terms of the Creative Commons
 * Attribution-ShareAlike 4.0 License.
 *
 * This line is intended for UTF-8 encoding checks, do not modify/delete: äöüéè
 *
 */
package org.husky.valueset.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.husky.valueset.config.ValueSetConfig;
import org.husky.valueset.enums.SourceFormatType;
import org.husky.valueset.enums.SourceSystemType;
import org.junit.jupiter.api.Test;

/**
 * The Test Class for ValueSetConfig.
 */
public class ValueSetConfigTest {

	/**
	 * Do all tests.
	 */
	@Test
	public void doAllTests() {

		// Basic ValueSetConfig elements tests
		String className = "className";
		String projectFolder = "//projectFolder";
		SourceFormatType sourceFormatType = SourceFormatType.JSON;
		SourceSystemType sourceSystemType = SourceSystemType.ARTDECOR_FHIR;
		String sourceUrl = "http://foo.bar";

		ValueSetConfig valueSetConfig = ValueSetConfig.builder().withClassName(className)
				.withProjectFolder(projectFolder).withSourceFormatType(sourceFormatType)
				.withSourceSystemType(sourceSystemType).withSourceUrl(sourceUrl).build();

		assertEquals(className, valueSetConfig.getClassName());
		assertEquals(projectFolder, valueSetConfig.getProjectFolder());
		assertEquals(sourceFormatType, valueSetConfig.getSourceFormatType());
		assertEquals(sourceSystemType, valueSetConfig.getSourceSystemType());
		assertEquals(sourceUrl, valueSetConfig.getSourceUrl());

	}
}