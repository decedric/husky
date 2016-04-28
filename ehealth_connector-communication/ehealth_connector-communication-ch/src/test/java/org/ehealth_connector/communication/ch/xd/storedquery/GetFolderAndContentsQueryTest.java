/*******************************************************************************
 *
 * The authorship of this code and the accompanying materials is held by medshare GmbH, Switzerland.
 * All rights reserved. http://medshare.net
 *
 * Project Team: https://sourceforge.net/p/ehealthconnector/wiki/Team/
 *
 * This code is made available under the terms of the Eclipse Public License v1.0.
 *
 * Accompanying materials are made available under the terms of the Creative Commons
 * Attribution-ShareAlike 4.0 License.
 *
 * Year of publication: 2015
 *
 *******************************************************************************/
package org.ehealth_connector.communication.ch.xd.storedquery;

import static org.junit.Assert.assertTrue;

import org.ehealth_connector.communication.ch.testhelper.XdsChTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.ihe.xds.consumer.storedquery.ObjectType;
import org.openhealthtools.ihe.xds.consumer.storedquery.StoredQueryParameterList;

/**
 * Test of class GetFolderAndContentsQuery
 */
public class GetFolderAndContentsQueryTest extends XdsChTestUtils {

	/**
	 * Method implementing
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.ehealth_connector.communication.ch.xd.storedquery.GetFolderAndContentsQuery#GetFolderAndContentsQuery(java.lang.String, boolean, org.ehealth_connector.communication.ch.enums.FormatCode[], org.ehealth_connector.communication.ch.enums.ConfidentialityCode[])}
	 * .
	 */
	@Test
	public void testGetFolderAndContentsQueryStringBooleanFormatCodeArrayConfidentialityCodeArray() {
		final GetFolderAndContentsQuery q1 = new GetFolderAndContentsQuery("1234", true,
				formatCodes, confidentialityCodes);

		final StoredQueryParameterList sqpl1 = q1.getOhtStoredQuery().getQueryParameters();

		assertTrue(sqpl1.get("$XDSFolderEntryUUID").contains("1234"));
		assertTrue(
				sqpl1.get("$XDSDocumentEntryFormatCode").contains(formatCodes[1].getCodeValue()));
		assertTrue(sqpl1.get("$XDSDocumentEntryConfidentialityCode")
				.contains(confidentialityCodes[1].getCodeSystemOid()));
	}

	/**
	 * Test method for
	 * {@link org.ehealth_connector.communication.ch.xd.storedquery.GetFolderAndContentsQuery#GetFolderAndContentsQuery(java.lang.String, boolean, org.ehealth_connector.communication.ch.enums.FormatCode[], org.ehealth_connector.communication.ch.enums.ConfidentialityCode[], java.lang.String)}
	 * .
	 */
	@Test
	public void testGetFolderAndContentsQueryStringBooleanFormatCodeArrayConfidentialityCodeArrayString() {
		final GetFolderAndContentsQuery q2 = new GetFolderAndContentsQuery("1234", true,
				formatCodes, confidentialityCodes, "9876");
		assertTrue(q2.getOhtStoredQuery().getHomeCommunityId().contains("9876"));
	}

	/**
	 * Test method for
	 * {@link org.ehealth_connector.communication.ch.xd.storedquery.GetFolderAndContentsQuery#GetFolderAndContentsQuery(java.lang.String, boolean, org.ehealth_connector.communication.ch.enums.FormatCode[], org.ehealth_connector.communication.ch.enums.ConfidentialityCode[], java.lang.String, org.openhealthtools.ihe.xds.consumer.storedquery.ObjectType)}
	 * .
	 */
	@Test
	public void testGetFolderAndContentsQueryStringBooleanFormatCodeArrayConfidentialityCodeArrayStringObjectType() {
		final GetFolderAndContentsQuery q3 = new GetFolderAndContentsQuery("1234", true,
				formatCodes, confidentialityCodes, "6565873dsdgsdg", ObjectType.STATIC);
		assertTrue(q3.getOhtStoredQuery().getQueryParameters().get("$XDSDocumentEntryType")
				.contains("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"));
	}

}
