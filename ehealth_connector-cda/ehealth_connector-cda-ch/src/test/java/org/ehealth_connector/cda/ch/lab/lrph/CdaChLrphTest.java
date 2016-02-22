package org.ehealth_connector.cda.ch.lab.lrph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.xpath.XPathExpressionException;

import org.ehealth_connector.cda.ch.lab.AbstractLaboratoryReportTest;
import org.ehealth_connector.cda.ihe.lab.LaboratorySpecialtySection;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.ch.CHPackage;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class CdaChLrphTest extends AbstractLaboratoryReportTest {

	private CdaChLrph deserializeCda(String document) throws Exception {
		final InputSource source = new InputSource(new StringReader(document));
		return new CdaChLrph((org.openhealthtools.mdht.uml.cda.ch.CdaChLrph) CDAUtil.load(source));
	}

	private CdaChLrph deserializeCdaDirect(String document) throws Exception {
		final InputStream stream = new ByteArrayInputStream(document.getBytes());
		final ClinicalDocument clinicalDocument = CDAUtil.loadAs(stream,
				CHPackage.eINSTANCE.getCdaChMtpsDis());
		return new CdaChLrph((org.openhealthtools.mdht.uml.cda.ch.CdaChLrph) clinicalDocument);
	}

	private ClinicalDocument deserializeClinicalDocument(String document) throws Exception {
		final InputSource source = new InputSource(new StringReader(document));
		return CDAUtil.load(source);
	}

	// private String serializeDocument(CdaChLrph doc) throws Exception {
	// final ByteArrayOutputStream boas = n
	// }

	@Test
	public void testContentModules() throws XPathExpressionException {
		final CdaChLrph doc = new CdaChLrph();

		// Specialty Section
		LaboratorySpecialtySection lss = new LaboratorySpecialtySection();
		doc.setLaboratorySpecialtySection(lss);
		assertNotNull(doc.getLaboratorySpecialtySection());
		Document document = doc.getDocument();
		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.3.2.1", null));

		// Convenience LaboratoryBatteryOrganizer
		LaboratoryBatteryOrganizer lbo = new LaboratoryBatteryOrganizer();
		doc.addLaboratoryBatteryOrganizer(lbo);
		assertFalse(doc.getLaboratoryBatteryOrganizerList().isEmpty());
		document = doc.getDocument();

		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1.4", null));
		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.3.2.1", null));
		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1", null));
		// a second Laboratory Battery Organizer
		doc.addLaboratoryBatteryOrganizer(lbo);
		document = doc.getDocument();
		// there must be two Laboratory Battery Organizer
		assertFalse(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1.4", null));
		assertTrue(xCount(document, "//templateId[@root='1.3.6.1.4.1.19376.1.3.1.4']", 2));
		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.3.2.1", null));
		assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1", null));

		// // Laboratory Isolate Organizer
		// doc.addLaboratoryIsolateOrganizer(new LaboratoryIsolateOrganizer());
		// assertFalse(doc.getLaboratoryIsolateOrganizerList().isEmpty());
		// document = doc.getDocument();
		// assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1.5",
		// null));
		// assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1.4",
		// null));
		// assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.3.2.1",
		// null));
		// assertTrue(xExistTemplateId(document, "1.3.6.1.4.1.19376.1.3.1", null));

		//

	}

	@Override
	@Test
	public void testDocumentHeader() throws XPathExpressionException {
		final CdaChLrph cda = new CdaChLrph();
		final Document document = cda.getDocument();

		// LRPH
		assertTrue(xExistTemplateId(document, "2.16.756.5.30.1.1.1.1.3.3.1", null));
	}
}