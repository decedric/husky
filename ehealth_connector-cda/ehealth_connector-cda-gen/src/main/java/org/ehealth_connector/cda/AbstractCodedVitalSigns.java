package org.ehealth_connector.cda;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.ehealth_connector.common.Author;
import org.ehealth_connector.common.Identificator;
import org.ehealth_connector.common.enums.CodeSystems;
import org.ehealth_connector.common.enums.LanguageCode;
import org.ehealth_connector.common.utils.DateUtil;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.Component4;
import org.openhealthtools.mdht.uml.cda.Observation;
import org.openhealthtools.mdht.uml.cda.Organizer;
import org.openhealthtools.mdht.uml.cda.ccd.VitalSignsOrganizer;
import org.openhealthtools.mdht.uml.cda.ihe.CodedVitalSignsSection;
import org.openhealthtools.mdht.uml.cda.ihe.IHEFactory;
import org.openhealthtools.mdht.uml.cda.ihe.VitalSignsSection;
import org.openhealthtools.mdht.uml.hl7.vocab.ActRelationshipHasComponent;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType;

public abstract class AbstractCodedVitalSigns extends MdhtFacade<VitalSignsSection> {

	private final List<AbstractObservation> myVitalSignObservations = new ArrayList<AbstractObservation>();

	// default language is German
	private LanguageCode languageCode = LanguageCode.GERMAN;

	public AbstractCodedVitalSigns() {
		super(IHEFactory.eINSTANCE.createCodedVitalSignsSection().init());
	}

	protected AbstractCodedVitalSigns(CodedVitalSignsSection mdht) {
		super(mdht);
	}

	public void add(AbstractVitalSignsOrganizer organizer, AbstractVitalSignObservation vitalSign,
			Author author, String contendIdPrefix) {
		myVitalSignObservations.add(vitalSign);
		if (author == null) {
			// default to author of document
			if (!getMdht().getClinicalDocument().getAuthors().isEmpty()) {
				author = new Author(getMdht().getClinicalDocument().getAuthors().get(0));
			} else {
				final org.openhealthtools.mdht.uml.cda.Author mdhtAuthor = CDAFactory.eINSTANCE
						.createAuthor();
				mdhtAuthor.setNullFlavor(NullFlavor.UNK);
				author = new Author(mdhtAuthor);
			}
		}

		Identificator id = null;
		if (!organizer.getIds().isEmpty()) {
			id = organizer.getIds().get(0);
		}
		final VitalSignsOrganizer mdhtOrganizer = getOrganizer(id, vitalSign.getEffectiveTime(),
				author);
		// VitalSignsOrganizer mdhtOrganizer = organizer.getMdht();
		mdhtOrganizer.addObservation(vitalSign.getMdhtCopy());
		// update the component type
		final EList<Component4> components = mdhtOrganizer.getComponents();
		components.get(components.size() - 1).setTypeCode(ActRelationshipHasComponent.COMP);

	}

	protected abstract AbstractVitalSignObservation createVitalSignObservation(
			org.openhealthtools.mdht.uml.cda.ihe.VitalSignObservation mdht);

	public List<AbstractVitalSignObservation> getCodedVitalSignObservations() {
		final List<AbstractVitalSignObservation> ret = new ArrayList<AbstractVitalSignObservation>();
		final EList<Organizer> organizers = getMdht().getOrganizers();
		for (final Organizer organizer : organizers) {
			final EList<Observation> observations = organizer.getObservations();
			for (final Observation observation : observations) {
				if (observation instanceof org.openhealthtools.mdht.uml.cda.ihe.VitalSignObservation) {
					ret.add(createVitalSignObservation(
							(org.openhealthtools.mdht.uml.cda.ihe.VitalSignObservation) observation));
				}
			}
		}
		ret.sort(new Comparator<AbstractVitalSignObservation>() {
			@Override
			public int compare(AbstractVitalSignObservation left,
					AbstractVitalSignObservation right) {
				return right.getEffectiveTime().compareTo(left.getEffectiveTime());
			}
		});
		return ret;
	}

	/**
	 * Method to get
	 *
	 * @return the languageCode
	 */
	public LanguageCode getLanguageCode() {
		return languageCode;
	}

	private VitalSignsOrganizer getOrganizer(Identificator id, Date effectiveTime, Author author) {
		final VitalSignsSection section = getMdht();
		final EList<VitalSignsOrganizer> organizers = section.getVitalSignsOrganizers();
		for (final VitalSignsOrganizer organizer : organizers) {
			if (!organizer.getIds().isEmpty() && (id != null)) {
				if (organizer.getIds().get(0).getExtension().equals(id.getExtension()))
					return organizer;
			}
		}
		for (final VitalSignsOrganizer organizer : organizers) {
			final Date organizerDate = DateUtil
					.parseIVL_TSVDateTimeValue(organizer.getEffectiveTime());
			if (organizerDate.equals(effectiveTime)) {
				return organizer;
			}
		}
		final VitalSignsOrganizer organizer = IHEFactory.eINSTANCE.createVitalSignsOrganizer()
				.init();
		try {
			organizer.setEffectiveTime(DateUtil.createIVL_TSFromEuroDateTime(effectiveTime));
			organizer.getIds().add(getUuid().getIi());
			final org.openhealthtools.mdht.uml.cda.Author mdhtAuthor = author.copyMdhtAuthor();
			mdhtAuthor.setTypeCode(ParticipationType.AUT);
			organizer.getAuthors().add(mdhtAuthor);
			// fix the code system name for schematron validation
			if (CodeSystems.SNOMEDCT.getCodeSystemId().equals(organizer.getCode().getCodeSystem())
					&& !organizer.getCode().getCodeSystemName().equals("SNOMED CT")) {
				organizer.getCode().setCodeSystemName("SNOMED CT");
			}
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		section.addOrganizer(organizer);
		return organizer;
	}

	// /**
	// * Creates the narrative text table
	// *
	// * @param contendIdPrefix
	// * the contend id prefix
	// * @return the table row
	// */
	// private String getTable(String contendIdPrefix) {
	// final StringBuilder sb = new StringBuilder();
	// final List<AbstractVitalSignObservation> observations =
	// getCodedVitalSignObservations();
	//
	// if (!observations.isEmpty()) {
	// sb.append("<table><tbody>");
	// if (getLanguageCode() == LanguageCode.GERMAN) {
	// sb.append(
	// "<tr><th>Datum /
	// Uhrzeit</th><th>Beschreibung</th><th>Resultat</th></tr>");
	// } else {
	// sb.append("<tr><th>Date /
	// Time</th><th>Description</th><th>Result</th></tr>");
	// }
	//
	// int colIndex = 0;
	// for (final AbstractVitalSignObservation vitalSignObservation :
	// observations) {
	// colIndex++;
	// final String signDateTime = DateUtil
	// .formatDateTimeCh(vitalSignObservation.getEffectiveTime());
	//
	// String signDescription = VitalSignCodes
	// .getEnum(vitalSignObservation.getCode().getCode())
	// .getDisplayName(getLanguageCode());
	// if (signDescription.equals(""))
	// signDescription = vitalSignObservation.getCode().getDisplayName();
	//
	// // TODOX
	// final String contentId = contendIdPrefix + colIndex++;
	// signDescription = "<content ID=\"" + contentId + "\">" + signDescription
	// + "</content>";
	// vitalSignObservation.setTextReference("#" + contentId);
	// // end of TODOX
	//
	// String signResult =
	// vitalSignObservation.getValue().getPhysicalQuantityValue() + " "
	// + vitalSignObservation.getValue().getPhysicalQuantityUnit();
	// final Code code = vitalSignObservation.getInterpretationCode();
	// if ((code != null) && !code.isNullFlavor() &&
	// !ObservationInterpretation.NORMAL
	// .getCodeValue().equals(code.getCode())) {
	// final String signInterpretation = "["
	// + vitalSignObservation.getInterpretationCode().getCode() + "]";
	// signResult += " " + signInterpretation;
	// }
	// final Code target = vitalSignObservation.getTargetSiteCode();
	// if ((target != null) && !target.isNullFlavor()) {
	//
	// String signTarget = ActSite
	// .getEnum(vitalSignObservation.getTargetSiteCode().getCode())
	// .getDisplayName(getLanguageCode());
	// if (signTarget.equals(""))
	// signTarget = vitalSignObservation.getTargetSiteCode().getDisplayName();
	//
	// // String signTarget = "["
	// // +
	// // vitalSignObservation.getTargetSiteCode().getDisplayName()
	// // + "]";
	// if (!signTarget.equals(""))
	// signDescription += " [" + signTarget + "]";
	// }
	// sb.append("<tr><td>" + signDateTime + "</td><td>" + signDescription +
	// "</td><td>"
	// + signResult + "</td></tr>");
	// }
	// sb.append("</tbody></table>");
	// }
	//
	// return sb.toString();
	// }
	//
	protected abstract Identificator getUuid();

	public List<AbstractObservation> getVitalSignObservations() {
		return myVitalSignObservations;
	}

	/**
	 * Method to set
	 *
	 * @param languageCode
	 *            the languageCode to set
	 */
	public void setLanguageCode(LanguageCode languageCode) {
		this.languageCode = languageCode;
	}

}
