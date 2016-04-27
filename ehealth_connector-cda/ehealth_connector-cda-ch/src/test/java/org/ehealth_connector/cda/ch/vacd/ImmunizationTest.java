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

package org.ehealth_connector.cda.ch.vacd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.ehealth_connector.cda.Consumable;
import org.ehealth_connector.cda.SectionAnnotationCommentEntry;
import org.ehealth_connector.cda.ch.enums.RouteOfAdministration;
import org.ehealth_connector.cda.ch.vacd.enums.CdaChVacdImmunizations;
import org.ehealth_connector.cda.ch.vacd.enums.CdaChVacdRecCategories;
import org.ehealth_connector.common.Author;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.Identificator;
import org.ehealth_connector.common.Name;
import org.ehealth_connector.common.Organization;
import org.ehealth_connector.common.enums.LanguageCode;
import org.ehealth_connector.common.utils.DateUtil;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The Class ImmunizationTests.
 */
public class ImmunizationTest {

	private final XPathFactory xpathFactory = XPathFactory.newInstance();
	private final XPath xpath = xpathFactory.newXPath();

	// <substanceadministration xmlns="urn:hl7-org:v3" classCode="SBADM"
	// moodCode="EVN" negationInd="false">
	// <templateId root="1.3.6.1.4.1.19376.1.5.3.1.4.12"/>
	// <templateId root="2.16.840.1.113883.10.20.1.24"/>
	// <templateId root="2.16.756.5.30.1.1.1.1.1"
	// extension="CDA-CH.Body.MediL3"/>
	// <code code="IMMUNIZ" codeSystem="2.16.840.1.113883.5.4"
	// codeSystemName="HL7ActCode"/>
	// <statusCode code="completed"/>
	// <priorityCode nullFlavor="UNK"/>
	// <routeCode nullFlavor="UNK"/>
	// <doseQuantity nullFlavor="UNK"/>
	// </substanceadministration>

	@Test
	public void testAddCriterionEntry() throws Exception {

		final Immunization immunization = new Immunization();
		assertEquals(null, immunization.getCriterionEntry());

		final CriterionEntry entry = new CriterionEntry();
		entry.setRecCategory(CdaChVacdRecCategories.REC_NONE, LanguageCode.ITALIAN);

		immunization.setCriterionEntry(entry);

		assertEquals(entry, immunization.getCriterionEntry());

		final Document document = immunization.getDocument();

		XPathExpression expr = xpath.compile(
				"substanceadministration/precondition/criterion/templateId[@root='2.16.756.5.30.1.1.1.1.3.5.1' and @extension='CDA-CH.VACD.Body.MediL3.Category']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/precondition/criterion/code[@code='41504' and @codeSystem='2.16.756.5.30.1.127.3.3.4']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testAddMedicationTarget() throws Exception {

		final Immunization immunization = new Immunization();

		assertEquals(0, immunization.getMedicationTargetEntries().size());

		final MedicationTargetEntry medicationTargetEntry = new MedicationTargetEntry();
		medicationTargetEntry.setImmunizationTarget(CdaChVacdImmunizations.HEPB);
		immunization.addMedicationTargetEntry(medicationTargetEntry);

		assertEquals(1, immunization.getMedicationTargetEntries().size());
		assertEquals(medicationTargetEntry, immunization.getMedicationTargetEntries().get(0));

		final Document document = immunization.getDocument();

		XPathExpression expr = xpath.compile(
				"//templateId[@root='2.16.756.5.30.1.1.1.1.1' and @extension='CDA-CH.Body.MediL3']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/entryRelationship[@typeCode='RSON' and @inversionInd='false']/observation[@classCode='OBS' and @moodCode='EVN']/templateId[@root='2.16.756.5.30.1.1.1.1.3.5.1' and @extension='CDA-CH.VACD.Body.MediL3.Reason']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testAuthorAsDoctor() throws Exception {
		final Immunization immunization = new Immunization();

		final Name name = new Name("Pieks", "Ratschlag", "Prof.", "Immunologin");
		final Author author = new Author(name, "7607777777777");
		immunization.setAuthor(author);

		final Document document = immunization.getDocument();

		assertEquals(false, immunization.getAuthor().isAuthorPatient());

		XPathExpression expr = xpath.compile(
				"substanceadministration/author/functionCode[@code='221' and @codeSystem='2.16.840.1.113883.2.9.6.2.7' and @codeSystemName='ISCO-08' and @displayName='Medical doctors']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/author/assignedAuthor/id[@root='2.51.1.3' and @extension='7607777777777']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/author/assignedAuthor/assignedPerson/name/given='Pieks'");
		assertTrue((Boolean) expr.evaluate(document, XPathConstants.BOOLEAN));

	}

	@Test
	public void testAuthorAsPatient() throws Exception {
		final Immunization immunization = new Immunization();

		final Name name = new Name("Muster", "Franz");

		final Author author = new Author(name);
		author.addId(new Identificator("OID des jeweiligen Systems",
				"D der eintragenden Person, welche innerhalb der OID des jeweiligen Systems eindeutig ist"));
		author.setAuthorIsPatient(true);

		immunization.setAuthor(author);

		assertEquals(true, immunization.getAuthor().isAuthorPatient());

		final Document document = immunization.getDocument();

		final XPathExpression expr = xpath
				.compile("substanceadministration/author/functionCode[@nullFlavor='NA']");
		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testCommentEntry() throws XPathExpressionException {
		final SectionAnnotationCommentEntry commentEntry = new SectionAnnotationCommentEntry();
		commentEntry.setContentIdReference("#reference");

		final Immunization entry = new Immunization();
		entry.setCommentEntry(commentEntry);

		assertEquals(commentEntry, entry.getCommentEntry());

		final Document document = entry.getDocument();

		final XPathExpression expr = xpath.compile(
				"substanceadministration/entryRelationship[@typeCode='SUBJ' and @inversionInd='true']/act/text/reference[@value='#reference']");
		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	// <consumable typeCode='CSM'>
	// <manufacturedProduct>
	// <templateId root='1.3.6.1.4.1.19376.1.5.3.1.4.7.2'/>
	// <templateId root='2.16.840.1.113883.10.20.1.53'/> <id root='1.3.160'
	// extension='7680006370012' />
	// <manufacturedMaterial>
	// <code code='J07CA02' codeSystem='2.16.840.1.113883.6.73' > <originalText>
	// diphtheria-pertussis-poliomyelitis-tetanus
	// <reference value='#ii1'/>
	// </originalText>
	// </code>
	// <name>BOOSTRIX Polio Inj Susp</name> </manufacturedMaterial>
	// <manufacturerOrganization>
	// <name>GlaxoSmithKline AG</name> </manufacturerOrganization>
	// </manufacturedProduct>
	// </consumable>
	@Test
	public void testConsumable() throws XPathExpressionException {
		final Consumable consumable = new Consumable("BOOSTRIX Polio Inj Susp");

		final Code whoAtc = new Code("2.16.840.1.113883.6.73", "J07CA02");

		final Organization organization = new Organization("GlaxoSmithKline");
		consumable.setManufacturer(organization);
		final Identificator gtin = new Identificator("1.3.160", "7680006370012");
		consumable.setManufacturedProductId(gtin);
		consumable.setLotNr("lotNr");
		consumable.setWhoAtcCode(whoAtc);

		final Immunization entry = new Immunization();
		entry.setConsumable(consumable);

		assertEquals(whoAtc, entry.getConsumable().getWhoAtcCode());
		assertEquals(gtin, entry.getConsumable().getManufacturedProductId());
		assertEquals("BOOSTRIX Polio Inj Susp", entry.getConsumable().getTradeName());

		final Document document = entry.getDocument();

		XPathExpression expr = xpath.compile(
				"substanceadministration/consumable/manufacturedProduct/templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.4.7.2']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/consumable/manufacturedProduct/id[@root='1.3.160' and @extension='7680006370012']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/consumable/manufacturedProduct/manufacturedMaterial/name='BOOSTRIX Polio Inj Susp'");
		assertTrue((Boolean) expr.evaluate(document, XPathConstants.BOOLEAN));
	}

	@Test
	public void testEffectiveTime() throws XPathExpressionException {
		final Immunization entry = new Immunization();

		final Date appliedAt = DateUtil.parseDateyyyyMMdd("20151019");
		entry.setApplyDate(appliedAt);

		assertEquals(appliedAt, entry.getApplyDate());

		final Document document = entry.getDocument();

		final XPathExpression expr = xpath
				.compile("substanceadministration/effectiveTime[@value='20151019']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testImmunizationSofwareId() throws Exception {
		final Immunization immunization = new Immunization();

		final Identificator identificator = new Identificator("oid", "id");
		immunization.setId(identificator);

		assertEquals(identificator, immunization.getId());

		final Document document = immunization.getDocument();

		final XPathExpression expr = xpath
				.compile("substanceadministration/id[@root='oid' and @extension='id']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

	}

	@Test
	public void testImmunizationUndesired() throws Exception {
		final Immunization immunization = new Immunization();

		assertTrue(immunization.isAdministered());
		assertTrue(!immunization.isUndesired());

		immunization.setUndesired();
		assertTrue(!immunization.isAdministered());
		assertTrue(immunization.isUndesired());

		final Document document = immunization.getDocument();

		final XPathExpression expr = xpath.compile("substanceadministration[@negationInd='true']");
		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

	}

	@Test
	public void testPriorityCode() throws XPathExpressionException {
		final Immunization entry = new Immunization();

		final Code priorityCode = new Code("2.16.840.1.113883.5.7", "UR");
		entry.setPriorityCode(priorityCode);
		assertEquals(priorityCode, entry.getPriorityCode());

		final Document document = entry.getDocument();

		final XPathExpression expr = xpath.compile(
				"substanceadministration/priorityCode[@code='UR' and @codeSystem='2.16.840.1.113883.5.7']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testRouteCode() throws XPathExpressionException {
		final Immunization entry = new Immunization();
		entry.setRouteOfAdministration(RouteOfAdministration.INJECTION_SUBCUTANEOUS);
		assertEquals(RouteOfAdministration.INJECTION_SUBCUTANEOUS,
				entry.getRouteOfAdministration());

		final Document document = entry.getDocument();
		final XPathExpression expr = xpath.compile(
				"substanceadministration/routeCode[@code='SQ' and @codeSystem='2.16.840.1.113883.5.112']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}

	@Test
	public void testSerializeEmpty() throws Exception {
		final Immunization immunization = new Immunization();

		final Document document = immunization.getDocument();

		XPathExpression expr = xpath
				.compile("//templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.4.12']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("//templateId[@root='2.16.840.1.113883.10.20.1.24']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"//templateId[@root='2.16.756.5.30.1.1.1.1.1' and @extension='CDA-CH.Body.MediL3']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("substanceadministration/routeCode[@nullFlavor='NA']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("substanceadministration[@negationInd='false']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile(
				"substanceadministration/code[@code='IMMUNIZ' and @codeSystem='2.16.840.1.113883.5.4']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("substanceadministration/statusCode[@code='completed']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("substanceadministration/priorityCode[@nullFlavor='UNK']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

	}

	@Test
	public void testTextReference() throws XPathExpressionException {
		final Immunization entry = new Immunization();

		entry.setTextReference("#reference1");

		final Document document = entry.getDocument();

		final XPathExpression expr = xpath
				.compile("substanceadministration/text/reference[@value='#reference1']");

		final NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		assertEquals("#reference1", entry.getTextReference());
	}

}
