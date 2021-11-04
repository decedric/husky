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
package org.husky.cda.ch.emed.v097;

import javax.annotation.processing.Generated;

/**
 * HeaderTemplateCompilationMedicationTreatmentPlan
 *
 * Template description: Header Templates for MedicationTreatmentPlan
 *
 * Element description: The document's creation date and time. If this document replaces a previous version (linked via parentDocument), this is the date and time of the new version.
 *
 * <!-- @formatter:off -->
 * Identifier: 2.16.756.5.30.1.1.10.9.44
 * Effective date: 2018-01-08 16:26:11
 * Version: 2019
 * Status: pending
 * <!-- @formatter:on -->
 */
@Generated(value = "org.ehealth_connector.codegenerator.cda.ArtDecor2JavaGenerator", date = "2021-03-05")
public class HeaderTemplateCompilationMedicationTreatmentPlan
		extends org.husky.common.hl7cdar2.POCDMT000040ClinicalDocument {

	public HeaderTemplateCompilationMedicationTreatmentPlan() {
	}

	/**
	 * Gets the hl7EffectiveTime The document's creation date and time. If this
	 * document replaces a previous version (linked via parentDocument), this is
	 * the date and time of the new version.
	 */
	public org.husky.common.hl7cdar2.TS getHl7EffectiveTime() {
		return effectiveTime;
	}

	/**
	 * Gets the hl7Title
	 */
	public org.husky.common.hl7cdar2.ST getHl7Title() {
		return title;
	}

	/**
	 * Sets the hl7EffectiveTime The document's creation date and time. If this
	 * document replaces a previous version (linked via parentDocument), this is
	 * the date and time of the new version.
	 */
	public void setHl7EffectiveTime(org.husky.common.hl7cdar2.TS value) {
		this.effectiveTime = value;
	}

	/**
	 * Sets the hl7Title
	 */
	public void setHl7Title(org.husky.common.hl7cdar2.ST value) {
		this.title = value;
	}
}