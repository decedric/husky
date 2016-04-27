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

import org.ehealth_connector.cda.ch.PastProblemConcern;
import org.ehealth_connector.cda.ch.ProblemEntry;
import org.ehealth_connector.cda.ch.enums.RiskOfExposure;
import org.ehealth_connector.cda.enums.ProblemConcernStatusCode;
import org.ehealth_connector.cda.ihe.pharm.PharmaceuticalAdviceConcernEntry;
import org.ehealth_connector.common.enums.LanguageCode;
import org.ehealth_connector.common.utils.DateUtil;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The Class PharmaceuticalAdviceConcernEntryTest.
 */
public class PharmaceuticalAdviceConcernEntryTest {

	private XPathFactory xpathFactory = XPathFactory.newInstance();
	private XPath xpath = xpathFactory.newXPath();

	@Test
	public void testProblemConcernEntry() throws Exception {
		final PharmaceuticalAdviceConcernEntry entry = new PharmaceuticalAdviceConcernEntry();

		final ProblemEntry pastProblem = new ProblemEntry();
		pastProblem.setExposureRisk(RiskOfExposure.BESCHAEFTIGTE_IM_GESUNDHEITSWESEN,
				LanguageCode.ENGLISH);
		pastProblem.setStartDate(DateUtil.date("08.10.2014"));
		pastProblem.setEndDate(DateUtil.date("31.12.2014"));

		final PastProblemConcern leiden = new PastProblemConcern("Ehemalige GFP", pastProblem,
				ProblemConcernStatusCode.COMPLETED, DateUtil.date("15.12.1999"),
				DateUtil.date("20.06.2002"));

		entry.addProblemConcernEntry(leiden);

		final Document document = entry.getDocument();

		XPathExpression expr = xpath
				.compile("/act/entryRelationship[@typeCode='SUBJ' and @inversionInd='false']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

}
