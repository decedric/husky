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
 * This line is intended for UTF-8 encoding checks, do not modify/delete: �����
 *
 */
package org.ehealth_connector.cda.ch.lrep.v133;

import javax.xml.bind.annotation.XmlTransient;
import org.ehealth_connector.common.hl7cdar2.ObjectFactory;

/**
 * Original ART-DECOR template id: 2.16.756.5.30.1.1.10.3.46
 * Template description: Chapter (CDA Body Section) containing the coded results according to IHE PCC Technical Framework Revision 11.0 - November 11, 2016.This section MAY contain relevant coded results of a patient for the given document context. The section MUST contain at least one Problem entry. In case of no relevant results, one of the 'special case' codes MUST be used.In the context of immunizations, this section may indicate the gestational age of a child if relevant for immunization of the child.In the context of lab reports, this section may indicate the gestational age of a child if relevant for the observation interpretation.
 *
 * Element description: Chapter (CDA Body Section) containing the coded results according to IHE PCC Technical Framework Revision 11.0 - November 11, 2016.This section MAY contain relevant coded results of a patient for the given document context. The section MUST contain at least one Problem entry. In case of no relevant results, one of the 'special case' codes MUST be used.In the context of immunizations, this section may indicate the gestational age of a child if relevant for immunization of the child.In the context of lab reports, this section may indicate the gestational age of a child if relevant for the observation interpretation.
 */
public class CdachvacdSectionOtherRelevantObservationsCoded extends org.ehealth_connector.common.hl7cdar2.POCDMT000040Section {

	public CdachvacdSectionOtherRelevantObservationsCoded() {
		super.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.756.5.30.1.1.10.3.46"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("1.3.6.1.4.1.19376.1.5.3.1.3.27"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("1.3.6.1.4.1.19376.1.5.3.1.3.28"));
		codeFixedValue = createHl7CodeFixedValue("30954-2", "2.16.840.1.113883.6.1", "LOINC", "Relevant diagnostic tests/laboratory data");
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:templateId:uid root = "2.16.756.5.30.1.1.10.3.46";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:templateId:uid root = "1.3.6.1.4.1.19376.1.5.3.1.3.27";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:templateId:uid root = "1.3.6.1.4.1.19376.1.5.3.1.3.28";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:code:st code = "30954-2";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:code:oid codeSystem = "2.16.840.1.113883.6.1";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:code:st codeSystemName = "LOINC";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:code:st displayName = "Relevant diagnostic tests/laboratory data";
	// cdachvacd_section_OtherRelevantObservationsCoded/hl7:entry:cs typeCode = "DRIV";
	}

	@XmlTransient()
	private org.ehealth_connector.common.hl7cdar2.CE codeFixedValue;

	/**
	 * Creates fixed contents for hl7Code
	 *
	 * @param code the desired fixed value for this argument.
	 * @param codeSystem the desired fixed value for this argument.
	 * @param codeSystemName the desired fixed value for this argument.
	 * @param displayName the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.CE createHl7CodeFixedValue(String code, String codeSystem, String codeSystemName, String displayName) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.CE retVal = factory.createCE();
		retVal.setCode(code);
		retVal.setCodeSystem(codeSystem);
		retVal.setCodeSystemName(codeSystemName);
		retVal.setDisplayName(displayName);
		return retVal;
	}

	/**
	 * Creates fixed contents for hl7Entry
	 *
	 * @param typeCode the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040Entry createHl7EntryFixedValue(String typeCode) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.POCDMT000040Entry retVal = factory.createPOCDMT000040Entry();
		retVal.setTypeCode(org.ehealth_connector.common.hl7cdar2.XActRelationshipEntry.fromValue(typeCode));
		return retVal;
	}

	/**
	 * Creates fixed contents for hl7TemplateId
	 *
	 * @param root the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.II createHl7TemplateIdFixedValue(String root) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.II retVal = factory.createII();
		retVal.setRoot(root);
		return retVal;
	}

	/**
	 * Gets the member codeFixedValue
	 */
	public org.ehealth_connector.common.hl7cdar2.CE getCodeFixedValue() {
		return codeFixedValue;
	}

	/**
	 * Gets the hl7Code
	 */
	public org.ehealth_connector.common.hl7cdar2.CE getHl7Code() {
		return code;
	}

	/**
	 * Gets the hl7Entry
	 * The narrative text in the text element of the section MUST be generated automatically from the information in this entry.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040Entry getHl7Entry() {
		org.ehealth_connector.common.hl7cdar2.POCDMT000040Entry retVal = null;
		if (getEntry() != null)
			if (getEntry().size() > 0)
				retVal = getEntry().get(0);
		return retVal;
	}

	/**
	 * Gets the hl7Id
	 * An ID for this section MAY be filled for traceability.
	 */
	public org.ehealth_connector.common.hl7cdar2.II getHl7Id() {
		return id;
	}

	/**
	 * Gets the hl7TemplateId
	 */
	public org.ehealth_connector.common.hl7cdar2.II getHl7TemplateId() {
		org.ehealth_connector.common.hl7cdar2.II retVal = null;
		if (getTemplateId() != null)
			if (getTemplateId().size() > 0)
				retVal = getTemplateId().get(0);
		return retVal;
	}

	/**
	 * Gets the hl7Text
	 * The narrative text for this section.
	 */
	public org.ehealth_connector.common.hl7cdar2.StrucDocText getHl7Text() {
		return text;
	}

	/**
	 * Gets the hl7Title
	 */
	public org.ehealth_connector.common.hl7cdar2.ST getHl7Title() {
		return title;
	}

	/**
	 * Sets the hl7Code
	 */
	public void setHl7Code(org.ehealth_connector.common.hl7cdar2.CE value) {
		this.code = value;
	}

	/**
	 * Sets the hl7Entry
	 * The narrative text in the text element of the section MUST be generated automatically from the information in this entry.
	 */
	public void setHl7Entry(org.ehealth_connector.common.hl7cdar2.POCDMT000040Entry value) {
		getEntry().clear();
		getEntry().add(value);
	}

	/**
	 * Sets the hl7Id
	 * An ID for this section MAY be filled for traceability.
	 */
	public void setHl7Id(org.ehealth_connector.common.hl7cdar2.II value) {
		this.id = value;
	}

	/**
	 * Sets the hl7TemplateId
	 */
	public void setHl7TemplateId(org.ehealth_connector.common.hl7cdar2.II value) {
		getTemplateId().clear();
		getTemplateId().add(value);
	}

	/**
	 * Sets the hl7Text
	 * The narrative text for this section.
	 */
	public void setHl7Text(org.ehealth_connector.common.hl7cdar2.StrucDocText value) {
		this.text = value;
	}

	/**
	 * Sets the hl7Title
	 */
	public void setHl7Title(org.ehealth_connector.common.hl7cdar2.ST value) {
		this.title = value;
	}
}