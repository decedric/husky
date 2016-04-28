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

package org.ehealth_connector.cda.ihe.pharm;

import static org.junit.Assert.assertEquals;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The Class MedicationFullfillmentInstructionsEntrys.
 */
public class MedicationFullfillmentInstructionsEntryTest {

	private XPath xpath = PharmXPath.getXPath();

	@Test
	public void testSerializeEmpty() throws Exception {
		final MedicationFullfillmentInstructionsEntry entry = new MedicationFullfillmentInstructionsEntry();

		final Document document = entry.getDocument();

		XPathExpression expr = xpath
				.compile("//templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.4.3.1']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("//templateId[@root='2.16.840.1.113883.10.20.1.43']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath
				.compile("//code[@code='FINSTRUCT' and @codeSystem='1.3.6.1.4.1.19376.1.5.3.2']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("//statusCode[@code='completed']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testTextReference() throws XPathExpressionException {
		final MedicationFullfillmentInstructionsEntry entry = new MedicationFullfillmentInstructionsEntry();

		entry.setTextReference("#reference1");

		final Document document = entry.getDocument();

		final XPathExpression expr = xpath.compile("//text/reference[@value='#reference1']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		assertEquals("#reference1", entry.getTextReference());
	}

}
