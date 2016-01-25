package org.ehealth_connector.cda.ch.edes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehealth_connector.cda.MdhtFacade;
import org.ehealth_connector.cda.testhelper.TestUtils;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.ch.CHPackage;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CdaChEdesCtnnTest extends TestUtils {

	private final Log log = LogFactory.getLog(MdhtFacade.class);

	private XPathFactory xpathFactory = XPathFactory.newInstance();
	private XPath xpath = xpathFactory.newXPath();


	public CdaChEdesCtnnTest() {
		super();
	}
	
	@Test
	public void testDocumenHeader() throws XPathExpressionException {
		final CdaChEdesCtnn cda = new CdaChEdesCtnn();
		final Document document = cda.getDocument();

		// Notfallaustrittsbericht Pflege
		XPathExpression expr = xpath.compile("//templateId[@root='2.16.756.5.30.1.1.1.1.3.1.1.12']");
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());

		// Notfallaustrittsbericht V1
		expr = xpath.compile("//templateId[@root='2.16.756.5.30.1.1.1.1.3.1.1']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
		
		// Composite Triage and Nursing Note
		expr = xpath.compile("//templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.1.3']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
		
		// IHE PCC Medical Documents
		expr = xpath.compile("//templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.1']");
		nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		assertEquals(1, nodes.getLength());
	}
	
	@Test
	public void deserializeClinicalDocumentTest() throws Exception {
		final CdaChEdesCtnn cda = new CdaChEdesCtnn();
		final String deserialized = this.serializeDocument(cda);
		log.debug(deserialized);
		final ClinicalDocument cdaDeserialized = deserializeClinicalDocument(deserialized);
		assertTrue(cdaDeserialized != null);
	}

	@Test
	public void deserializeCdaDirectTest() throws Exception {
		final CdaChEdesCtnn cda = new CdaChEdesCtnn();
		final String deserialized = this.serializeDocument(cda);
		log.debug(deserialized);
		final CdaChEdesCtnn cdaDeserialized = deserializeCdaDirect(deserialized);
		assertTrue(cdaDeserialized != null);
	}

	@Test
	public void deserializeCdaTest() throws Exception {
		final CdaChEdesCtnn cda = new CdaChEdesCtnn();
		final String deserialized = this.serializeDocument(cda);
		log.debug(deserialized);
		final CdaChEdesCtnn cdaDeserialized = deserializeCda(deserialized);
		assertTrue(cdaDeserialized != null);
	}

	@Test
	public void deserializeCdaTestTemplateId() throws Exception {
		final CdaChEdesCtnn cda = new CdaChEdesCtnn();
		final String deserialized = this.serializeDocument(cda);
		log.debug(deserialized);
		final CdaChEdesCtnn cdaDeserialized = deserializeCda(deserialized);
		assertTrue(cdaDeserialized != null);
	}
	
	
	private ClinicalDocument deserializeClinicalDocument(String document) throws Exception {
		final InputSource source = new InputSource(new StringReader(document));
		return CDAUtil.load(source);
	}

	private CdaChEdesCtnn deserializeCda(String document) throws Exception {
		final InputSource source = new InputSource(new StringReader(document));
		return new CdaChEdesCtnn((org.openhealthtools.mdht.uml.cda.ch.CdaChEdesCtnn) CDAUtil.load(source));
	}

	private CdaChEdesCtnn deserializeCdaDirect(String document) throws Exception {
		final InputStream stream = new ByteArrayInputStream(document.getBytes());
		final ClinicalDocument clinicalDocument = CDAUtil.loadAs(stream, CHPackage.eINSTANCE.getCdaChEdesCtnn());
		return new CdaChEdesCtnn((org.openhealthtools.mdht.uml.cda.ch.CdaChEdesCtnn)clinicalDocument);
	}

	private String serializeDocument(CdaChEdesCtnn doc) throws Exception {
		final ByteArrayOutputStream boas = new ByteArrayOutputStream();
		CDAUtil.save(doc.getDoc(), boas);
		return boas.toString();
	}
}
