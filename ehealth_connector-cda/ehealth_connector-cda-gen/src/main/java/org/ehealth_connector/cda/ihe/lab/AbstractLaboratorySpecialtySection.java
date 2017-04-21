/*
 *
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
package org.ehealth_connector.cda.ihe.lab;

import org.ehealth_connector.cda.MdhtSectionFacade;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.enums.LanguageCode;
import org.openhealthtools.mdht.uml.cda.ihe.lab.LABFactory;

/**
 * The Class AbstractLaboratorySpecialtySection.
 */
public abstract class AbstractLaboratorySpecialtySection extends
		MdhtSectionFacade<org.openhealthtools.mdht.uml.cda.ihe.lab.LaboratorySpecialtySection> {

	/**
	 * Instantiates a new abstract laboratory specialty section.
	 */
	public AbstractLaboratorySpecialtySection() {
		super(LABFactory.eINSTANCE.createLaboratorySpecialtySection().init());
		setTitle(LanguageCode.ENGLISH);
	}

	/**
	 * Instantiates a new abstract laboratory specialty section.
	 *
	 * @param code
	 *            the code
	 */
	public AbstractLaboratorySpecialtySection(Code code) {
		this();
		setCode(code);
	}

	/**
	 * Instantiates a new abstract laboratory specialty section.
	 *
	 * @param code
	 *            the code
	 * @param languageCode
	 *            the language code
	 */
	public AbstractLaboratorySpecialtySection(Code code, LanguageCode languageCode) {
		this(code);
		setTitle(languageCode);
	}

	/**
	 * Instantiates a new abstract laboratory specialty section.
	 *
	 * @param mdht
	 *            the mdht
	 */
	public AbstractLaboratorySpecialtySection(
			org.openhealthtools.mdht.uml.cda.ihe.lab.LaboratorySpecialtySection mdht) {
		super(mdht);
	}

	/**
	 * Sets the laboratory report data processing entry.
	 *
	 * @param entry
	 *            the new laboratory report data processing entry
	 */
	public void setLaboratoryReportDataProcessingEntry(LaboratoryReportDataProcessingEntry entry) {
		getMdht().getEntries().clear();
		getMdht().getEntries().add(entry.getMdht());
	}

	/**
	 * Sets the title.
	 *
	 * @param languageCode
	 *            the new title
	 */
	public void setTitle(LanguageCode languageCode) {
		switch (languageCode) {
		case FRENCH:
			setTitle("Rapport de laboratoire");
			break;
		case GERMAN:
			this.setTitle("Laborbefund");
			break;
		case ITALIAN:
			setTitle("Rapporto di laboratorio");
			break;
		case ENGLISH:
			setTitle("Laboratory Specialty Section");
			break;
		}
	}
}
