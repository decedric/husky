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
 * Year of publication: 2016
 *
 *******************************************************************************/
package org.ehealth_connector.cda.ch.lab.lrtp;

import org.ehealth_connector.cda.ch.lab.lrtp.enums.SpecialtySections;
import org.ehealth_connector.cda.ihe.lab.AbstractLaboratorySpecialtySection;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.enums.LanguageCode;

/**
 * The Class LaboratorySpecialtySection. <div class="en">Each LRPH document in
 * Swiss MUST contain exactly one laboratory specialty section.</div>
 * <div class="de">Für meldepflichtige Laborbefunde in der Schweiz wird
 * festgelegt, dass genau ein Kapitel vorhanden sein muss.</div>
 */
public class LaboratorySpecialtySection extends AbstractLaboratorySpecialtySection {

	/**
	 * Instantiates a new laboratory specialty section.
	 */
	public LaboratorySpecialtySection() {
		super();
	}

	/**
	 * Instantiates a new laboratory specialty section.
	 *
	 * @param code
	 *            the code
	 */
	public LaboratorySpecialtySection(Code code) {
		super(code);
	}

	/**
	 * Instantiates the class with the required elements. A
	 * LaboratoryReportDataProcessingEntry will be created automatically.
	 *
	 * @param code
	 *            the code for this section
	 * @param languageCode
	 *            the language code
	 */
	public LaboratorySpecialtySection(Code code, LanguageCode languageCode) {
		super(code, languageCode);
		setLaboratoryReportDataProcessingEntry(new LaboratoryReportDataProcessingEntry());
	}

	/**
	 * Instantiates the class with the required elements.
	 *
	 * @param code
	 *            the code for this section
	 * @param languageCode
	 *            the language code
	 * @param entry
	 *            the LaboratoryReportDataProcessingEntry
	 */
	public LaboratorySpecialtySection(Code code, LanguageCode languageCode,
			LaboratoryReportDataProcessingEntry entry) {
		this(code, languageCode);
		getMdht().getEntries().clear();
		getMdht().getEntries().add(0, entry.getMdht());
	}

	/**
	 * Instantiates a new laboratory specialty section.
	 *
	 * @param mdht
	 *            the mdht
	 */
	public LaboratorySpecialtySection(
			org.openhealthtools.mdht.uml.cda.ihe.lab.LaboratorySpecialtySection mdht) {
		super(mdht);
	}

	/**
	 * Instantiates the class with the required elements. A
	 * LaboratoryReportDataProcessingEntry will be created automatically.
	 *
	 * @param code
	 *            the code for this section
	 * @param languageCode
	 *            the language code
	 */
	public LaboratorySpecialtySection(SpecialtySections code, LanguageCode languageCode) {
		this(code.getCode(), languageCode);
	}

	/**
	 * Gets the laboratory report data processing entry.
	 *
	 * @return the laboratory report data processing entry
	 */
	public org.ehealth_connector.cda.ch.lab.lrtp.LaboratoryReportDataProcessingEntry getLaboratoryReportDataProcessingEntry() {
		if (!getMdht().getEntries().isEmpty() && getMdht().getEntries().get(
				0) instanceof org.openhealthtools.mdht.uml.cda.ihe.lab.LaboratoryReportDataProcessingEntry) {
			return new org.ehealth_connector.cda.ch.lab.lrtp.LaboratoryReportDataProcessingEntry(
					(org.openhealthtools.mdht.uml.cda.ihe.lab.LaboratoryReportDataProcessingEntry) getMdht()
							.getEntries().get(0));
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.ehealth_connector.cda.ihe.lab.AbstractLaboratorySpecialtySection#setLaboratoryReportDataProcessingEntry(org.ehealth_connector.cda.ihe.lab.LaboratoryReportDataProcessingEntry)
	 */
	public void setLaboratoryReportDataProcessingEntry(
			org.ehealth_connector.cda.ch.lab.lrtp.LaboratoryReportDataProcessingEntry entry) {
		getMdht().getEntries().clear();
		getMdht().getEntries().add(0, entry.copy());
	}
}
