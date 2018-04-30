/*
 * The authorship of this project and accompanying materials is held by medshare GmbH, Switzerland.
 * All rights reserved. https://medshare.net
 *
 * Source code, documentation and other resources have been contributed by various people.
 * Project Team: https://sourceforge.net/p/ehealthconnector/wiki/Team/
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
package org.ehealth_connector.communication.ch.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openhealthtools.ihe.xds.metadata.CodedMetadataType;

public class ClassCodeTest {

	@Test
	public void testGetCodeSystem() {

		assertEquals("2.16.756.5.30.1.127.3.10.1.3", ClassCode.VALUE_SET_ID);

		for (ClassCode entry : ClassCode.values()) {

			assertNotNull(entry.getCodeSystemValue());
			assertNotNull(entry.getCodeValue());

			CodedMetadataType cmt = entry.getCodedMetadataType();
			assertEquals(entry.getCodeSystemValue(), cmt.getSchemeName());
			assertEquals(entry.getCodeValue(), cmt.getCode());

		}

	}

}