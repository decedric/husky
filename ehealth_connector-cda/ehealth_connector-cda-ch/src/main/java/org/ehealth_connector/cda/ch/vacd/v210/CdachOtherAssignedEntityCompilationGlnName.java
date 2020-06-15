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
 * This line is intended for UTF-8 encoding checks, do not modify/delete: äöüéè
 *
 */
package org.ehealth_connector.cda.ch.vacd.v210;

import org.ehealth_connector.common.hl7cdar2.ObjectFactory;

/**
 * Original ART-DECOR template id: 2.16.756.5.30.1.1.10.9.51 Template
 * description: Reusable template wherever an assigned entity with a required
 * GLN is used in a CDA-CH V2 document. CDA-CH V2 derivatives, i.e. Swiss
 * exchange formats MAY use this template by either reference or specialisation.
 *
 * Element description: The assigned organization.
 */
public class CdachOtherAssignedEntityCompilationGlnName
		extends org.ehealth_connector.common.hl7cdar2.POCDMT000040AssignedEntity {

	public CdachOtherAssignedEntityCompilationGlnName() {
		super.getId().add(createHl7IdFixedValue("2.51.1.3"));
	}

	/**
	 * Adds a hl7Addr The assigned entity's address.
	 */
	public void addHl7Addr(org.ehealth_connector.common.hl7cdar2.AD value) {
		getAddr().add(value);
	}

	/**
	 * Adds a hl7Telecom The assigned entity's means of communication (phone,
	 * eMail, ...).
	 */
	public void addHl7Telecom(org.ehealth_connector.common.hl7cdar2.TEL value) {
		getTelecom().add(value);
	}

	/**
	 * Adds a hl7Addr The assigned entity's address.
	 */
	public void clearHl7Addr() {
		getAddr().clear();
	}

	/**
	 * Adds a hl7Telecom The assigned entity's means of communication (phone,
	 * eMail, ...).
	 */
	public void clearHl7Telecom() {
		getTelecom().clear();
	}

	/**
	 * Creates fixed contents for CDA Element hl7Id
	 *
	 * @param root
	 *            the desired fixed value for this argument.
	 */
	private static org.ehealth_connector.common.hl7cdar2.II createHl7IdFixedValue(String root) {
		ObjectFactory factory = new ObjectFactory();
		org.ehealth_connector.common.hl7cdar2.II retVal = factory.createII();
		retVal.setRoot(root);
		return retVal;
	}

	/**
	 * Gets the hl7AssignedPerson The assigned person.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040Person getHl7AssignedPerson() {
		return assignedPerson;
	}

	/**
	 * Gets the hl7Code The human-readable text MUST be generated automatically
	 * from the structured information of this element. The text element MUST
	 * contain the reference to the corresponding text in the human readable
	 * part, ONLY.
	 */
	public org.ehealth_connector.common.hl7cdar2.CE getHl7Code() {
		return code;
	}

	/**
	 * Gets the hl7Id The assigned entity's GLN (including the assigned person's
	 * GLN).
	 */
	public java.util.List<org.ehealth_connector.common.hl7cdar2.II> getHl7Id() {
		return id;
	}

	/**
	 * Gets the hl7RepresentedOrganization The assigned organization.
	 */
	public org.ehealth_connector.common.hl7cdar2.POCDMT000040Organization getHl7RepresentedOrganization() {
		return representedOrganization;
	}

	/**
	 * Sets the hl7AssignedPerson The assigned person.
	 */
	public void setHl7AssignedPerson(
			org.ehealth_connector.common.hl7cdar2.POCDMT000040Person value) {
		this.assignedPerson = value;
	}

	/**
	 * Sets the hl7Code The human-readable text MUST be generated automatically
	 * from the structured information of this element. The text element MUST
	 * contain the reference to the corresponding text in the human readable
	 * part, ONLY.
	 */
	public void setHl7Code(org.ehealth_connector.common.hl7cdar2.CE value) {
		this.code = value;
	}

	/**
	 * Sets the hl7Id The assigned entity's GLN (including the assigned person's
	 * GLN).
	 */
	public void setHl7Id(org.ehealth_connector.common.hl7cdar2.II value) {
		getId().clear();
		getId().add(value);
	}

	/**
	 * Sets the hl7RepresentedOrganization The assigned organization.
	 */
	public void setHl7RepresentedOrganization(
			org.ehealth_connector.common.hl7cdar2.POCDMT000040Organization value) {
		this.representedOrganization = value;
	}
}
