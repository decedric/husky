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
package org.ehealth_connector.cda.ch.lrep;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.ehealth_connector.common.CdaNamespacePrefixMapper;
import org.ehealth_connector.common.hl7cdar2.POCDMT000040ClinicalDocument;

/**
 * Original ART-DECOR template id: 2.16.756.5.30.1.1.10.2.57
 * Template description: The laboratory report MUST contain at least one receiver. This can be a person or an institution. If the document is created for internal use, the recipient can include the same information as author or custodian. In case of health care professionals the declaration is as for custodian. For patients or other persons, the declaration is as for recordTarget or author.
 *
 * Element description: The laboratory report MUST contain at least one receiver.
 */
public class CdachlrepHeaderRecipient extends org.ehealth_connector.common.hl7cdar2.POCDMT000040InformationRecipient {

	/**
	 * The laboratory report MUST contain at least one receiver.
	 */
	private org.ehealth_connector.common.hl7cdar2.POCDMT000040IntendedRecipient hl7IntendedRecipient;

	private org.ehealth_connector.common.hl7cdar2.II hl7TemplateId;

	private org.ehealth_connector.common.hl7cdar2.II hl7TemplateId1;

	private org.ehealth_connector.common.hl7cdar2.II hl7TemplateId2;

	/**
	 * Gets the hl7IntendedRecipient
	 * The laboratory report MUST contain at least one receiver.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040IntendedRecipient getHl7IntendedRecipient() {
		return hl7IntendedRecipient;
	}

	/**
	 * Gets the hl7TemplateId
	 */
	public org.ehealth_connector.common.hl7cdar2.II getHl7TemplateId() {
		return hl7TemplateId;
	}

	/**
	 * Gets the hl7TemplateId1
	 */
	public org.ehealth_connector.common.hl7cdar2.II getHl7TemplateId1() {
		return hl7TemplateId1;
	}

	/**
	 * Gets the hl7TemplateId2
	 */
	public org.ehealth_connector.common.hl7cdar2.II getHl7TemplateId2() {
		return hl7TemplateId2;
	}

	/**
	 * Loads the CDA document from file.
	 * @param inputFileName the full path and filename of the sourcefile.
	 * @return the CDA document\n@throws JAXBException\n@throws IOException Signals that an I/O exception has occurred.
	 */
	public static CdachlrepHeaderRecipient loadFromFile(String inputFileName) throws JAXBException, IOException {
		return loadFromFile(new File(inputFileName));
	}

	/**
	 * Loads the CDA document from file.
	 * @param inputFile the source file.
	 * n@return the CDA document\n@throws JAXBException\n@throws IOException Signals that an I/O exception has occurred.
	 */
	public static CdachlrepHeaderRecipient loadFromFile(File inputFile) throws JAXBException, IOException {
		CdachlrepHeaderRecipient retVal;
		JAXBContext context = JAXBContext.newInstance(CdachlrepHeaderRecipient.class);
		Unmarshaller mar = context.createUnmarshaller();
		StreamSource source = new StreamSource(inputFile);
		JAXBElement<CdachlrepHeaderRecipient> root = mar.unmarshal(source, CdachlrepHeaderRecipient.class);
		retVal = root.getValue();
		return retVal;
	}

	/**
	 * Saves the current CDA document to file.
	 * @param outputFileName the full path and filename of the destination file.
	 * @throws JAXBException
	 */
	public void saveToFile(String outputFileName) throws JAXBException {
		saveToFile(new File(outputFileName));
	}

	/**
	 * Saves the current CDA document to file.
	 * @param outputFile the destination file.
	 * @throws JAXBException
	 */
	public void saveToFile(File outputFile) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(this.getClass());
		Marshaller mar = context.createMarshaller();
		mar.setProperty("com.sun.xml.bind.namespacePrefixMapper", new CdaNamespacePrefixMapper());
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(this, outputFile);
	}

	/**
	 * Sets the hl7IntendedRecipient
	 * The laboratory report MUST contain at least one receiver.
	 */
	public void setHl7IntendedRecipient(org.ehealth_connector.common.hl7cdar2.POCDMT000040IntendedRecipient value) {
		hl7IntendedRecipient = value;
	}

	/**
	 * Sets the hl7TemplateId
	 */
	public void setHl7TemplateId(org.ehealth_connector.common.hl7cdar2.II value) {
		hl7TemplateId = value;
	}

	/**
	 * Sets the hl7TemplateId1
	 */
	public void setHl7TemplateId1(org.ehealth_connector.common.hl7cdar2.II value) {
		hl7TemplateId1 = value;
	}

	/**
	 * Sets the hl7TemplateId2
	 */
	public void setHl7TemplateId2(org.ehealth_connector.common.hl7cdar2.II value) {
		hl7TemplateId2 = value;
	}
}
