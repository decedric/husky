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
 * cdach_other_TextElementWithReferenceToNarrativeText
 *
 * Template description: Reusable template wherever a text reference to the corresponding text in the human readable part (narrative text) is used in a CDA-CH V2 document. CDA-CH V2 derivatives, i.e. Swiss exchange formats MAY use this template by either reference or specialisation.
 *
 * <!-- @formatter:off -->
 * Identifier: 2.16.756.5.30.1.1.10.9.14
 * Effective date: 2018-04-18 00:00:00
 * Version: 2017
 * Status: active
 * <!-- @formatter:on -->
 */
@Generated(value = "org.ehealth_connector.codegenerator.cda.ArtDecor2JavaGenerator", date = "2021-03-05")
public class CdachOtherTextElementWithReferenceToNarrativeText
		extends org.husky.common.hl7cdar2.ED {

	public CdachOtherTextElementWithReferenceToNarrativeText() {
	}

	/**
	 * Gets the hl7Reference The reference to the corresponding text in the
	 * human readable part must be specified by reference to content[@ID]:
	 * reference[@value='#xxx']
	 */
	public org.husky.common.hl7cdar2.TEL getHl7Reference() {
		return reference;
	}

	/**
	 * Sets the hl7Reference The reference to the corresponding text in the
	 * human readable part must be specified by reference to content[@ID]:
	 * reference[@value='#xxx']
	 */
	public void setHl7Reference(org.husky.common.hl7cdar2.TEL value) {
		this.reference = value;
	}
}