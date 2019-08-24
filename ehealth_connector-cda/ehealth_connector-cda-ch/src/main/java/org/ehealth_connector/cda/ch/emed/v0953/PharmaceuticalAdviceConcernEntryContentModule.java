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
package org.ehealth_connector.cda.ch.emed.v0953;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;
import org.ehealth_connector.common.hl7cdar2.ObjectFactory;

/**
 * Original ART-DECOR template id: 2.16.756.5.30.1.1.10.4.81
 * Template description: A Pharmaceutical Advice Concern Item belongs to one Pharmaceutical Advice Item and represents the information to concerns (e.g., problems, allergies, etc.) which the Medication Treatment Plan-, Prescription-, Dispense- or Administration Item referenced by the underlying Pharmaceutical Advice Item causes.
 */
public class PharmaceuticalAdviceConcernEntryContentModule extends org.ehealth_connector.common.hl7cdar2.POCDMT000040Act {

	public PharmaceuticalAdviceConcernEntryContentModule() {
		super.setClassCode(org.ehealth_connector.common.hl7cdar2.XActClassDocumentEntryAct.fromValue("ACT"));
		super.setMoodCode(org.ehealth_connector.common.hl7cdar2.XDocumentActMood.fromValue("EVN"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.756.5.30.1.1.10.4.81"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("2.16.840.1.113883.10.20.1.27"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("1.3.6.1.4.1.19376.1.5.3.1.4.5.1"));
		super.getTemplateId().add(createHl7TemplateIdFixedValue("1.3.6.1.4.1.19376.1.9.1.3.5"));
		super.setCode(createHl7CodeFixedValue("NA"));
		super.setStatusCode(createHl7StatusCodeFixedValue("active"));
		super.getEntryRelationship().add(createHl7EntryRelationshipFixedValue("SUBJ", null));
		super.getEntryRelationship().add(createHl7EntryRelationshipFixedValue("SUBJ", null));
		super.getEntryRelationship().add(createHl7EntryRelationshipFixedValue("REFR", "false"));
		super.getEntryRelationship().add(createHl7EntryRelationshipFixedValue("SUBJ", "true"));
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:act:null classCode = "ACT";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:act:null moodCode = "EVN";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:templateId:null root = "2.16.756.5.30.1.1.10.4.81";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:templateId:null root = "2.16.840.1.113883.10.20.1.27";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:templateId:null root = "1.3.6.1.4.1.19376.1.5.3.1.4.5.1";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:templateId:null root = "1.3.6.1.4.1.19376.1.9.1.3.5";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:code:null nullFlavor = "NA";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:statusCode:cs code = "active"; (isVocab)
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null typeCode = "SUBJ";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null typeCode = "SUBJ";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null typeCode = "REFR";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null inversionInd = "false";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null typeCode = "SUBJ";
	// PharmaceuticalAdviceConcernEntryContentModule/hl7:entryRelationship:null inversionInd = "true";
	}

	@XmlTransient()
	private String myClassCode;

	@XmlTransient()
	private String myMoodCode;

	/**
	 * Creates fixed contents for CDA Attribute classCode
	 */
	private void createClassCodeFixedValue(String value) {
		this.myClassCode = value;
	}

	/**
	 * Creates fixed contents for CDA Element hl7Code
	 *
	 * @param nullFlavor the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.CD createHl7CodeFixedValue(String nullFlavor) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.CD retVal = factory.createCD();
		retVal.nullFlavor = new ArrayList<String>();
		retVal.nullFlavor.add(nullFlavor);
		return retVal;
	}

	/**
	 * Creates fixed contents for CDA Element hl7EntryRelationship
	 *
	 * @param typeCode the desired fixed value for this argument.
	 * @param inversionInd the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040EntryRelationship createHl7EntryRelationshipFixedValue(String typeCode, String inversionInd) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.POCDMT000040EntryRelationship retVal = factory.createPOCDMT000040EntryRelationship();
		retVal.setTypeCode(org.ehealth_connector.common.hl7cdar2.XActRelationshipEntryRelationship.fromValue(typeCode));
		retVal.setInversionInd(Boolean.parseBoolean(inversionInd));
		return retVal;
	}

	/**
	 * Creates fixed contents for CDA Element hl7StatusCode
	 *
	 * @param code the desired fixed value for this argument.
	 */
	public org.ehealth_connector.common.hl7cdar2.CS createHl7StatusCodeFixedValue(String code) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.CS retVal = factory.createCS();
		retVal.setCode(code);
		return retVal;
	}

	/**
	 * Creates fixed contents for CDA Element hl7TemplateId
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
	 * Creates fixed contents for CDA Attribute moodCode
	 */
	private void createMoodCodeFixedValue(String value) {
		this.myMoodCode = value;
	}

	/**
	 * Gets the hl7Code
	 * Code
	 */
	public org.ehealth_connector.common.hl7cdar2.CD getHl7Code() {
		return code;
	}

	/**
	 * Gets the hl7EffectiveTime
	 * Problems determined
	 */
	public org.ehealth_connector.common.hl7cdar2.IVLTS getHl7EffectiveTime() {
		return effectiveTime;
	}

	/**
	 * Gets the hl7EntryRelationship
	 */
	public java.util.List<org.ehealth_connector.common.hl7cdar2.POCDMT000040EntryRelationship> getHl7EntryRelationship() {
		return entryRelationship;
	}

	/**
	 * Gets the hl7Id
	 * Pharmaceutical Advice Concern ID
	 */
	public java.util.List<org.ehealth_connector.common.hl7cdar2.II> getHl7Id() {
		return id;
	}

	/**
	 * Gets the hl7StatusCode
	 * Status Code
	 */
	public org.ehealth_connector.common.hl7cdar2.CS getHl7StatusCode() {
		return statusCode;
	}

	/**
	 * Gets the hl7TemplateId
	 * CH-PHARM Pharmaceutical Advice Concern Entry TemplateID
	 */
	public java.util.List<org.ehealth_connector.common.hl7cdar2.II> getHl7TemplateId() {
		return templateId;
	}

	/**
	 * Gets the hl7Text
	 * Narrative description of the concern
	 */
	public org.ehealth_connector.common.hl7cdar2.ED getHl7Text() {
		return text;
	}

	/**
	 * Gets the member myClassCode
	 */
	public String getPredefinedClassCode() {
		return myClassCode;
	}

	/**
	 * Gets the member myMoodCode
	 */
	public String getPredefinedMoodCode() {
		return myMoodCode;
	}

	/**
	 * Sets the hl7Code
	 * Code
	 */
	public void setHl7Code(org.ehealth_connector.common.hl7cdar2.CD value) {
		this.code = value;
	}

	/**
	 * Sets the hl7EffectiveTime
	 * Problems determined
	 */
	public void setHl7EffectiveTime(org.ehealth_connector.common.hl7cdar2.IVLTS value) {
		this.effectiveTime = value;
	}

	/**
	 * Sets the hl7EntryRelationship
	 */
	public void setHl7EntryRelationship(org.ehealth_connector.common.hl7cdar2.POCDMT000040EntryRelationship value) {
		getEntryRelationship().clear();
		getEntryRelationship().add(value);
	}

	/**
	 * Sets the hl7Id
	 * Pharmaceutical Advice Concern ID
	 */
	public void setHl7Id(org.ehealth_connector.common.hl7cdar2.II value) {
		getId().clear();
		getId().add(value);
	}

	/**
	 * Sets the hl7StatusCode
	 * Status Code
	 */
	public void setHl7StatusCode(org.ehealth_connector.common.hl7cdar2.CS value) {
		this.statusCode = value;
	}

	/**
	 * Sets the hl7TemplateId
	 * CH-PHARM Pharmaceutical Advice Concern Entry TemplateID
	 */
	public void setHl7TemplateId(org.ehealth_connector.common.hl7cdar2.II value) {
		getTemplateId().clear();
		getTemplateId().add(value);
	}

	/**
	 * Sets the hl7Text
	 * Narrative description of the concern
	 */
	public void setHl7Text(org.ehealth_connector.common.hl7cdar2.ED value) {
		this.text = value;
	}
}
