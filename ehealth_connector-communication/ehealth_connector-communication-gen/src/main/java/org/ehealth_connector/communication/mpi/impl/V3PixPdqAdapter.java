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
package org.ehealth_connector.communication.mpi.impl;

import static org.ehealth_connector.communication.CamelService.log;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.CamelContext;
import org.eclipse.emf.ecore.EPackage;
import org.ehealth_connector.common.enums.TelecomAddressUse;
import org.ehealth_connector.communication.CamelService;
import org.ehealth_connector.communication.mpi.MpiAdapterInterface;
import org.ehealth_connector.communication.utils.PixPdqV3Utils;
import org.ehealth_connector.fhir.structures.gen.FhirCommon;
import org.ehealth_connector.fhir.structures.gen.FhirPatient;
import org.ehealth_connector.xua.core.SecurityHeaderElement;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.openhealthtools.ihe.atna.auditor.PDQConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.common.hl7v3.client.V3GenericAcknowledgement;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqConsumer;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqContinuationCancel;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqContinuationQuery;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumer;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumerQuery;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumerResponse;
import org.openhealthtools.ihe.pix.source.v3.V3PixSource;
import org.openhealthtools.ihe.pix.source.v3.V3PixSourceAcknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import net.ihe.gazelle.hl7v3.coctmt030007UV.COCTMT030007UVPerson;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03ContactParty;
import net.ihe.gazelle.hl7v3.datatypes.AD;
import net.ihe.gazelle.hl7v3.datatypes.AdxpStreetAddressLine;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.EnFamily;
import net.ihe.gazelle.hl7v3.datatypes.EnGiven;
import net.ihe.gazelle.hl7v3.datatypes.EnPrefix;
import net.ihe.gazelle.hl7v3.datatypes.EnSuffix;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.datatypes.ON;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.datatypes.TEL;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02BirthPlace;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Citizen;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Employee;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02LanguageCommunication;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Nation;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02PersonalRelationship;
import net.ihe.gazelle.hl7v3.voc.HomeAddressUse;
import net.ihe.gazelle.hl7v3.voc.WorkPlaceAddressUse;

/**
 * V3PixPdqAdapter
 *
 * V3PixPdqAdapter implements the Actor Patient Identity Source from ITI-44
 * Patient Identity Feed HL7 V3 and the Actor Patient Identifier Cross-reference
 * Consumer from ITI-45 PIXV3 Query as well as the ITI-47 PDQ Consumer
 *
 * The V3PixPdqAdapter implements the MpiAdapterInterfare with the Open Health
 * Tools (OHT) IHE Profile Classes V3PixConsumer, V3PixSource andV3PdqConsumer
 *
 * @see "https://www.projects.openhealthtools.org/sf/projects/iheprofiles/V3PixSource"
 * @see "https://www.projects.openhealthtools.org/sf/projects/iheprofiles/javadocs/2.0.0/org/openhealthtools/ihe/pix/source/v3/V3PixSource.html/V3PixConsumer"
 * @see "https://www.projects.openhealthtools.org/sf/projects/iheprofiles/profiles/pdq.html"
 */
@Component
public class V3PixPdqAdapter extends CamelService implements MpiAdapterInterface<V3PdqQuery, V3PdqQueryResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(V3PixPdqAdapter.class.getName());

	/** The adapter cfg. */
	private V3PixPdqAdapterConfig adapterCfg;

	/** The home community oid. */
	private String homeCommunityOid;

	/**
	 * Used to capture additional oid identifiers for the person such as a
	 * Drivers’ license or Social Security Number.
	 */
	private final Set<String> otherIdsOidSet;

	/** if the pdq consumer is configured. */
	private boolean pdqConsumerConfigured;

	/** If the pix consumer is configured. */
	private boolean pixConsumerConfigured;

	/** The pix source. */
	private V3PixSource pixSource;

	/** If the pix source is configured. */
	private boolean pixSourceConfigured;

	/** The v3 pdq consumer. */
	private V3PdqConsumer v3PdqConsumer;

	/** The v3 pix consumer. */
	private V3PixConsumer v3PixConsumer;

	/** The orginal model package. */
	private EPackage eOrigPackage;

	/**
	 * Instantiates a new v3 pix adapter.
	 */
	public V3PixPdqAdapter() {
		otherIdsOidSet = new HashSet<>();
	}

	/**
	 * Instantiates a new v3 pix adapter.
	 */
	public V3PixPdqAdapter(CamelContext context) {
		setCamelContext(context);
		otherIdsOidSet = new HashSet<>();
	}

	/**
	 * Instantiates a new v3 pix adapter.
	 *
	 * @param adapterConfig
	 *            the adapter config
	 */
	public V3PixPdqAdapter(V3PixPdqAdapterConfig adapterConfig) {
		otherIdsOidSet = new HashSet<>();
		adapterCfg = adapterConfig;
		homeCommunityOid = adapterCfg.getHomeCommunityOid();
		if (adapterConfig.getOtherOidIds() != null) {
			Collections.addAll(otherIdsOidSet, adapterConfig.getOtherOidIds());
		}
	}

	public void setConfig(V3PixPdqAdapterConfig adapterConfig) {
		adapterCfg = adapterConfig;
		homeCommunityOid = adapterCfg.getHomeCommunityOid();
		if (adapterConfig.getOtherOidIds() != null) {
			Collections.addAll(otherIdsOidSet, adapterConfig.getOtherOidIds());
		}
	}

	/**
	 * adds the demographic data to the pix queries, can be overloaded if additional
	 * information of the patient needs to be providied for the mpi.
	 *
	 * @param patient            the patient
	 * @param v3PixSourceMessage the v3 add message
	 */
	protected void addDemographicData(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		if (v3PixSourceMessage == null) {
			return;
		}
		setScopingOrganization(patient, v3PixSourceMessage);
		addPatientIds(patient, v3PixSourceMessage);
		addPatientName(patient, v3PixSourceMessage);
		setPatientBirthTime(patient, v3PixSourceMessage);
		setPatientGender(patient, v3PixSourceMessage);
		addPatientAddresses(patient, v3PixSourceMessage);
		addPatientTelecoms(patient, v3PixSourceMessage);
		addLanguageCommunications(patient, v3PixSourceMessage);
		setPatientMaritalStatus(patient, v3PixSourceMessage);
		setDeceased(patient, v3PixSourceMessage);
		setMultipeBirth(patient, v3PixSourceMessage);
		setPatientMothersMaidenName(patient, v3PixSourceMessage);
		setBirthPlace(patient, v3PixSourceMessage);
		setPatientReligiousAffiliation(patient, v3PixSourceMessage);
		setNation(patient, v3PixSourceMessage);
		setEmployee(patient, v3PixSourceMessage);
	}

	/**
	 * adds the demographic data from the pdq query to the fhir patient, can be
	 * overloaded if additional information of the patient needs to be providied
	 * for the mpi.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addDemographicData(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if (pdqPatient == null) {
			return;
		}
		setScopingOrganization(pdqPatient, patient);
		addPatientIds(pdqPatient, patient);
		addPatientName(pdqPatient, patient);
		setPatientBirthTime(pdqPatient, patient);
		setPatientGender(pdqPatient, patient);
		addPatientAddresses(pdqPatient, patient);
		addPatientTelecoms(pdqPatient, patient);
		addLanguageCommunications(pdqPatient, patient);
		setPatientMaritalStatus(pdqPatient, patient);
		setDeceased(pdqPatient, patient);
		setMultipeBirth(pdqPatient, patient);
		setPatientMothersMaidenName(pdqPatient, patient);
		setBirthPlace(pdqPatient, patient);
		setPatientReligiousAffiliation(pdqPatient, patient);
		setNation(pdqPatient, patient);
		setEmployee(pdqPatient, patient);
	}

	/**
	 * Adds the language communications from the patient to the pix message.
	 * FHIR language code is based on http://tools.ietf.org/html/bcp47, HL7V3
	 * makes no requirements
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void addLanguageCommunications(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		if (!patient.getCommunication().isEmpty()) {
			for (final PatientCommunicationComponent communication : patient.getCommunication()) {
				v3PixSourceMessage.addLanguageCommunication(communication.getLanguage().getText());
			}
		}
	}

	/**
	 * Adds the language communications from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addLanguageCommunications(PRPAMT201310UV02Patient pdqPatient,
			FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getLanguageCommunication() != null)) {
			for (final PRPAMT201310UV02LanguageCommunication languageCommunication : pdqPatient
					.getPatientPerson().getLanguageCommunication()) {
				if ((languageCommunication.getLanguageCode() != null)
						&& (languageCommunication.getLanguageCode().getCode() != null)) {
					final var communication = new PatientCommunicationComponent();
					final var language = new CodeableConcept();
					language.setText(languageCommunication.getLanguageCode().getCode());
					communication.setLanguage(language);
					patient.getCommunication().add(communication);

				}
			}
		}
	}

	/**
	 * add another oid identifiers for the person such as a Drivers’ license or
	 * Social Security Number.
	 *
	 * @param oid
	 *            oid for the domain to add as otherId in the pix v3 message
	 */
	public void addOtherIdsOid(String oid) {
		otherIdsOidSet.add(FhirCommon.addUrnOid(oid));
	}

	/**
	 * adds a patient to the mpi. implements ITI-44 Patient Identity Source –
	 * Add Patient Record
	 *
	 * @param patient
	 *            the patient
	 * @return true, if successful
	 */
	@Override
	public boolean addPatient(FhirPatient patient, SecurityHeaderElement assertion) {
		configurePix(true);
		LOGGER.debug("creating v3RecordAddedMessage");
		boolean ret = false;
		final V3PixSourceMessageHelper v3RecordAddedMessage = new V3PixSourceMessageHelper(true,
				false, false, adapterCfg.getSenderApplicationOid(),
				adapterCfg.getSenderFacilityOid(), adapterCfg.getReceiverApplicationOid(),
				adapterCfg.getReceiverFacilityOid());
		LOGGER.debug("add demographic data");
		addDemographicData(patient, v3RecordAddedMessage);
		try {
			printMessage("sourceAdd(req)",
					v3RecordAddedMessage.getV3RecordAddedMessage().getRequest());
			final V3PixSourceAcknowledgement v3pixack = pixSource
					.sendRecordAdded(v3RecordAddedMessage.getV3RecordAddedMessage());
			printMessage("sourceAdd(res)", v3pixack.getRequest());
			ret = checkResponse(v3pixack);
		} catch (final Exception e) {
			LOGGER.error("addPatient failed", e);
		}
		this.postFixV3Package();
		return ret;
	}

	/**
	 * Adds the patient addresses from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void addPatientAddresses(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		if (!patient.getAddress().isEmpty()) {
			for (final Address address : patient.getAddress()) {

				List<String> addressLines = new ArrayList<>();
				for (StringType addressLine : address.getLine()) {
					addressLines.add(addressLine.getValueAsString());
				}

				String adressOtherDesignation = null;

				String addressType = null;
				if ((address.getUseElement() != null)
						&& (address.getUseElement().getValue() != null)) {
					switch (address.getUseElement().getValue()) {
					case HOME:
						addressType = "H";
						break;
					case WORK:
						addressType = "WP";
						break;
					case TEMP:
						addressType = "TMP";
						break;
					case OLD:
						addressType = "OLD";
						break;
					default:
						break;
					}
				}
				v3PixSourceMessage.addPatientAddress(addressLines, address.getCity(), null,
						address.getState(), address.getCountry(), address.getPostalCode(),
						adressOtherDesignation, addressType);
			}
		}
	}

	/**
	 * Adds the patient addresses from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addPatientAddresses(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getAddr() != null)) {
			for (final AD ad : pdqPatient.getPatientPerson().getAddr()) {
				patient.getAddress().add(getAddressFromAD(ad));
			}
		}
	}

	/**
	 * Adds the patient ids from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void addPatientIds(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		for (final Identifier Identifier : patient.getIdentifier()) {
			if ((Identifier.getSystem().length() > 8)
					&& (Identifier.getSystem().startsWith(FhirCommon.oidUrn))) {
				final String oid = FhirCommon.removeUrnOidPrefix(Identifier.getSystem());
				if (this.otherIdsOidSet.contains(oid)) {
					v3PixSourceMessage.addPatientOtherID(Identifier.getValue(), oid);
				} else {
					if (homeCommunityOid.equals(oid)) {
						v3PixSourceMessage.addPatientID(Identifier.getValue(), homeCommunityOid,
								adapterCfg.getHomeCommunityNamespace());
					} else {
						v3PixSourceMessage.addPatientID(Identifier.getValue(), oid, "");
					}
				}
			}
		}
	}

	/**
	 * Adds the patient ids from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addPatientIds(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if (pdqPatient.getId() != null) {
			for (final II patientId : pdqPatient.getId()) {
				final var Identifier = new Identifier();
				Identifier.setSystem(FhirCommon.addUrnOid(patientId.getRoot()));
				Identifier.setValue(patientId.getExtension());
				patient.getIdentifier().add(Identifier);
			}
		}

		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getAsOtherIDs() != null)) {
			for (final var asOtherId : pdqPatient.getPatientPerson()
					.getAsOtherIDs()) {
				if ((asOtherId.getId() != null) && (!asOtherId.getId().isEmpty())) {
					final II patientId = asOtherId.getId().get(0);
					if (patientId != null) {
						final Identifier Identifier = new Identifier();
						Identifier.setSystem(FhirCommon.addUrnOid(patientId.getRoot()));
						Identifier.setValue(patientId.getExtension());
						patient.getIdentifier().add(Identifier);
					}
				}
			}
		}
	}

	/**
	 * Adds the patient name from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void addPatientName(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		// Name
		final String familyName = patient.getName().get(0).getFamily();
		final String givenName = patient.getName().get(0).getGivenAsSingleString();
		final String otherName = ""; // other is resolved into given in
										// addPatientName
		// below, we have that already with above lines
		final String prefixName = patient.getName().get(0).getPrefixAsSingleString();
		final String suffixName = patient.getName().get(0).getSuffixAsSingleString();
		v3PixSourceMessage.addPatientName(familyName, givenName, otherName, prefixName, suffixName);
	}

	/**
	 * Adds the patient name from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addPatientName(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		final List<PN> pns = pdqPatient.getPatientPerson().getName();
		for (var i = 0; i < pns.size(); ++i) {
			final var pn = pns.get(i);
			final var HumanNameType = new HumanName();
			if (pn.getGiven() != null) {
				for (final EnGiven given : pn.getGiven()) {
					HumanNameType.addGiven(getMixedValue(given.getMixed()));
				}
			}
			if (pn.getFamily() != null) {
				for (final EnFamily family : pn.getFamily()) {
					HumanNameType.setFamily(getMixedValue(family.getMixed()));
				}
			}
			if (pn.getPrefix() != null) {
				for (final EnPrefix prefix : pn.getPrefix()) {
					HumanNameType.addPrefix(getMixedValue(prefix.getMixed()));
				}
			}
			if (pn.getSuffix() != null) {
				for (final EnSuffix suffix : pn.getSuffix()) {
					HumanNameType.addPrefix(getMixedValue(suffix.getMixed()));
				}
			}
			patient.getName().add(HumanNameType);
		}
	}

	/**
	 * Adds the patient telecoms from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void addPatientTelecoms(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		// telecommunication addresses (only phone and email will be added to
		// source)
		if ((patient.getTelecom() != null) && (!patient.getTelecom().isEmpty())) {
			for (final ContactPoint contactPoint : patient.getTelecom()) {
				// system I 0..1 code phone | fax | email | url
				// use M 0..1 code home | work | temp | old | mobile - purpose
				// of this contact point
				var telecomValue = "";
				var useValue = "";

				var system = "";
				var use = "";
				var value = "NULL";

				if (contactPoint.getSystem() != null)
					system = contactPoint.getSystem().toString().toLowerCase();
				if (contactPoint.getUse() != null)
					use = contactPoint.getUse().toString().toLowerCase();
				if (contactPoint.getValue() != null)
					value = contactPoint.getValue();

				if ("phone".equals(system)) {
					telecomValue = "tel:" + value;
					if ("home".equals(use)) {
						useValue = TelecomAddressUse.PRIVATE.getCodeValue();
					}
					if ("work".equals(use)) {
						useValue = TelecomAddressUse.BUSINESS.getCodeValue();
					}
					if ("mobile".equals(use)) {
						// ignore, because MC will produce an
						// java.lang.IllegalArgumentException: Invalid value:
						// 'org.ehealth_connector.communication.mpi.impl.V3PixSourceMessageHelper$1@7528089c'
						// for datatype :TelecommunicationAddressUse
					}
				}
				if ("email".equals(system)) {
					telecomValue = "mailto:" + value;
					if ("home".equals(use)) {
						useValue = TelecomAddressUse.PRIVATE.getCodeValue();
					}
					if ("work".equals(use)) {
						useValue = TelecomAddressUse.BUSINESS.getCodeValue();
					}
				}

				v3PixSourceMessage.addPatientTelecom(telecomValue, useValue);
			}
		}
	}

	/**
	 * Adds the patient telecoms from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void addPatientTelecoms(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getTelecom() != null)
				&& (!pdqPatient.getPatientPerson().getTelecom().isEmpty())) {
			for (final TEL tel : pdqPatient.getPatientPerson().getTelecom()) {
				final var contactPoint = new ContactPoint();
				if ((tel.getValue() != null) && tel.getValue().startsWith("tel:")) {
					contactPoint.setValue(tel.getValue().substring(4));
					contactPoint.setSystem(ContactPointSystem.PHONE);
					if (tel.getUse().contains(WorkPlaceAddressUse.WP.value())) {
						contactPoint.setUse(ContactPointUse.WORK);
					} else if (tel.getUse().contains(HomeAddressUse.H.value())
							|| tel.getUse().contains(HomeAddressUse.HP.value())) {
						contactPoint.setUse(ContactPointUse.HOME);
					} else if ((!tel.getUse().isEmpty())
							&& "MC".equals(tel.getUse())) {
						contactPoint.setUse(ContactPointUse.MOBILE);
					}
					patient.getTelecom().add(contactPoint);
				}
				if ((tel.getValue() != null) && tel.getValue().startsWith("mailto:")) {
					contactPoint.setValue(tel.getValue().substring(7));
					contactPoint.setSystem(ContactPointSystem.EMAIL);
					if (tel.getUse().contains(WorkPlaceAddressUse.WP.value())) {
						contactPoint.setUse(ContactPointUse.WORK);
					} else if (tel.getUse().contains(HomeAddressUse.H.value())
							|| tel.getUse().contains(HomeAddressUse.HP.value())) {
						contactPoint.setUse(ContactPointUse.HOME);
					}
					patient.getTelecom().add(contactPoint);
				}
			}

		}

	}

	/**
	 * Checks the response, error are logged.
	 *
	 * @param response
	 *            the response
	 * @return true, if response has no error, false if there are errors
	 */
	protected boolean checkResponse(V3PixSourceAcknowledgement response) {
		if (response.hasError()) {
			LOGGER.error("AcknowledgementCode: {}", response.getAcknowledgementCode());
			LOGGER.error("Query error text: {}", response.getErrorText());
			return false;
		}
		return true;
	}

	/**
	 * Configures the pdq consumer actor, is automatically called by the
	 * different functions.
	 *
	 * @return true, if successful
	 */
	protected boolean configurePdq() {
		try {
			LOGGER.debug("pdq configure start");
			if (!pdqConsumerConfigured) {
				this.pdqConsumerConfigured = true;
				if (adapterCfg.getAuditSourceId() != null) {
					PDQConsumerAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.getAuditSourceId());
				}
				if (adapterCfg.getAuditEnterpriseSiteId() != null) {
					PDQConsumerAuditor.getAuditor().getConfig()
							.setAuditEnterpriseSiteId(adapterCfg.getAuditEnterpriseSiteId());
				}
				if (adapterCfg.getAuditRepositoryUri() != null) {
					PDQConsumerAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.getAuditRepositoryUri());
				}
				if (v3PdqConsumer == null) {
					v3PdqConsumer = new V3PdqConsumer(adapterCfg.getPdqConsumerUri());
				}
			}
		} catch (final Exception e) {
			LOGGER.error("configuring not successful", e);
			return false;
		}
		LOGGER.debug("configure end");
		return true;
	}

	/**
	 * Configures the pix actor, is automatically called by the different
	 * functions.
	 *
	 * @param source
	 *            true if source actor, false for consumer
	 * @return true, if successful
	 */
	protected boolean configurePix(boolean source) {
		try {
			LOGGER.debug("pix configure start");
			if (source && !pixSourceConfigured) {
				this.pixSourceConfigured = true;
				if (adapterCfg.getAuditSourceId() != null) {
					PIXSourceAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.getAuditSourceId());
				}
				if (adapterCfg.getAuditEnterpriseSiteId() != null) {
					PIXSourceAuditor.getAuditor().getConfig()
							.setAuditEnterpriseSiteId(adapterCfg.getAuditEnterpriseSiteId());
				}
				if (adapterCfg.getAuditRepositoryUri() != null) {
					PIXSourceAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.getAuditRepositoryUri());
				}
				if (pixSource == null) {
					pixSource = new V3PixSource(adapterCfg.getPixSourceUri());
				}
			}
			if (!source && !pixConsumerConfigured) {
				this.pixConsumerConfigured = true;
				if (adapterCfg.getAuditSourceId() != null) {
					PIXConsumerAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.getAuditSourceId());
				}
				if (adapterCfg.getAuditEnterpriseSiteId() != null) {
					PIXConsumerAuditor.getAuditor().getConfig()
							.setAuditEnterpriseSiteId(adapterCfg.getAuditEnterpriseSiteId());
				}
				if (adapterCfg.getAuditRepositoryUri() != null) {
					PIXConsumerAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.getAuditRepositoryUri());
				}
				if (v3PixConsumer == null) {
					v3PixConsumer = new V3PixConsumer(adapterCfg.getPixQueryUri());
				}

			}
		} catch (final Exception e) {
			LOGGER.error("configuring not successful", e);
			return false;
		}
		LOGGER.debug("configure end");
		return true;
	}

	/**
	 * Helper function to convert the HL7 PDQ AD type to the coressponding FHIR
	 * type
	 *
	 * @param ad
	 *            the ad
	 * @return the address dt from ad
	 */
	protected org.hl7.fhir.dstu3.model.Address getAddressFromAD(AD ad) {
		final var address = new org.hl7.fhir.dstu3.model.Address();
		if (ad.getUse() != null) {
			if ("H".equals(ad.getUse())) {
				address.setUse(AddressUse.HOME);
			}
			if ("WP".equals(ad.getUse())) {
				address.setUse(AddressUse.WORK);
			}
			if ("TMP".equals(ad.getUse())) {
				address.setUse(AddressUse.TEMP);
			}
			if ("OLD".equals(ad.getUse())) {
				address.setUse(AddressUse.OLD);
			}
		}
		if ((ad.getStreetAddressLine() != null) && (!ad.getStreetAddressLine().isEmpty())) {
			for (final AdxpStreetAddressLine addressStreetLine : ad.getStreetAddressLine()) {
				address.addLine(getMixedValue(addressStreetLine.getMixed()));
			}
		}
		if ((ad.getCity() != null) && (!ad.getCity().isEmpty())) {
			address.setCity(getMixedValue(ad.getCity().get(0).getMixed()));
		}
		if ((ad.getState() != null) && (!ad.getState().isEmpty())) {
			address.setState(getMixedValue(ad.getState().get(0).getMixed()));
		}
		if ((ad.getPostalCode() != null) && (!ad.getPostalCode().isEmpty())) {
			address.setPostalCode(getMixedValue(ad.getPostalCode().get(0).getMixed()));
		}
		if ((ad.getCountry() != null) && (!ad.getCountry().isEmpty())) {
			address.setCountry(getMixedValue(ad.getCountry().get(0).getMixed()));
		}
		return address;
	}

	/**
	 * Gets the home community patient id.
	 *
	 * @param patient
	 *            the patient
	 * @return the home community patient id
	 */
	protected String getHomeCommunityPatientId(FhirPatient patient) {
		for (final Identifier Identifier : patient.getIdentifier()) {
			if (Identifier.getSystem().startsWith(FhirCommon.oidUrn)) {
				if (FhirCommon.removeUrnOidPrefix(Identifier.getSystem())
						.equals(this.homeCommunityOid)) {
					return Identifier.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Helper method which gets the value of the supplied FeatureMap
	 *
	 * @param mixed
	 *            (the FeatureMap containing the value)
	 * @return String containing the value of the supplied FeatureMap.
	 */
	protected String getMixedValue(List<Serializable> mixed) {
		var returnValue = "";
		// if we have a mixed
		if (!mixed.isEmpty() && mixed.get(0) instanceof String) {
			returnValue = (String) mixed.get(0);
		}
		return returnValue;
	}

	/**
	 * Returns a confgure V3PdqQuery Object which can be used to perfom a query
	 *
	 * @return the mpi query
	 */
	@Override
	public V3PdqQuery getMpiQuery() {
		return new V3PdqQuery(adapterCfg.getSenderApplicationOid(),
				adapterCfg.getSenderFacilityOid(), adapterCfg.getReceiverApplicationOid(),
				adapterCfg.getReceiverFacilityOid());
	}

	/**
	 * Get the specified patient object.
	 *
	 * @param v3PdqConsumerResponse
	 *            the consumer response
	 * @param patientIndex
	 *            the patient index
	 * @return PRPAMT201310UV02Patient - the patient object at the specified
	 *         index.
	 */
	protected PRPAMT201310UV02Patient getPatientByIndex(V3PdqConsumerResponse v3PdqConsumerResponse,
			int patientIndex) {
		return v3PdqConsumerResponse.getPdqResponse().getControlActProcess().getSubject()
				.get(patientIndex).getRegistrationEvent().getSubject1().getPatient();
	}

	/**
	 * Gets the patient domain id.
	 *
	 * @param v3PixConsumerResponse
	 *            the v3 pix consumer response
	 * @param rootOid
	 *            the root oid
	 * @return the patient domain id
	 */
	public String getPatientDomainId(V3PixConsumerResponse v3PixConsumerResponse, String rootOid) {
		String retVal = null;
		if ((rootOid != null) && (v3PixConsumerResponse != null)
				&& ((v3PixConsumerResponse.getNumPatientIds() > 0)
						|| (v3PixConsumerResponse.getNumAsOtherIds() > 0))) {

			for (var i = 0; i < v3PixConsumerResponse.getNumPatientIds(); i++) {
				final String[] id = v3PixConsumerResponse.getPatientID(i);
				if ((id[2] != null) && id[2].equals(rootOid)) {
					retVal = id[0];
				}
			}
			if (retVal == null) {
				for (var i = 0; i < v3PixConsumerResponse.getNumAsOtherIds(); i++) {
					final String[] id = v3PixConsumerResponse.getPatientAsOtherID(i);
					if ((id[2] != null) && id[2].equals(rootOid)) {
						retVal = id[0];
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * Gets the patients from pdq query.
	 *
	 * @param response
	 *            the response
	 * @return the patients from pdq query
	 */
	public List<FhirPatient> getPatientsFromPdqQuery(V3PdqConsumerResponse response) {
		var success = false;
		if (response != null) {
			success = !response.hasError();
			if (success) {
				final List<FhirPatient> listFhirPatients = new ArrayList<>(
						response.getNumRecordsCurrent());
				for (var i = 0; i < response.getNumRecordsCurrent(); ++i) {
					final var fhirPatient = new FhirPatient();
					addDemographicData(getPatientByIndex(response, i), fhirPatient);
					listFhirPatients.add(fhirPatient);
				}
				return listFhirPatients;
			}
		}
		return null;
	}

	/**
	 * Merge patient. implements ITI-44 Patient Identity Source – Patient
	 * Identity Merge
	 *
	 * Patient Registry Duplicates Resolved message indicates that the Patient
	 * Identity Source has done a merge within a specific Patient Identification
	 * Domain. That is, the surviving identifier (patient ID) has subsumed a
	 * duplicate patient identifier.
	 *
	 * @param patient
	 *            the patient (with the surviving identifier)
	 * @param obsoleteId
	 *            the obsolete id (duplicate patient identifier)
	 * @return true, if successful
	 */
	@Override
	public boolean mergePatient(FhirPatient patient, String obsoleteId, SecurityHeaderElement assertion) {

		if (!configurePix(true)) {
			return false;
		}

		var ret = false;

		final var v3pixSourceMsgMerge = new V3PixSourceMessageHelper(false,
				false, true, adapterCfg.getSenderApplicationOid(),
				adapterCfg.getSenderFacilityOid(), adapterCfg.getReceiverApplicationOid(),
				adapterCfg.getReceiverFacilityOid());
		addDemographicData(patient, v3pixSourceMsgMerge);

		v3pixSourceMsgMerge.getV3MergePatientsMessage().setObsoletePatientID(obsoleteId,
				this.homeCommunityOid, this.adapterCfg.getHomeCommunityNamespace());
		try {
			printMessage("sourceMerge(req)",
					v3pixSourceMsgMerge.getV3MergePatientsMessage().getRequest());
			final V3PixSourceAcknowledgement v3pixack = pixSource
					.sendMergePatients(v3pixSourceMsgMerge.getV3MergePatientsMessage());
			printMessage("sourceMerge(res)", v3pixack.getRequest());
			ret = checkResponse(v3pixack);
		} catch (final Exception e) {
			log.error("mergePatient failed", e);
		}
		postFixV3Package();
		return ret;
	}

	/**
	 * Logs a debug message.
	 *
	 * @param test
	 *            will be prefixed to the log message
	 * @param element
	 *            the xml element serialized to be logged
	 */
	protected void printMessage(String test, Element element) {

		try {
			// use a transformer to improve the output of the xml
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			// initialize StreamResult with File object to save to file
			final StreamResult result = new StreamResult(new StringWriter());
			final DOMSource source = new DOMSource(element);
			transformer.transform(source, result);

			final String xmlString = result.getWriter().toString();

			LOGGER.debug(test + "\n" + xmlString);
		} catch (final Exception e) {
			LOGGER.debug(test + " problem encountered in printMessage");
		}
	}

	/**
	 * Query patient id.
	 *
	 * @param patient
	 *            the patient
	 * @return the string
	 */
	public String queryPatientId(FhirPatient patient) {
		return queryPatientId(patient, null, null)[0];
	}

	private PRPAIN201306UV02Type sendITI47Query(V3PdqConsumerQuery request, SecurityHeaderElement assertion,
			URI pdqDest, boolean continuation)
			throws Exception {
		final var endpoint = String.format(
				"pdqv3-iti47://%s?inInterceptors=#serverInLogger&inFaultInterceptors=#serverInLogger&outInterceptors=#serverOutLogger&outFaultInterceptors=#serverOutLogger&secure=%s&supportContinuation=%s",
				pdqDest.toString().replace("https://", ""), true, continuation);
		LOGGER.info("Sending request to '{}' endpoint", endpoint);

		final var marshaller = JAXBContext.newInstance(PRPAIN201305UV02Type.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
		final var stringWriter = new StringWriter();
		marshaller.marshal(request.getRootElement(), stringWriter);

		System.out.println(stringWriter.toString());

		final var exchange = send(endpoint, stringWriter.toString(), assertion, "urn:hl7-org:v3:PRPA_IN201305UV02");

		var xml = exchange.getMessage().getBody(String.class);

		final var unmarshaller = JAXBContext.newInstance(PRPAIN201306UV02Type.class).createUnmarshaller();
		return (PRPAIN201306UV02Type) unmarshaller
				.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * query the mpi with patient id and return the ids in the queried domains
	 * from the mpi.
	 *
	 * Implements ITI-45 Patient Identifier Cross-reference Consumer Queries the
	 * Patient Identifier Cross-reference Manager for a list of corresponding
	 * patientidentifiers, if any
	 *
	 * @param patient
	 *            the patient
	 * @param queryDomainOids
	 *            the query domain oids
	 * @param queryDomainNamespaces
	 *            the query domain namespaces
	 * @return the string[]
	 */
	@Override
	public String[] queryPatientId(org.ehealth_connector.fhir.structures.gen.FhirPatient patient,
			String[] queryDomainOids, String[] queryDomainNamespaces, SecurityHeaderElement assertion) {

		if (!configurePix(false)) {
			return null;
		}
		String[] domainToReturnOids = null;
		String[] domainToReturnNamespaces = null;

		if (queryDomainOids != null) {
			domainToReturnOids = queryDomainOids;
		} else {
			if (adapterCfg.getDomainToReturnOid() != null) {
				domainToReturnOids = new String[1];
				domainToReturnOids[0] = adapterCfg.getDomainToReturnOid();
			}
		}
		if (queryDomainNamespaces != null) {
			domainToReturnNamespaces = queryDomainNamespaces;
		} else if (adapterCfg.getDomainToReturnNamespace() != null) {
			domainToReturnNamespaces = new String[1];
			domainToReturnNamespaces[0] = adapterCfg.getDomainToReturnNamespace();
		}

		final V3PixConsumerQuery v3PixConsumerQuery = new V3PixConsumerQuery(
				adapterCfg.getSenderApplicationOid(), adapterCfg.getSenderFacilityOid(),
				adapterCfg.getReceiverApplicationOid(), adapterCfg.getReceiverFacilityOid());

		// add the patient identifier
		final String homeCommunityPatientId = this.getHomeCommunityPatientId(patient);
		if (homeCommunityPatientId != null) {
			v3PixConsumerQuery.addPatientIdToQuery(homeCommunityPatientId, homeCommunityOid,
					adapterCfg.getHomeCommunityNamespace());

			if (domainToReturnOids != null) {
				for (int i = 0; i < domainToReturnOids.length; ++i) {
					final String domainToReturnOid = domainToReturnOids[i];
					String domainToReturnNamespace = null;
					if ((domainToReturnNamespaces != null)
							&& (i < domainToReturnNamespaces.length)) {
						domainToReturnNamespace = domainToReturnNamespaces[i];
					}
					v3PixConsumerQuery.addDomainToReturn(domainToReturnOid,
							domainToReturnNamespace);
				}
			}
			V3PixConsumerResponse v3PixConsumerResponse = null;
			try {
				printMessage("sourceQuery(req)", v3PixConsumerQuery.getRequest());
				v3PixConsumerResponse = v3PixConsumer.sendQuery(v3PixConsumerQuery);
				printMessage("sourceQuery(res)", v3PixConsumerResponse.getRequest());
				if (domainToReturnOids != null) {
					final String[] returnIds = new String[domainToReturnOids.length];
					for (int i = 0; i < returnIds.length; ++i) {
						returnIds[i] = getPatientDomainId(v3PixConsumerResponse,
								domainToReturnOids[i]);
					}
					postFixV3Package();
					return returnIds;
				}
				postFixV3Package();
				return null;
			} catch (final Exception e) {
				log.error("exception queryPatient", e);
				postFixV3Package();
				return null;
			}
		} else {
			log.error("homeCommunityPatientId not provided");
			postFixV3Package();
			return null;
		}
	}

	/**
	 * Perfoms a PDQ Query (ITI-47)
	 *
	 * @param mpiQuery the mpi query object
	 * @return the v3 pdq query response
	 */
	@Override
	public V3PdqQueryResponse queryPatients(V3PdqQuery mpiQuery, SecurityHeaderElement assertion) {
		final var queryResponse = new V3PdqQueryResponse();
		if (!configurePdq()) {
			return queryResponse;
		}
		/** The last pdq consumer response. */

		try {
			if (!mpiQuery.doCancelQuery()) {
				if (!mpiQuery.doContinueQuery()) {
					try {
						PRPAIN201306UV02Type lastPdqConsumerResponse = sendITI47Query(mpiQuery.getV3PdqConsumerQuery(),
								assertion,
								adapterCfg.getPdqConsumerUri(), false);
						var response = new V3PdqConsumerResponse(lastPdqConsumerResponse);
						queryResponse.setPatients(getPatientsFromPdqQuery(response));
						queryResponse.setSuccess(!response.hasError());
						queryResponse.setCurrentNumbers(response.getNumRecordsCurrent());
						queryResponse.setRemainingNumbers(response.getNumRecordsRemaining());
						queryResponse.setInfoCodes(List.of(response.acknowledgementDetailCode));
						queryResponse.setInfoTexts(List.of(response.acknowledgementDetailText));
						queryResponse.setErrorText(response.errorText);
						final INT totalNumbers = response.getPdqResponse().getControlActProcess().getQueryAck()
								.getResultTotalQuantity();
						if (totalNumbers != null) {
							queryResponse.setTotalNumbers(totalNumbers.getValue().intValue());
						}
					} catch (final Exception e) {
						LOGGER.error("queryPatient failed", e);
						queryResponse.setSuccess(false);
						return queryResponse;
					}
				} else {
					lastPdqConsumerResponse = mpiQuery.getLastPdqConsumerResponse();
					final V3PdqContinuationQuery continuationQuery = new V3PdqContinuationQuery(
							mpiQuery.getV3PdqConsumerQuery().getSendingApplication(),
							mpiQuery.getV3PdqConsumerQuery().getSendingFacility(),
							mpiQuery.getV3PdqConsumerQuery().getReceivingApplication(0),
							mpiQuery.getV3PdqConsumerQuery().getReceivingFacility(0),
							lastPdqConsumerResponse, mpiQuery.getPageCount());
					continuationQuery.setProcessingCode("T");
					printMessage("consumerQuery(req,continue)", continuationQuery.getRequest());
					lastPdqConsumerResponse = v3PdqConsumer.sendContinuation(continuationQuery);
					printMessage("consumerQuery(res,continue)",
							lastPdqConsumerResponse.getRequest());
				}
				queryResponse.setPatients(getPatientsFromPdqQuery(lastPdqConsumerResponse));
				queryResponse.setSuccess(!lastPdqConsumerResponse.hasError());
				queryResponse.setCurrentNumbers(lastPdqConsumerResponse.getNumRecordsCurrent());
				queryResponse.setRemainingNumbers(lastPdqConsumerResponse.getNumRecordsRemaining());
				final INT totalNumbers = lastPdqConsumerResponse.getPdqResponse()
						.getControlActProcess().getQueryAck().getResultTotalQuantity();
				if (totalNumbers != null) {
					queryResponse.setTotalNumbers(totalNumbers.getValue().intValue());
				}
				mpiQuery.setLastPdqConsumerResponse(lastPdqConsumerResponse);
			} else {
				final V3PdqConsumerResponse lastPdqConsumerResponse = mpiQuery
						.getLastPdqConsumerResponse();
				final V3PdqContinuationCancel continuationCancel = new V3PdqContinuationCancel(
						mpiQuery.getV3PdqConsumerQuery().getSendingApplication(),
						mpiQuery.getV3PdqConsumerQuery().getSendingFacility(),
						mpiQuery.getV3PdqConsumerQuery().getReceivingApplication(0),
						mpiQuery.getV3PdqConsumerQuery().getReceivingFacility(0),
						lastPdqConsumerResponse);
				printMessage("consumerQuery(req,cancel)", continuationCancel.getRequest());
				final V3GenericAcknowledgement v3Ack = v3PdqConsumer.sendCancel(continuationCancel);
				printMessage("consumerQuery(res,cancel)", v3Ack.getRequest());
				queryResponse.setSuccess(!v3Ack.hasError());
			}
		} catch (final Exception e) {
			log.error("queryPatient failed", e);
			queryResponse.setSuccess(false);
			return queryResponse;
		}
		this.postFixV3Package();
		return queryResponse;
	}

	/**
	 * Sets the birthplace from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setBirthPlace(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		if (patient.getBirthPlace() != null) {
			final var fhirAddress = patient.getBirthPlace();
			String adressOtherDesignation = null;
			if (fhirAddress.getLine().size() > 1) {
				adressOtherDesignation = fhirAddress.getLine().get(1).getValueAsString();
			}
			final var patientAddress = PixPdqV3Utils.createAD(
					fhirAddress.getLine().get(0).getValue(), fhirAddress.getCity(), null,
					fhirAddress.getState(), fhirAddress.getCountry(), fhirAddress.getPostalCode(),
					adressOtherDesignation, null);
			v3PixSourceMessage.setPatientBirthPlace(patientAddress);
		}
	}

	/**
	 * Sets the birth place from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setBirthPlace(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getBirthPlace() != null)) {
			final PRPAMT201310UV02BirthPlace birthplace = pdqPatient.getPatientPerson()
					.getBirthPlace();
			final var addr = birthplace.getAddr();
			if (addr != null) {
				patient.setBirthPlace(getAddressFromAD(addr));
			}
		}
	}

	/**
	 * sets the deceased status, either boolean or by datetime from the patient
	 * to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setDeceased(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		final Type idDeceased = patient.getDeceased();
		if (idDeceased instanceof DateTimeType) {
			final DateTimeType deceased = (DateTimeType) idDeceased;
			if (deceased.getValue() != null) {
				final var dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				v3PixSourceMessage.setPatientDeceasedTime(dateFormat.format(deceased.getValue()));
				v3PixSourceMessage.setPatientDeceased(true);
			}
		}
		if (idDeceased instanceof BooleanType) {
			final BooleanType deceased = (BooleanType) idDeceased;
			if (deceased.getValue() != null) {
				v3PixSourceMessage.setPatientDeceased(deceased.getValue());
			}
		}
	}

	/**
	 * Sets the deceased status from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setDeceased(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if (pdqPatient.getPatientPerson() != null) {
			if ((pdqPatient.getPatientPerson().getDeceasedInd() != null)
					&& pdqPatient.getPatientPerson().getDeceasedInd().value) {
				final BooleanType dt = new BooleanType();
				dt.setValue(pdqPatient.getPatientPerson().getDeceasedInd().value);
				patient.setDeceased(dt);
			}
			if (pdqPatient.getPatientPerson().getDeceasedTime() != null) {
				final String deceasedTime = pdqPatient.getPatientPerson().getDeceasedTime()
						.getValue();
				patient.setDeceased(new DateTimeType(deceasedTime));
			}
		}
	}

	/**
	 * Sets the employee from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setEmployee(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		if ((patient.getEmployeeOccupation() != null)
				&& !patient.getEmployeeOccupation().isEmpty()) {
			v3PixSourceMessage.addEmployeeCode(patient.getEmployeeOccupation().getText());
		}
	}

	/**
	 * Sets the employee from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setEmployee(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient != null) && (pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getAsEmployee() != null)) {
			final List<PRPAMT201310UV02Employee> employees = pdqPatient.getPatientPerson()
					.getAsEmployee();
			if (!employees.isEmpty()) {
				final PRPAMT201310UV02Employee employee = employees.get(0);
				if ((employee.getOccupationCode() != null)
						&& (employee.getOccupationCode().getCode() != null)) {
					final var employeeOccupationCode = new CodeableConcept();
					employeeOccupationCode.setText(employee.getOccupationCode().getCode());
					patient.setEmployeeOccupation(employeeOccupationCode);
				}
			}
		}
	}

	/**
	 * Set the patient Birth Order from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setMultipeBirth(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		final Type iMultipleBirth = patient.getMultipleBirth();
		if (iMultipleBirth instanceof IntegerType) {
			final IntegerType multipleBirth = (IntegerType) iMultipleBirth;
			if (multipleBirth.getValue() != null) {
				v3PixSourceMessage.setPatientMultipleBirthOrderNumber(multipleBirth.getValue());
				v3PixSourceMessage.setPatientMultipleBirthIndicator(true);
			}
		}
		if (iMultipleBirth instanceof BooleanType) {
			final BooleanType multipleBirth = (BooleanType) iMultipleBirth;
			if (multipleBirth.getValue() != null) {
				v3PixSourceMessage.setPatientMultipleBirthIndicator(multipleBirth.getValue());
			}
		}
	}

	/**
	 * Sets the multipe birth from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setMultipeBirth(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if (pdqPatient.getPatientPerson() != null) {
			if ((pdqPatient.getPatientPerson().getMultipleBirthInd() != null)
					&& pdqPatient.getPatientPerson().getMultipleBirthInd().value) {
				final BooleanType dt = new BooleanType();
				dt.setValue(pdqPatient.getPatientPerson().getMultipleBirthInd().value);
				patient.setMultipleBirth(dt);
			}
			if (pdqPatient.getPatientPerson().getMultipleBirthOrderNumber() != null) {
				final Integer birthOrderNumber = pdqPatient.getPatientPerson()
						.getMultipleBirthOrderNumber().getValue();
				patient.setMultipleBirth(new IntegerType(birthOrderNumber.intValue()));
			}
		}
	}

	/**
	 * Sets the nation from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setNation(FhirPatient patient, V3PixSourceMessageHelper v3PixSourceMessage) {
		if ((patient.getNation() != null) && !patient.getNation().isEmpty()) {
			v3PixSourceMessage.addPatientNation(patient.getNation().getText());
		}
	}

	/**
	 * Sets the nation from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setNation(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getAsCitizen() != null)) {
			final List<PRPAMT201310UV02Citizen> citizens = pdqPatient.getPatientPerson()
					.getAsCitizen();
			if (!citizens.isEmpty()) {
				final PRPAMT201310UV02Nation nation = citizens.get(0).getPoliticalNation();
				if ((nation != null) && (nation.getCode() != null)
						&& (nation.getCode().getCode() != null)) {
					final var nationCode = new CodeableConcept();
					nationCode.setText(nation.getCode().getCode());
					patient.setNation(nationCode);
				}
			}
		}
	}

	/**
	 * Sets the patient birth time from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setPatientBirthTime(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		v3PixSourceMessage.setPatientBirthTime(
				patient.getBirthDateElement().getValueAsString().replace("-", ""));
	}

	/**
	 * Sets the patient birth time from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setPatientBirthTime(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getBirthTime() != null)) {
			final var ts = pdqPatient.getPatientPerson().getBirthTime();
			final String date = ts.getValue();
			if ((date != null) && (date.length() >= 4)) {
				var dateFhir = date.substring(0, 4);
				if (date.length() >= 6) {
					dateFhir += "-" + date.substring(4, 6);
				}
				if (date.length() >= 8) {
					dateFhir += "-" + date.substring(6, 8);
				}
				patient.getBirthDateElement().setValueAsString(dateFhir);
			}
		}

	}

	/**
	 * Sets the patient gender from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setPatientGender(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		// Gender
		if (patient.getGender() != null) {
			var gender = "";
			if (patient.getGender() == AdministrativeGender.MALE) {
				gender = "M";
			} else if (patient.getGender() == AdministrativeGender.FEMALE) {
				gender = "F";
			} else {
				gender = "U";
			}
			v3PixSourceMessage.setPatientGender(gender);
		}
	}

	/**
	 * Sets the patient gender from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setPatientGender(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getAdministrativeGenderCode() != null)) {
			final CE gender = pdqPatient.getPatientPerson().getAdministrativeGenderCode();
			if ("M".equals(gender.getCode())) {
				patient.setGender(AdministrativeGender.MALE);
			} else if ("F".equals(gender.getCode())) {
				patient.setGender(AdministrativeGender.FEMALE);
			} else if ("U".equals(gender.getCode())) {
				patient.setGender(AdministrativeGender.OTHER);
			}
		}
	}

	/**
	 * Adds the marital status from the patient to the pix message. To verify in
	 * an implementation: is the coding of marital status of fhir equivalent to
	 * HL7 V3
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setPatientMaritalStatus(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		if (!patient.getMaritalStatus().isEmpty()) {
			v3PixSourceMessage.setPatientMaritalStatus(
					patient.getMaritalStatus().getCodingFirstRep().getCode());
		}
	}

	/**
	 * Sets the patient marital status the pdq message to the patient. To verify
	 * in an implementation: is the coding of marital status of fhir equivalent
	 * to HL7 V3? http://hl7.org/implement/standards/FHIR-Develop/valueset
	 * -marital-status.html
	 *
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setPatientMaritalStatus(PRPAMT201310UV02Patient pdqPatient,
			FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getMaritalStatusCode() != null)) {
			final CE maritalStatusCode = pdqPatient.getPatientPerson().getMaritalStatusCode();
			if (maritalStatusCode.getCode() != null) {
				final var maritalStatus = new CodeableConcept();
				maritalStatus.setText(maritalStatusCode.getCode());
				patient.setMaritalStatus(maritalStatus);
			}
		}
	}

	/**
	 * Sets the patients mother maiden name from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setPatientMothersMaidenName(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		final HumanName maidenName = patient.getMothersMaidenName();
		if (maidenName.isEmpty()) {
			final String familyName = maidenName.getFamily();
			final var givenName = maidenName.getGivenAsSingleString();
			final var otherName = ""; // other is resolved into given in
			// addPatientName
			final var prefixName = maidenName.getPrefixAsSingleString();
			final var suffixName = maidenName.getSuffixAsSingleString();
			v3PixSourceMessage.setPatientMothersMaidenName(familyName, givenName, otherName,
					suffixName, prefixName);
		}
	}

	/**
	 * Sets the patient mothers maiden name from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setPatientMothersMaidenName(PRPAMT201310UV02Patient pdqPatient,
			FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getPersonalRelationship() != null)) {
			for (final PRPAMT201310UV02PersonalRelationship personalRelationship : pdqPatient
					.getPatientPerson().getPersonalRelationship()) {
				if ((personalRelationship.getCode() != null)
						&& "MTH".equals(personalRelationship.getCode().getCode())
						&& "2.16.840.1.113883.5.111"
								.equals(personalRelationship.getCode().getCodeSystem())) {
					final COCTMT030007UVPerson motherRelationShipHolder = personalRelationship
							.getRelationshipHolder1();
					if ((motherRelationShipHolder != null)
							&& (motherRelationShipHolder.getName() != null)) {
						final List<EN> names = motherRelationShipHolder.getName();
						if ((names != null) && (!names.isEmpty())) {
							// ITI 2b Rev. 11.0 Final Text – 2014-09-23
							// MothersMaidenName Parameter (approx 6645)
							// This optional parameter specifies the maiden name
							// of the mother of the person whose
							// information is being queried. For this parameter
							// item, a single person name (PN) data item shall
							// be specified in the Person.value attribute.
							final EN pn = names.get(0);
							final var HumanNameType = new HumanName();
							if (pn.getGiven() != null) {
								for (final EnGiven given : pn.getGiven()) {
									HumanNameType.addGiven(getMixedValue(given.getMixed()));
								}
							}
							if (pn.getFamily() != null) {
								for (final EnFamily family : pn.getFamily()) {
									HumanNameType.setFamily(getMixedValue(family.getMixed()));
								}
							}
							if (pn.getPrefix() != null) {
								for (final EnPrefix prefix : pn.getPrefix()) {
									HumanNameType.addPrefix(getMixedValue(prefix.getMixed()));
								}
							}
							if (pn.getSuffix() != null) {
								for (final EnSuffix suffix : pn.getSuffix()) {
									HumanNameType.addPrefix(getMixedValue(suffix.getMixed()));
								}
							}
							patient.setMothersMaidenName(HumanNameType);
						}
					}
				}
			}
		}
	}

	/**
	 * Sets the patient religious affiliation from the patient to the pix
	 * message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setPatientReligiousAffiliation(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		if ((patient.getReligiousAffiliation() != null)
				&& !patient.getReligiousAffiliation().isEmpty()) {
			v3PixSourceMessage
					.setPatientReligiousAffiliation(patient.getReligiousAffiliation().getText());
		}
	}

	/**
	 * Sets the patient religious affiliation from the pdq message to the
	 * patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setPatientReligiousAffiliation(PRPAMT201310UV02Patient pdqPatient,
			FhirPatient patient) {
		if ((pdqPatient.getPatientPerson() != null)
				&& (pdqPatient.getPatientPerson().getReligiousAffiliationCode() != null)) {
			final CE religiousAffiliation = pdqPatient.getPatientPerson()
					.getReligiousAffiliationCode();
			if (religiousAffiliation.getCode() != null) {
				final var religion = new CodeableConcept();
				religion.setText(religiousAffiliation.getCode());
				patient.setReligiousAffiliation(religion);
			}
		}
	}

	/**
	 * Sets the scoping organization from the patient to the pix message.
	 *
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 pix source message
	 */
	protected void setScopingOrganization(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
		// scoping organization set the scoping organization
		var organizationOid = "";
		var organizationName = "";
		var organizationTelecomValue = "NOTPROVIDED";

		final var organization = (Organization) patient.getManagingOrganization()
				.getResource();

		if ((organization != null) && (!organization.getIdentifier().isEmpty())) {
			final Identifier organizationId = organization.getIdentifier().get(0);
			if (organizationId.getSystem().startsWith(FhirCommon.oidUrn)) {
				organizationOid = FhirCommon.removeUrnOidPrefix(organizationId.getSystem());
			}
		}

		if ((organization != null) && (organization.getName() != null)) {
			organizationName = organization.getName();
		}

		if ((organization != null) && (!organization.getTelecom().isEmpty())) {
			if (!organization.getTelecom().isEmpty()) {
				final var contactPoint = organization.getTelecomFirstRep();
				if (contactPoint != null) {
					organizationTelecomValue = contactPoint.getValue();
				}
			}
		}

		v3PixSourceMessage.setScopingOrganization(organizationOid, organizationName,
				organizationTelecomValue);
	}

	/**
	 * Sets the scoping organization from the pdq message to the patient.
	 *
	 * @param pdqPatient
	 *            the pdq patient
	 * @param patient
	 *            the patient
	 */
	protected void setScopingOrganization(PRPAMT201310UV02Patient pdqPatient, FhirPatient patient) {
		if (pdqPatient.getProviderOrganization() != null) {
			final var organization = new Organization();
			patient.getManagingOrganization().setResource(organization);

			if ((pdqPatient.getProviderOrganization().getId() != null)
					&& (!pdqPatient.getProviderOrganization().getId().isEmpty())) {
				for (final II id : pdqPatient.getProviderOrganization().getId()) {
					final var Identifier = new Identifier();
					Identifier.setValue(FhirCommon.addUrnOid(id.getRoot()));
					organization.getIdentifier().add(Identifier);
				}
			}

			final List<ON> ons = pdqPatient.getProviderOrganization().getName();
			if ((ons != null) && (!ons.isEmpty())) {
				organization.setName(getMixedValue(ons.get(0).getMixed()));
			}

			final List<COCTMT150003UV03ContactParty> contactParties = pdqPatient
					.getProviderOrganization().getContactParty();
			if ((contactParties != null) && (!contactParties.isEmpty())) {
				final List<TEL> tels = contactParties.get(0).getTelecom();
				if ((tels != null) && (!tels.isEmpty())) {
					final var tel = tels.get(0);
					if ((tel.getValue() != null) && tel.getValue().startsWith("tel:")) {
						final var ContactPoint = new ContactPoint();
						ContactPoint.setValue(tel.getValue().substring(4));
						ContactPoint.setSystem(ContactPointSystem.PHONE);
						organization.getTelecom().add(ContactPoint);
					}
				}
			}
		}

	}

	/**
	 * updates the demographic information of the patient in the mpi.
	 *
	 * implements ITI-44 Patient Identity Source – Revise Patient Record updates
	 * the demographic information of the patient in the mpi.
	 *
	 * @param patient
	 *            the patient
	 * @return true, if successful
	 */
	@Override
	public boolean updatePatient(FhirPatient patient, SecurityHeaderElement assertion) {
		if (pixSource == null) {
			pixSource = new V3PixSource(adapterCfg.getPixSourceUri());
		}
		final V3PixSourceMessageHelper v3RecordRevisedMessage = new V3PixSourceMessageHelper(false,
				true, false, adapterCfg.getSenderApplicationOid(),
				adapterCfg.getSenderFacilityOid(), adapterCfg.getReceiverApplicationOid(),
				adapterCfg.getReceiverFacilityOid());
		addDemographicData(patient, v3RecordRevisedMessage);
		try {
			printMessage("sourceUpdate(req)",
					v3RecordRevisedMessage.getV3RecordRevisedMessage().getRequest());
			final V3PixSourceAcknowledgement v3pixack = pixSource
					.sendRecordRevised(v3RecordRevisedMessage.getV3RecordRevisedMessage());
			printMessage("sourceUpdate(res)", v3pixack.getRequest());
			return checkResponse(v3pixack);
		} catch (final Exception e) {
			log.error("updatePatient failed", e);
			return false;
		}
	}

}
