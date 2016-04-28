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
 * Year of publication: 2015
 *
 *******************************************************************************/
package org.ehealth_connector.fhir;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.ehealth_connector.common.Address;
import org.ehealth_connector.common.Identificator;
import org.ehealth_connector.common.Name;
import org.ehealth_connector.common.Patient;
import org.ehealth_connector.common.Telecoms;
import org.ehealth_connector.common.enums.AddressUse;
import org.ehealth_connector.common.enums.AdministrativeGender;
import org.ehealth_connector.common.utils.Util;
import org.openhealthtools.mdht.uml.cda.Birthplace;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.LanguageCommunication;
import org.openhealthtools.mdht.uml.cda.Organization;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Place;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.ADXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.ENXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.vocab.TelecommunicationAddressUse;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * The Class FhirPatient. FHIRPatient extends from the FHIR HAPI Patient
 * Resource and provides convenience methods to translate between the
 * convenience patient in the ehealthconeector and back
 *
 * @see "http://jamesagnew.github.io/hapi-fhir/index.html"
 */
@ResourceDef(name = "Patient")
public class FhirPatient extends ca.uhn.fhir.model.dstu2.resource.Patient {

	/**
	 *
	 */
	private static final long serialVersionUID = -1520681931095452610L;

	/**
	 * converts the mdht AD to the fhir address
	 *
	 * @param address
	 *            address object
	 * @return fhir address
	 */
	@SuppressWarnings("incomplete-switch")
	static public AddressDt convertAddress(AD address) {
		if (address == null) {
			return null;
		}
		final AddressDt addressDt = new AddressDt();
		if (address.getStreetAddressLines() != null) {
			for (final ADXP street : address.getStreetAddressLines()) {
				addressDt.addLine().setValue(street.getText());
			}
		}
		if ((address.getCities() != null) && (address.getCities().size() > 0)) {
			addressDt.setCity(address.getCities().get(0).getText());
		}
		if ((address.getPostalCodes() != null) && (address.getPostalCodes().size() > 0)) {
			addressDt.setPostalCode(address.getPostalCodes().get(0).getText());
		}
		if ((address.getStates() != null) && (address.getStates().size() > 0)) {
			addressDt.setState(address.getStates().get(0).getText());
		}
		if ((address.getCountries() != null) && (address.getCountries().size() > 0)) {
			addressDt.setCountry(address.getCountries().get(0).getText());
		}
		if ((address.getUses() != null) && (address.getCountries().size() > 0)) {
			switch (address.getUses().get(0)) {
			case H:
			case HP:
				addressDt.setUse(AddressUseEnum.HOME);
				break;
			case WP:
				addressDt.setUse(AddressUseEnum.WORK);
				break;
			}

		}
		return addressDt;

	}

	/**
	 * Convert the cda gender to the fhir gender.
	 *
	 * @param gender
	 *            the gender
	 * @return the administrative gender enum
	 */
	static public AdministrativeGenderEnum convertGender(AdministrativeGender gender) {
		if (gender != null) {
			if (gender.equals(AdministrativeGender.FEMALE)) {
				return AdministrativeGenderEnum.FEMALE;
			} else if (gender.equals(AdministrativeGender.MALE)) {
				return AdministrativeGenderEnum.MALE;
			} else if (gender.equals(AdministrativeGender.UNDIFFERENTIATED)) {
				return AdministrativeGenderEnum.OTHER;
			}
		}
		return null;
	}

	/**
	 * Converts the convenience name to the fhir name
	 *
	 * @param name
	 *            the name
	 * @return converted name
	 */
	static public HumanNameDt convertName(Name name) {
		if (name != null) {
			final HumanNameDt humanName = new HumanNameDt();
			if (name.getGivenNames() != null) {
				final EList<ENXP> givens = name.getMdhtPn().getGivens();
				for (final ENXP given : givens) {
					humanName.addGiven(given.getText());
				}
			}
			if (name.getFamilyNames() != null) {
				final EList<ENXP> givens = name.getMdhtPn().getFamilies();
				for (final ENXP given : givens) {
					humanName.addFamily(given.getText());
				}
			}
			if (name.getPrefixes() != null) {
				final EList<ENXP> givens = name.getMdhtPn().getPrefixes();
				for (final ENXP given : givens) {
					humanName.addPrefix(given.getText());
				}
			}
			if (name.getSuffixes() != null) {
				final EList<ENXP> givens = name.getMdhtPn().getSuffixes();
				for (final ENXP given : givens) {
					humanName.addSuffix(given.getText());
				}
			}
			return humanName;
		}
		return null;
	}

	/**
	 * Convert telecom from hl7 datatype to fhir datatpye.
	 *
	 * @param tel
	 *            hl7 data type
	 * @return the fhir data type
	 */
	public static ContactPointDt convertTelecom(TEL tel) {
		if (tel == null) {
			return null;
		}
		final ContactPointDt contactPointDt = new ContactPointDt();
		String value = null;
		ContactPointSystemEnum system = null;
		ContactPointUseEnum use = null;
		if ((tel.getValue().length() > 4) && tel.getValue().startsWith("tel:")) {
			value = tel.getValue().substring(4);
			system = ContactPointSystemEnum.PHONE;
		} else if ((tel.getValue().length() > 4) && tel.getValue().startsWith("mailto:")) {
			value = tel.getValue().substring(7);
			system = ContactPointSystemEnum.EMAIL;
		}
		if ((tel.getUses().size() > 0)
				&& (tel.getUses().get(0) == TelecommunicationAddressUse.HP)) {
			use = ContactPointUseEnum.HOME;
		}
		if ((tel.getUses().size() > 0)
				&& (tel.getUses().get(0) == TelecommunicationAddressUse.WP)) {
			use = ContactPointUseEnum.WORK;
		}
		if ((tel.getUses().size() > 0)
				&& (tel.getUses().get(0) == TelecommunicationAddressUse.MC)) {
			use = ContactPointUseEnum.MOBILE;
		}
		contactPointDt.setSystem(system);
		contactPointDt.setUse(use);
		contactPointDt.setValue(value);
		return contactPointDt;
	}

	@Child(name = "birthPlace")
	@Extension(url = "http://hl7.org/fhir/ExtensionDefinition/birthPlace", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The birtplace of the patientt")
	private AddressDt birthPlace;

	@Child(name = "religiousAffiliation")
	@Extension(url = "http://hl7.org/fhir/ExtensionDefinition/us-core-religion", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The religious Affiliation of the patient")
	private CodeableConceptDt religiousAffiliation;

	@Child(name = "nation")
	@Extension(url = "http://www.ehealth-connector.org/fhir-extensions/nation", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The nation of the patient")
	private CodeableConceptDt nation;

	@Child(name = "employeeOccupation")
	@Extension(url = "http://www.ehealth-connector.org/fhir-extensions/employeeOccupation", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The employee OccupationCode of the patient")
	private CodeableConceptDt employeeOccupation;

	/**
	 * Instantiates a new fhir patient.
	 */
	public FhirPatient() {

	}

	/**
	 * Instantiates a new fhir patient based on the
	 * org.ehealth_connector.common.patient
	 *
	 * @param patient
	 *            the patient
	 */
	public FhirPatient(Patient patient) {

		if (patient == null) {
			return;
		}

		if (patient.getNames() != null) {
			for (final Name name : patient.getNames()) {
				getName().add(convertName(name));
			}
		}

		if (patient.getBirthday() != null) {
			setBirthDateWithDayPrecision(patient.getBirthday());
		}

		if (patient.getAdministrativeGenderCode() != null) {
			setGender(convertGender(patient.getAdministrativeGenderCode()));
		}

		if ((patient.getAddresses() != null) && (patient.getAddresses().size() > 0)) {
			for (final Address address : patient.getAddresses()) {
				getAddress().add(convertAddress(address.getMdhtAdress()));
			}
		}
		for (final Identificator ident : patient.getIds()) {
			this.getIdentifier()
					.add(new IdentifierDt("urn:oid:" + ident.getRoot(), ident.getExtension()));
		}
		final Organization organization = patient.getMdhtPatientRole().getProviderOrganization();
		if (organization != null) {
			final ca.uhn.fhir.model.dstu2.resource.Organization fhirOrganization = new ca.uhn.fhir.model.dstu2.resource.Organization();

			if ((organization != null) && (organization.getIds() != null)
					&& (organization.getIds().size() > 0)) {
				final org.openhealthtools.mdht.uml.hl7.datatypes.II ii = organization.getIds()
						.get(0);
				final IdentifierDt identifier = new IdentifierDt();
				identifier.setValue(ii.getExtension());
				identifier.setSystem("urn:oid:" + ii.getRoot());
				fhirOrganization.getIdentifier().add(identifier);
			}

			if ((organization.getNames() != null) && (organization.getNames().size() > 0)) {
				final String name = organization.getNames().get(0).getText();
				fhirOrganization.setName(name);
			}

			if ((organization.getTelecoms() != null) && (organization.getTelecoms().size() > 0)) {
				final TEL tel = organization.getTelecoms().get(0);
				final ContactPointDt fhirTel = fhirOrganization.addTelecom();
				if (tel.getValue().startsWith("tel:")) {
					fhirTel.setValue(tel.getValue().substring(4));
				}
			}
			getManagingOrganization().setResource(fhirOrganization);
		}

		if (patient.getTelecoms() != null) {
			for (final TEL tel : patient.getTelecoms().getMdhtTelecoms()) {
				this.getTelecom().add(convertTelecom(tel));
			}
		}

		// languageCommunications
		if (patient.getMdhtPatient().getLanguageCommunications().size() > 0) {
			for (final LanguageCommunication languageCommunication : patient.getMdhtPatient()
					.getLanguageCommunications()) {
				final Communication communication = new Communication();
				final CodeableConceptDt language = new CodeableConceptDt();
				language.setText(languageCommunication.getLanguageCode().getCode());
				communication.setLanguage(language);
				this.getCommunication().add(communication);
			}
		}

		// maritalStatus
		if (((patient.getMdhtPatient().getMaritalStatusCode() != null)
				&& (patient.getMdhtPatient().getMaritalStatusCode().getCode() != null))) {
			this.setMaritalStatus(MaritalStatusCodesEnum
					.valueOf(patient.getMdhtPatient().getMaritalStatusCode().getCode()));
		}

		// deceasedBooolean
		if (patient.getDeceasedInd() != null) {
			setDeceased(new BooleanDt(patient.getDeceasedInd()));
		}

		// deceasedDateTime
		if (patient.getDeceasedTime() != null) {
			setDeceased(new DateTimeDt(patient.getDeceasedTime()));
		}

		// multipleBirthInd
		if (patient.getMultipleBirthInd() != null) {
			setMultipleBirth(new BooleanDt(patient.getMultipleBirthInd()));
		}

		// multipleBirthOrder
		if (patient.getMultipleBirthOrderNumber() != null) {
			setMultipleBirth(new IntegerDt(patient.getMultipleBirthOrderNumber()));
		}

		// mothersMaidenName
		if (patient.getMothersMaidenName() != null) {
			final HumanNameDt mothersMaidenName = new HumanNameDt();
			mothersMaidenName.addFamily(patient.getMothersMaidenName());
			setMothersMaidenName(mothersMaidenName);
		}

		// birthplace
		if ((patient.getMdhtPatient().getBirthplace() != null)
				&& (patient.getMdhtPatient().getBirthplace().getPlace() != null)) {
			setBirthPlace(
					convertAddress(patient.getMdhtPatient().getBirthplace().getPlace().getAddr()));
		}

		// religiousAffiliation
		if (patient.getReligiousAffiliation() != null) {
			final CodeableConceptDt religiousAffiliation = new CodeableConceptDt();
			religiousAffiliation.setText(patient.getReligiousAffiliation());
			this.setReligiousAffiliation(religiousAffiliation);
		}

		// nationCode
		if (patient.getNation() != null) {
			final CodeableConceptDt nationCode = new CodeableConceptDt();
			nationCode.setText(patient.getNation());
			this.setNation(nationCode);
		}

		// employeeOccupationcode
		if (patient.getEmployeeOccupation() != null) {
			final CodeableConceptDt employeeOccupationCode = new CodeableConceptDt();
			employeeOccupationCode.setText(patient.getEmployeeOccupation());
			this.setEmployeeOccupation(employeeOccupationCode);
		}

	}

	/**
	 * converts the fhir address to the convenience address
	 *
	 * @param address
	 * @return fhir address
	 */
	@SuppressWarnings("incomplete-switch")
	private Address convertAddress(AddressDt addressDt) {
		if (addressDt == null) {
			return null;
		}

		String addressline1 = "";
		String addressline2 = "";
		String addressline3 = "";

		if (addressDt.getLine().size() > 2) {
			addressline3 = addressDt.getLine().get(2).getValueAsString();
		}
		if (addressDt.getLine().size() > 1) {
			addressline2 = addressDt.getLine().get(1).getValueAsString();
		}
		if (addressDt.getLine().size() > 0) {
			addressline1 = addressDt.getLine().get(0).getValueAsString();
		}

		String city = "";
		if (addressDt.getCity() != null) {
			city = addressDt.getCity();
		}
		String zip = "";
		if (addressDt.getPostalCode() != null) {
			zip = addressDt.getPostalCode();
		}

		AddressUse addressUse = null;
		if ((addressDt.getUseElement() != null)
				&& (addressDt.getUseElement().getValueAsEnum() != null)) {
			switch (addressDt.getUseElement().getValueAsEnum()) {
			case HOME:
				addressUse = AddressUse.PRIVATE;
				break;
			case WORK:
				addressUse = AddressUse.BUSINESS;
				break;
			default:
				break;
			}
		}
		final Address patientAddress = new Address(addressline1, addressline2, addressline3, zip,
				city, addressUse);
		if (addressDt.getState() != null) {
			patientAddress.getMdhtAdress().addState(addressDt.getState());
		}
		if (addressDt.getCountry() != null) {
			patientAddress.getMdhtAdress().addCountry(addressDt.getCountry());
		}

		return patientAddress;
	}

	/**
	 * Gets the birth place.
	 *
	 * @return the birth place
	 */
	public AddressDt getBirthPlace() {
		return birthPlace;
	}

	/**
	 * Gets the employee occupation.
	 *
	 * @return the employee occupation
	 */
	public CodeableConceptDt getEmployeeOccupation() {
		return employeeOccupation;
	}

	/**
	 * Gets the maiden name, implementation might change, because it is yet an
	 * open issue how it is stored in pdqm/fhir
	 *
	 * @return mothers maiden name
	 */
	public HumanNameDt getMothersMaidenName() {
		for (final Contact contact : getContact()) {
			for (final CodeableConceptDt codeableConceptDt : contact.getRelationship()) {
				for (final CodingDt codingDt : codeableConceptDt.getCoding()) {
					if ("parent".equals(codingDt.getCode())
							&& "female".equals(contact.getGender())) {
						if ((NameUseEnum.MAIDEN
								.equals(contact.getName().getUseElement().getValueAsEnum()))) {
							return contact.getName();
						}
					}
				}
			}
		}
		return new HumanNameDt();
	}

	/**
	 * Gets the nation.
	 *
	 * @return the nation
	 */
	public CodeableConceptDt getNation() {
		return nation;
	}

	/**
	 * converts the fhir patient resource in the convenience api patient
	 *
	 * @return the patient convenience api patient
	 */
	public Patient getPatient() {

		final Name patientName = new Name(null, null);
		final PN pn = patientName.getMdhtPn();
		AdministrativeGender patientGender = null;
		Date patientBirthdate = null;

		final HumanNameDt humanDt = this.getNameFirstRep();
		if (humanDt != null) {
			for (final StringDt name : humanDt.getPrefix()) {
				pn.addPrefix(name.getValue());
			}
			for (final StringDt name : humanDt.getGiven()) {
				pn.addGiven(name.getValue());
			}
			for (final StringDt name : humanDt.getFamily()) {
				pn.addFamily(name.getValue());
			}
			for (final StringDt name : humanDt.getSuffix()) {
				pn.addSuffix(name.getValue());
			}
		}

		final String gender = getGender();
		if (gender != null) {
			if (gender.equals(AdministrativeGenderEnum.FEMALE.getCode())) {
				patientGender = AdministrativeGender.FEMALE;
			} else if (gender.equals(AdministrativeGenderEnum.MALE.getCode())) {
				patientGender = AdministrativeGender.MALE;
			} else if (gender.equals(AdministrativeGenderEnum.OTHER.getCode())) {
				patientGender = AdministrativeGender.UNDIFFERENTIATED;
			}
		}

		patientBirthdate = getBirthDate();
		final Patient patient = new Patient(patientName, patientGender, patientBirthdate);
		for (final IdentifierDt identDt : getIdentifier()) {
			String oid = "";
			if (identDt.getSystem().startsWith("urn:oid:")) {
				oid = identDt.getSystem().substring(8);
			}
			final String id = identDt.getValue();
			final Identificator identificator = new Identificator(oid, id);
			patient.addId(identificator);
		}

		for (final AddressDt addressDt : getAddress()) {
			patient.addAddress(convertAddress(addressDt));
		}

		if (getManagingOrganization() != null) {
			final PatientRole patientRole = patient.getMdhtPatientRole();
			final Organization organization = CDAFactory.eINSTANCE.createOrganization();
			final org.ehealth_connector.common.Organization convenienceOrganization = new org.ehealth_connector.common.Organization(
					organization);

			patientRole.setProviderOrganization(organization);
			final ca.uhn.fhir.model.dstu2.resource.Organization org = (ca.uhn.fhir.model.dstu2.resource.Organization) getManagingOrganization()
					.getResource();

			if ((org != null) && (org.getName() != null)) {
				convenienceOrganization.addName(org.getName());
			}

			if ((org != null) && org.getIdentifierFirstRep().getSystem().startsWith("urn:oid:")) {
				String oid = "";
				oid = org.getIdentifierFirstRep().getSystem().substring(8);
				organization.getIds().add(
						new Identificator(oid, org.getIdentifierFirstRep().getValue()).getIi());
			}
			if ((org != null) && (org.getTelecom().size() > 0)) {
				final ContactPointDt contactPointDt = org.getTelecomFirstRep();
				if ((contactPointDt != null) && (contactPointDt.getValue() != null)) {
					final TEL tel = Util.createTel(contactPointDt.getValue(), null);
					organization.getTelecoms().add(tel);
				}
			}
		}

		// telecommunications
		if (getTelecom().size() > 0) {
			final Telecoms telecoms = new Telecoms();
			for (final ContactPointDt contactPointDt : getTelecom()) {
				if (ContactPointSystemEnum.PHONE
						.equals(contactPointDt.getSystemElement().getValueAsEnum())) {
					AddressUse addressUse = null;
					if (ContactPointUseEnum.HOME
							.equals(contactPointDt.getUseElement().getValueAsEnum())) {
						addressUse = AddressUse.PRIVATE;
					} else if (ContactPointUseEnum.WORK
							.equals(contactPointDt.getUseElement().getValueAsEnum())) {
						addressUse = AddressUse.BUSINESS;
					} else if (ContactPointUseEnum.MOBILE
							.equals(contactPointDt.getUseElement().getValueAsEnum())) {
						addressUse = AddressUse.MOBILE;
					}
					telecoms.addPhone(contactPointDt.getValue(), addressUse);
				}
				if (ContactPointSystemEnum.EMAIL
						.equals(contactPointDt.getSystemElement().getValueAsEnum())) {
					AddressUse addressUse = null;
					if (ContactPointUseEnum.HOME
							.equals(contactPointDt.getUseElement().getValueAsEnum())) {
						addressUse = AddressUse.PRIVATE;
					} else if (ContactPointUseEnum.WORK
							.equals(contactPointDt.getUseElement().getValueAsEnum())) {
						addressUse = AddressUse.BUSINESS;
					}
					telecoms.addEMail(contactPointDt.getValue(), addressUse);
				}
			}
			if (telecoms.getMdhtTelecoms().size() > 0) {
				patient.setTelecoms(telecoms);
			}
		}

		// languageCommunications
		if (getCommunication().size() > 0) {
			for (final Communication communication : getCommunication()) {
				final LanguageCommunication lang = CDAFactory.eINSTANCE
						.createLanguageCommunication();
				final CS languageCode = DatatypesFactory.eINSTANCE.createCS();
				languageCode.setCode(communication.getLanguage().getText());
				lang.setLanguageCode(languageCode);
				patient.getMdhtPatient().getLanguageCommunications().add(lang);
			}
		}

		// maritalStatus
		if (!getMaritalStatus().isEmpty()) {
			final CE maritalStatusCode = DatatypesFactory.eINSTANCE.createCE();
			maritalStatusCode.setCode(getMaritalStatus().getValueAsEnum().toArray()[0].toString());
			patient.getMdhtPatient().setMaritalStatusCode(maritalStatusCode);
		}

		// deceasedBooolean
		final IDatatype idDeceased = getDeceased();
		if (idDeceased instanceof BooleanDt) {
			final BooleanDt deceased = (BooleanDt) idDeceased;
			if (deceased.getValue() != null) {
				patient.setDeceasedInd(deceased.getValue());
			}
		}

		// deceasedDateTime
		if (idDeceased instanceof DateTimeDt) {
			final DateTimeDt deceased = (DateTimeDt) idDeceased;
			if (deceased.getValue() != null) {
				patient.setDeceasedTime(deceased.getValue());
				patient.setDeceasedInd(true);
			}
		}

		// multipleBirthOrder
		final IDatatype iMultipleBirth = getMultipleBirth();
		if (iMultipleBirth instanceof IntegerDt) {
			final IntegerDt multipleBirth = (IntegerDt) iMultipleBirth;
			if (multipleBirth.getValue() != null) {
				patient.setMultipleBirthOrderNumber(multipleBirth.getValue());
				patient.setMultipleBirthInd(true);
			}
		}

		// multipleBirth Indicator
		if (iMultipleBirth instanceof BooleanDt) {
			final BooleanDt multipleBirth = (BooleanDt) iMultipleBirth;
			if (multipleBirth.getValue() != null) {
				patient.setMultipleBirthInd(true);
			}
		}

		// mothersName
		final HumanNameDt mothersMaidenName = getMothersMaidenName();
		if (!mothersMaidenName.isEmpty()) {
			patient.setMothersMaidenName(mothersMaidenName.getFamilyAsSingleString());
		}

		if ((getBirthPlace() != null) && !getBirthPlace().isEmpty()) {
			final Birthplace birthPlace = CDAFactory.eINSTANCE.createBirthplace();
			final Place place = CDAFactory.eINSTANCE.createPlace();
			birthPlace.setPlace(place);
			place.setAddr(this.convertAddress(this.getBirthPlace()).getMdhtAdress());
			patient.getMdhtPatient().setBirthplace(birthPlace);
		}

		// religiousAffiliation
		if ((getReligiousAffiliation() != null) && !getReligiousAffiliation().isEmpty()) {
			patient.setReligiousAffiliation(getReligiousAffiliation().getText());
		}

		// nationCode
		if ((getNation() != null) && !getNation().isEmpty()) {
			patient.setNation(getNation().getText());
		}

		// employeeOccupationCode
		if ((getEmployeeOccupation() != null) && !getEmployeeOccupation().isEmpty()) {
			patient.setEmployeeOccupation(getEmployeeOccupation().getText());
		}

		return patient;
	}

	/**
	 * Gets the religious Affiliation
	 *
	 * @return religious affiliation
	 */
	public CodeableConceptDt getReligiousAffiliation() {
		return religiousAffiliation;
	}

	/**
	 * Sets the birth place.
	 *
	 * @param address
	 *            the new birth place
	 */
	public void setBirthPlace(AddressDt address) {
		birthPlace = address;

	}

	/**
	 * Sets the employee occupation.
	 *
	 * @param employeeOccupation
	 *            the new employee occupation
	 */
	public void setEmployeeOccupation(CodeableConceptDt employeeOccupation) {
		this.employeeOccupation = employeeOccupation;
	}

	/**
	 * Sets the maiden name, implementation might change, because it is yet an
	 * open issue how it is stored in pdqm/fhir
	 *
	 * @param maidenName
	 *            sets the mothers maiden name
	 */
	public void setMothersMaidenName(HumanNameDt maidenName) {
		maidenName.setUse(NameUseEnum.MAIDEN);
		for (final Contact contact : getContact()) {
			for (final CodeableConceptDt codeableConceptDt : contact.getRelationship()) {
				for (final CodingDt codingDt : codeableConceptDt.getCoding()) {
					if ("parent".equals(codingDt.getCode())
							&& "female".equals(contact.getGender())) {
						if ((NameUseEnum.MAIDEN
								.equals(contact.getName().getUseElement().getValueAsEnum()))) {
							contact.setName(maidenName);
							return;
						}
					}
				}
			}
		}
		final Contact mother = addContact().setGender(AdministrativeGenderEnum.FEMALE);
		mother.addRelationship().addCoding().setCode("parent");
		mother.setName(maidenName);
	}

	/**
	 * Sets the nation.
	 *
	 * @param nation
	 *            the new nation
	 */
	public void setNation(CodeableConceptDt nation) {
		this.nation = nation;
	}

	/**
	 * Set the religious Affiliation
	 *
	 * @param religiousAffiliation
	 *            the religious affiliation
	 */
	public void setReligiousAffiliation(CodeableConceptDt religiousAffiliation) {
		this.religiousAffiliation = religiousAffiliation;
	}

}
