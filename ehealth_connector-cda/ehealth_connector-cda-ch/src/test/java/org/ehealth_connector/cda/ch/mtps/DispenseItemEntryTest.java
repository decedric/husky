/*******************************************************************************
 *
 * The authorship of this code and the accompanying materials is held by medshare GmbH, Switzerland.
 * All rights reserved. http://medshare.net
 *
 * Project Team: https://sourceforge.net/p/ehealthconnector/wiki/Team/
 *
 * This code is are made available under the terms of the Eclipse Public License v1.0.
 *
 * Accompanying materials are made available under the terms of the Creative Commons
 * Attribution-ShareAlike 4.0 License.
 *
 * Year of publication: 2015
 *
 *******************************************************************************/

package org.ehealth_connector.cda.ch.mtps;

import static org.junit.Assert.assertEquals;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.ehealth_connector.cda.ch.mtps.enums.DispenseCodeList;
import org.ehealth_connector.cda.ihe.pharm.DispenseItemEntry;
import org.ehealth_connector.common.enums.LanguageCode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The Class DispenseItemEntry.
 */
public class DispenseItemEntryTest {

	private XPathFactory xpathFactory = XPathFactory.newInstance();
	private XPath xpath = xpathFactory.newXPath();

	@Test
	public void testDispenseCodeList() throws Exception {

		final DispenseItemEntry entry = new DispenseItemEntry();

		entry.setDispenseCode(DispenseCodeList.REFILL_COMPLETE.getCode(LanguageCode.FRENCH));

		final Document document = entry.getDocument();

		XPathExpression expr = xpath
				.compile("//code[@code='RFC' and @codeSystem='2.16.840.1.113883.5.4']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
		assertEquals(DispenseCodeList.REFILL_COMPLETE,
				DispenseCodeList.getEnum(entry.getDispenseCode().getCode()));
	}

}
