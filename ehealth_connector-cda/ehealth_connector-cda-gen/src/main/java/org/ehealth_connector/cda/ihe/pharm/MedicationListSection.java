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

package org.ehealth_connector.cda.ihe.pharm;

import java.util.ArrayList;
import java.util.List;

import org.ehealth_connector.cda.MdhtFacade;
import org.ehealth_connector.common.enums.LanguageCode;
import org.ehealth_connector.common.utils.Util;
import org.openhealthtools.mdht.uml.cda.ihe.pharm.PHARMFactory;

/**
 * Implements the Medication List Section Content Module
 */
public class MedicationListSection
		extends MdhtFacade<org.openhealthtools.mdht.uml.cda.ihe.pharm.MedicationListSection> {

	/** The language code. */
	private LanguageCode languageCode;

	/**
	 * Instantiates a new medication list section.
	 */
	public MedicationListSection() {
		this(LanguageCode.ENGLISH);
	}

	/**
	 * Instantiates a new medication list section.
	 *
	 * @param languageCode
	 *            the language code
	 */
	public MedicationListSection(LanguageCode languageCode) {
		super(PHARMFactory.eINSTANCE.createMedicationListSection().init());
		this.setLanguageCode(languageCode);

		if (languageCode == LanguageCode.FRENCH)
			this.getMdht().setTitle(Util.st("Liste de médicaments"));
		if (languageCode == LanguageCode.GERMAN)
			this.getMdht().setTitle(Util.st("Medikamentenliste"));
		if (languageCode == LanguageCode.ITALIAN)
			this.getMdht().setTitle(Util.st("Lista farmaci"));
		if (languageCode == LanguageCode.ENGLISH)
			this.getMdht().setTitle(Util.st("Medication List"));
	}

	/**
	 * Instantiates a new medication list section.
	 *
	 * @param section
	 *            the section
	 */
	public MedicationListSection(
			org.openhealthtools.mdht.uml.cda.ihe.pharm.MedicationListSection section) {
		super(section);
	}

	/**
	 * Adds the dispense item entry.
	 *
	 * @param entry
	 *            the entry
	 */
	public void addDispenseItemEntry(DispenseItemEntry entry) {
		this.getMdht().addSupply(entry.getMdht());
	}

	/**
	 * Adds the medication treatment plan item entry.
	 *
	 * @param entry
	 *            the entry
	 */
	public void addMedicationTreatmentPlanItemEntry(MedicationTreatmentPlanItemEntry entry) {
		this.getMdht().addSubstanceAdministration(entry.getMdht());
	}

	/**
	 * Adds the pharmaceutical advice item entry.
	 *
	 * @param entry
	 *            the entry
	 */
	public void addPharmaceuticalAdviceItemEntry(PharmaceuticalAdviceItemEntry entry) {
		this.getMdht().addObservation(entry.getMdht());
	}

	/**
	 * Adds the prescription item entry.
	 *
	 * @param entry
	 *            the entry
	 */
	public void addPrescriptionItemEntry(PrescriptionItemEntry entry) {
		this.getMdht().addSubstanceAdministration(entry.getMdht());
	}

	/**
	 * Gets the dispense item entries.
	 *
	 * @return the dispense item entries
	 */
	public List<DispenseItemEntry> getDispenseItemEntries() {
		final List<DispenseItemEntry> entries = new ArrayList<DispenseItemEntry>();
		for (org.openhealthtools.mdht.uml.cda.ihe.pharm.DispenseItemEntry entry : getMdht()
				.getDispenseItemEntries()) {
			entries.add(new DispenseItemEntry(entry));
		}
		return entries;
	}

	/**
	 * Gets the medication treatment plan item entries.
	 *
	 * @return the medication treatment plan item entries
	 */
	public List<MedicationTreatmentPlanItemEntry> getMedicationTreatmentPlanItemEntries() {
		final List<MedicationTreatmentPlanItemEntry> entries = new ArrayList<MedicationTreatmentPlanItemEntry>();
		for (org.openhealthtools.mdht.uml.cda.ihe.pharm.MedicationTreatmentPlanItemEntry entry : getMdht()
				.getMedicationTreatmentPlanItemEntries()) {
			entries.add(new MedicationTreatmentPlanItemEntry(entry));
		}
		return entries;
	}

	/**
	 * Gets the pharmaceutical advice item entries.
	 *
	 * @return the pharmaceutical advice item entries
	 */
	public List<PharmaceuticalAdviceItemEntry> getPharmaceuticalAdviceItemEntries() {
		final List<PharmaceuticalAdviceItemEntry> entries = new ArrayList<PharmaceuticalAdviceItemEntry>();
		for (org.openhealthtools.mdht.uml.cda.ihe.pharm.PharmaceuticalAdviceItemEntry entry : getMdht()
				.getPharmaceuticalAdviceItemEntries()) {
			entries.add(new PharmaceuticalAdviceItemEntry(entry));
		}
		return entries;
	}

	/**
	 * Gets the prescription item entries.
	 *
	 * @return the prescription item entries
	 */
	public List<PrescriptionItemEntry> getPrescriptionItemEntries() {
		final List<PrescriptionItemEntry> entries = new ArrayList<PrescriptionItemEntry>();
		for (org.openhealthtools.mdht.uml.cda.ihe.pharm.PrescriptionItemEntry entry : getMdht()
				.getPrescriptionItemEntries()) {
			entries.add(new PrescriptionItemEntry(entry));
		}
		return entries;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		if (this.getMdht().getTitle() != null) {
			return this.getMdht().getTitle().getText();
		}
		return null;
	}

	public LanguageCode getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(LanguageCode languageCode) {
		this.languageCode = languageCode;
	}

}
