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
package org.ehealth_connector.communication.mpi.impl;

import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.ehealth_connector.communication.mpi.FhirPatient;
import org.ehealth_connector.communication.mpi.MpiAdapterInterface;
import org.hl7.v3.AD;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT030007UVPerson;
import org.hl7.v3.COCTMT150003UV03ContactParty;
import org.hl7.v3.EN;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnPrefix;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.HomeAddressUse;
import org.hl7.v3.II;
import org.hl7.v3.INT1;
import org.hl7.v3.ON;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAMT201310UV02BirthPlace;
import org.hl7.v3.PRPAMT201310UV02Employee;
import org.hl7.v3.PRPAMT201310UV02LanguageCommunication;
import org.hl7.v3.PRPAMT201310UV02Nation;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02PersonalRelationship;
import org.hl7.v3.TEL;
import org.hl7.v3.TS1;
import org.hl7.v3.WorkPlaceAddressUse;
import org.openhealthtools.ihe.atna.auditor.PDQConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.common.hl7v3.client.PixPdqV3Utils;
import org.openhealthtools.ihe.common.hl7v3.client.V3GenericAcknowledgement;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqConsumer;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqConsumerResponse;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqContinuationCancel;
import org.openhealthtools.ihe.pdq.consumer.v3.V3PdqContinuationQuery;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumer;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumerQuery;
import org.openhealthtools.ihe.pix.consumer.v3.V3PixConsumerResponse;
import org.openhealthtools.ihe.pix.source.v3.V3PixSource;
import org.openhealthtools.ihe.pix.source.v3.V3PixSourceAcknowledgement;
import org.w3c.dom.Element;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient.Communication;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;

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
public class V3PixPdqAdapter implements MpiAdapterInterface<V3PdqQuery, V3PdqQueryResponse> {

	/** The log. */
	protected final Log log = LogFactory.getLog(V3PixPdqAdapter.class);

	/** The adapter cfg. */
	protected V3PixPdqAdapterConfig adapterCfg;

	/** The home community oid. */
	protected String homeCommunityOid;

	/** If the pix source is configured. */
	protected boolean pixSourceConfigured;

	/** If the pix consumer is configured. */
	protected boolean pixConsumerConfigured;

	/** The pix source. */
	protected V3PixSource pixSource;

	/** The v3 pix consumer. */
	protected V3PixConsumer v3PixConsumer;

	/** if the pdq consumer is configured. */
	protected boolean pdqConsumerConfigured;

	/** The v3 pdq consumer. */
	protected V3PdqConsumer v3PdqConsumer;

	/**
	 * Used to capture additional oid identifiers for the person such as a
	 * Drivers’ license or Social Security Number.
	 */
	protected Set<String> otherIdsOidSet;

	/**
	 * Instantiates a new v3 pix adapter.
	 */
	public V3PixPdqAdapter() {
		otherIdsOidSet = new HashSet<String>();
	}

	/**
	 * Instantiates a new v3 pix adapter.
	 * 
	 * @param adapterConfig
	 *            the adapter config
	 */
	public V3PixPdqAdapter(V3PixPdqAdapterConfig adapterConfig) {
		otherIdsOidSet = new HashSet<String>();
		adapterCfg = adapterConfig;
		homeCommunityOid = adapterCfg.homeCommunityOid;
		if (adapterConfig.otherOidIds != null) {
			for (String oid : adapterConfig.otherOidIds) {
				otherIdsOidSet.add(oid);
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
		otherIdsOidSet.add("urn:oid:" + oid);
	}

	/**
	 * adds a patient to the mpi. implements ITI-44 Patient Identity Source –
	 * Add Patient Record
	 * 
	 * @param patient
	 *            the patient
	 * @return true, if successful
	 * @see org.ehealth_connector.communication.mpi.MpiAdapterInterface#addPatient(org.ehealth_connector.communication.mpi.FhirPatient)
	 */
	@Override
	public boolean addPatient(FhirPatient patient) {
		configurePix(true);
		log.debug("creating v3RecordAddedMessage");
		V3PixSourceMessageHelper v3RecordAddedMessage = new V3PixSourceMessageHelper(true, false,
				false, adapterCfg.senderApplicationOid, adapterCfg.senderFacilityOid,
				adapterCfg.receiverApplicationOid, adapterCfg.receiverFacilityOid);
		log.debug("add demographic data");
		addDemographicData(patient, v3RecordAddedMessage);
		try {
			printMessage("addPatient", v3RecordAddedMessage.getV3RecordAddedMessage().getRequest());
			V3PixSourceAcknowledgement v3pixack = pixSource
					.sendRecordAdded(v3RecordAddedMessage.getV3RecordAddedMessage());
			printMessage("sendRecordAdded", v3pixack.getRequest());
			return checkResponse(v3pixack);
		} catch (Exception e) {
			log.error("addPatient failed", e);
			return false;
		}
	}

	/**
	 * Returns a confgure V3PdqQuery Object which can be used to perfom a query
	 *
	 * @return the mpi query
	 */
	@Override
	public V3PdqQuery getMpiQuery() {
		return new V3PdqQuery(adapterCfg.senderApplicationOid, adapterCfg.senderFacilityOid,
				adapterCfg.receiverApplicationOid, adapterCfg.receiverFacilityOid);
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

		if (rootOid != null && v3PixConsumerResponse != null
				&& ((v3PixConsumerResponse.getNumPatientIds() > 0)
						|| (v3PixConsumerResponse.getNumAsOtherIds() > 0))) {

			for (int i = 0; i < v3PixConsumerResponse.getNumPatientIds(); i++) {
				String id[] = v3PixConsumerResponse.getPatientID(i);
				if (id[2] != null && id[2].equals(rootOid)) {
					return id[0];
				}
			}
			for (int i = 0; i < v3PixConsumerResponse.getNumAsOtherIds(); i++) {
				String id[] = v3PixConsumerResponse.getPatientAsOtherID(i);
				if (id[2] != null && id[2].equals(rootOid)) {
					return id[0];
				}
			}
		}
		return null;
	}

	/**
	 * Gets the patients from pdq query.
	 *
	 * @param response
	 *            the response
	 * @return the patients from pdq query
	 */
	public List<FhirPatient> getPatientsFromPdqQuery(V3PdqConsumerResponse response) {
		boolean success = false;
		if (response != null) {
			success = !response.hasError();
			if (success) {
				ArrayList<FhirPatient> listFhirPatients = new ArrayList<FhirPatient>(
						response.getNumRecordsCurrent());
				for (int i = 0; i < response.getNumRecordsCurrent(); ++i) {
					FhirPatient fhirPatient = new FhirPatient();
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
	 * @see org.ehealth_connector.communication.mpi.MpiAdapterInterface#mergePatient(org.ehealth_connector.communication.mpi.FhirPatient,
	 *      java.lang.String)
	 */
	@Override
	public boolean mergePatient(FhirPatient patient, String obsoleteId) {

		if (!configurePix(true)) {
			return false;
		}

		V3PixSourceMessageHelper v3pixSourceMsgMerge = new V3PixSourceMessageHelper(false, false,
				true, adapterCfg.senderApplicationOid, adapterCfg.senderFacilityOid,
				adapterCfg.receiverApplicationOid, adapterCfg.receiverFacilityOid);
		addDemographicData(patient, v3pixSourceMsgMerge);

		v3pixSourceMsgMerge.getV3MergePatientsMessage().setObsoletePatientID(obsoleteId,
				this.homeCommunityOid, this.adapterCfg.homeCommunityNamespace);
		try {
			printMessage("sourceMerge",
					v3pixSourceMsgMerge.getV3MergePatientsMessage().getRequest());
			V3PixSourceAcknowledgement v3pixack = pixSource
					.sendMergePatients(v3pixSourceMsgMerge.getV3MergePatientsMessage());
			printMessage("sourceMerge", v3pixack.getRequest());
			return checkResponse(v3pixack);
		} catch (Exception e) {
			log.error("mergePatient failed", e);
			return false;
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
	 * @see org.ehealth_connector.communication.mpi.MpiAdapterInterface#queryPatientId(org.ehealth_connector.communication.mpi.FhirPatient,
	 *      java.lang.String[], java.lang.String[])
	 */
	@Override
	public String[] queryPatientId(FhirPatient patient, String[] queryDomainOids,
			String[] queryDomainNamespaces) {

		if (!configurePix(false)) {
			return null;
		}
		String domainToReturnOids[] = null;
		String domainToReturnNamespaces[] = null;

		if (queryDomainOids != null) {
			domainToReturnOids = queryDomainOids;
		} else {
			if (adapterCfg.domainToReturnOid != null) {
				domainToReturnOids = new String[1];
				domainToReturnOids[0] = adapterCfg.domainToReturnOid;
			}
		}
		if (queryDomainNamespaces != null) {
			domainToReturnNamespaces = queryDomainNamespaces;
		} else if (adapterCfg.domainToReturnNamespace != null) {
			domainToReturnNamespaces = new String[1];
			domainToReturnNamespaces[0] = adapterCfg.domainToReturnNamespace;
		}

		V3PixConsumerQuery v3PixConsumerQuery = new V3PixConsumerQuery(
				adapterCfg.senderApplicationOid, adapterCfg.senderFacilityOid,
				adapterCfg.receiverApplicationOid, adapterCfg.receiverFacilityOid);

		// add the patient identifier
		String homeCommunityPatientId = this.getHomeCommunityPatientId(patient);
		if (homeCommunityPatientId != null) {
			v3PixConsumerQuery.addPatientIdToQuery(homeCommunityPatientId, homeCommunityOid,
					adapterCfg.homeCommunityNamespace);

			if (domainToReturnOids != null) {
				for (int i = 0; i < domainToReturnOids.length; ++i) {
					String domainToReturnOid = domainToReturnOids[i];
					String domainToReturnNamespace = null;
					if (domainToReturnNamespaces != null && i < domainToReturnNamespaces.length) {
						domainToReturnNamespace = domainToReturnNamespaces[i];
					}
					v3PixConsumerQuery.addDomainToReturn(domainToReturnOid,
							domainToReturnNamespace);
				}
			}
			V3PixConsumerResponse v3PixConsumerResponse = null;
			try {
				v3PixConsumerResponse = v3PixConsumer.sendQuery(v3PixConsumerQuery);
				if (domainToReturnOids != null) {
					String returnIds[] = new String[domainToReturnOids.length];
					for (int i = 0; i < returnIds.length; ++i) {
						returnIds[i] = getPatientDomainId(v3PixConsumerResponse,
								domainToReturnOids[i]);
					}
					return returnIds;
				}
				return null;
			} catch (Exception e) {
				log.error("exception queryPatient", e);
				return null;
			}
		} else {
			log.error("homeCommunityPatientId not provided");
			return null;
		}
	}

	/**
	 * Perfoms a PDQ Query
	 *
	 * @param mpiQuery
	 *            the mpi query object
	 * @return the v3 pdq query response
	 */
	@Override
	public V3PdqQueryResponse queryPatients(V3PdqQuery mpiQuery) {
		V3PdqQueryResponse queryResponse = new V3PdqQueryResponse();
		if (!configurePdq()) {
			return queryResponse;
		}
		try {
			if (!mpiQuery.doCancelQuery()) {
				V3PdqConsumerResponse lastPdqConsumerResponse = null;
				if (!mpiQuery.doContinueQuery()) {
					lastPdqConsumerResponse = v3PdqConsumer
							.sendQuery(mpiQuery.getV3PdqConsumerQuery());
				} else {
					lastPdqConsumerResponse = mpiQuery.getLastPdqConsumerResponse();
					V3PdqContinuationQuery continuationQuery = new V3PdqContinuationQuery(
							mpiQuery.getV3PdqConsumerQuery().getSendingApplication(),
							mpiQuery.getV3PdqConsumerQuery().getSendingFacility(),
							mpiQuery.getV3PdqConsumerQuery().getReceivingApplication(0),
							mpiQuery.getV3PdqConsumerQuery().getReceivingFacility(0),
							lastPdqConsumerResponse, mpiQuery.getPageCount());
					continuationQuery.setProcessingCode("T");
					lastPdqConsumerResponse = v3PdqConsumer.sendContinuation(continuationQuery);
				}
				queryResponse.setPatients(getPatientsFromPdqQuery(lastPdqConsumerResponse));
				queryResponse.setSuccess(!lastPdqConsumerResponse.hasError());
				queryResponse.setCurrentNumbers(lastPdqConsumerResponse.getNumRecordsCurrent());
				queryResponse.setRemainingNumbers(lastPdqConsumerResponse.getNumRecordsRemaining());
				INT1 totalNumbers = lastPdqConsumerResponse.getPdqResponse().getControlActProcess()
						.getQueryAck().getResultTotalQuantity();
				if (totalNumbers != null) {
					queryResponse.setTotalNumbers(totalNumbers.getValue().intValue());
				}
				mpiQuery.setLastPdqConsumerResponse(lastPdqConsumerResponse);
			} else {
				V3PdqConsumerResponse lastPdqConsumerResponse = mpiQuery
						.getLastPdqConsumerResponse();
				V3PdqContinuationCancel continuationCancel = new V3PdqContinuationCancel(
						mpiQuery.getV3PdqConsumerQuery().getSendingApplication(),
						mpiQuery.getV3PdqConsumerQuery().getSendingFacility(),
						mpiQuery.getV3PdqConsumerQuery().getReceivingApplication(0),
						mpiQuery.getV3PdqConsumerQuery().getReceivingFacility(0),
						lastPdqConsumerResponse);
				V3GenericAcknowledgement v3Ack = v3PdqConsumer.sendCancel(continuationCancel);
				queryResponse.setSuccess(!v3Ack.hasError());
			}
		} catch (Exception e) {
			log.error("queryPatient failed", e);
			queryResponse.setSuccess(false);
			return queryResponse;
		}
		return queryResponse;
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
	 * @see org.ehealth_connector.communication.mpi.MpiAdapterInterface#updatePatient(org.ehealth_connector.communication.mpi.FhirPatient)
	 */
	@Override
	public boolean updatePatient(FhirPatient patient) {
		if (pixSource == null) {
			pixSource = new V3PixSource(adapterCfg.pixSourceUri);
		}
		V3PixSourceMessageHelper v3RecordRevisedMessage = new V3PixSourceMessageHelper(false, true,
				false, adapterCfg.senderApplicationOid, adapterCfg.senderFacilityOid,
				adapterCfg.receiverApplicationOid, adapterCfg.receiverFacilityOid);
		addDemographicData(patient, v3RecordRevisedMessage);
		try {
			printMessage("sourceUpdate",
					v3RecordRevisedMessage.getV3RecordRevisedMessage().getRequest());
			V3PixSourceAcknowledgement v3pixack = pixSource
					.sendRecordRevised(v3RecordRevisedMessage.getV3RecordRevisedMessage());
			printMessage("sourceUpdate", v3pixack.getRequest());
			return checkResponse(v3pixack);
		} catch (Exception e) {
			log.error("updatePatient failed", e);
			return false;
		}
	}

	/**
	 * adds the demographic data to the pix queries, can be overloaded if
	 * additional information of the patient needs to be providied for the mpi.
	 * 
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 add message
	 */
	protected void addDemographicData(FhirPatient patient,
			V3PixSourceMessageHelper v3PixSourceMessage) {
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
	 * @param patient
	 *            the patient
	 * @param v3PixSourceMessage
	 *            the v3 add message
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
		if (patient.getCommunication().size() > 0) {
			for (Communication communication : patient.getCommunication()) {
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getLanguageCommunication() != null) {
			for (PRPAMT201310UV02LanguageCommunication languageCommmunication : pdqPatient
					.getPatientPerson().getLanguageCommunication()) {
				if (languageCommmunication.getLanguageCode() != null
						&& languageCommmunication.getLanguageCode().getCode() != null) {
					Communication communication = new Communication();
					CodeableConceptDt languageCode = new CodeableConceptDt();
					languageCode.setText(languageCommmunication.getLanguageCode().getCode());
					communication.setLanguage(languageCode);
					patient.getCommunication().add(communication);
				}
			}
		}
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
		if (patient.getAddress().size() > 0) {
			for (AddressDt addressDt : patient.getAddress()) {

				String adressOtherDesignation = null;
				if (addressDt.getLine().size() > 1) {
					adressOtherDesignation = addressDt.getLine().get(1).getValueAsString();
				}
				String addressType = null;
				if (addressDt.getUseElement() != null
						&& addressDt.getUseElement().getValueAsEnum() != null) {
					switch (addressDt.getUseElement().getValueAsEnum()) {
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
					}
				}
				v3PixSourceMessage.addPatientAddress(addressDt.getLineFirstRep().getValue(),
						addressDt.getCity(), null, addressDt.getState(), addressDt.getCountry(),
						addressDt.getPostalCode(), adressOtherDesignation, addressType);
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getAddr() != null) {
			for (AD ad : pdqPatient.getPatientPerson().getAddr()) {
				patient.getAddress().add(getAddressDtFromAD(ad));
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
		for (IdentifierDt identifierDt : patient.getIdentifier()) {
			if (this.otherIdsOidSet.contains(identifierDt.getSystem())) {
				v3PixSourceMessage.addPatientOtherID(identifierDt.getValue(),
						identifierDt.getSystem().substring(8));
			} else {
				if (identifierDt.getSystem().length() > 8
						&& (identifierDt.getSystem().startsWith("urn:oid:"))) {
					String oid = identifierDt.getSystem().substring(8);
					if (homeCommunityOid.equals(oid)) {
						v3PixSourceMessage.addPatientID(identifierDt.getValue(), homeCommunityOid,
								adapterCfg.homeCommunityNamespace);
					} else {
						v3PixSourceMessage.addPatientID(identifierDt.getValue(), oid, "");
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
			for (II patientId : pdqPatient.getId()) {
				IdentifierDt identifierDt = new IdentifierDt();
				identifierDt.setSystem("urn:oid:" + patientId.getRoot());
				identifierDt.setValue(patientId.getExtension());
				patient.getIdentifier().add(identifierDt);
			}
		}

		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getAsOtherIDs() != null) {
			for (PRPAMT201310UV02OtherIDs asOtherId : pdqPatient.getPatientPerson()
					.getAsOtherIDs()) {
				if (asOtherId.getId() != null && asOtherId.getId().size() > 0) {
					II patientId = asOtherId.getId().get(0);
					if (patientId != null) {
						IdentifierDt identifierDt = new IdentifierDt();
						identifierDt.setSystem("urn:oid:" + patientId.getRoot());
						identifierDt.setValue(patientId.getExtension());
						patient.getIdentifier().add(identifierDt);
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
		String familyName = patient.getName().get(0).getFamilyAsSingleString();
		String givenName = patient.getName().get(0).getGivenAsSingleString();
		String otherName = ""; // other is resolved into given in addPatientName
		// below, we have that already with above lines
		String prefixName = patient.getName().get(0).getPrefixAsSingleString();
		String suffixName = patient.getName().get(0).getSuffixAsSingleString();
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
		EList<PN> pns = pdqPatient.getPatientPerson().getName();
		for (int i = 0; i < pns.size(); ++i) {
			PN pn = pns.get(i);
			HumanNameDt humanNameDt = new HumanNameDt();
			if (pn.getGiven() != null) {
				for (EnGiven given : pn.getGiven()) {
					humanNameDt.addGiven(getMixedValue(given.getMixed()));
				}
			}
			if (pn.getFamily() != null) {
				for (EnFamily family : pn.getFamily()) {
					humanNameDt.addFamily(getMixedValue(family.getMixed()));
				}
			}
			if (pn.getPrefix() != null) {
				for (EnPrefix prefix : pn.getPrefix()) {
					humanNameDt.addPrefix(getMixedValue(prefix.getMixed()));
				}
			}
			if (pn.getSuffix() != null) {
				for (EnSuffix suffix : pn.getSuffix()) {
					humanNameDt.addPrefix(getMixedValue(suffix.getMixed()));
				}
			}
			patient.getName().add(humanNameDt);
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
		if (patient.getTelecom() != null && patient.getTelecom().size() > 0) {
			for (ContactPointDt contactPointDt : patient.getTelecom()) {
				// system I 0..1 code phone | fax | email | url
				// use M 0..1 code home | work | temp | old | mobile - purpose
				// of this contact point
				String telecomValue = "";
				String useValue = "";
				if ("phone".equals(contactPointDt.getSystem())) {
					telecomValue = "tel:" + contactPointDt.getValue();
					if ("home".equals(contactPointDt.getUse())) {
						useValue = "H";
					}
					if ("work".equals(contactPointDt.getUse())) {
						useValue = "WP";
					}
					if ("mobile".equals(contactPointDt.getUse())) {
						useValue = "MC";
					}
				}
				if ("email".equals(contactPointDt.getSystem())) {
					telecomValue = "mailto:" + contactPointDt.getValue();
					if ("home".equals(contactPointDt.getUse())) {
						useValue = "H";
					}
					if ("work".equals(contactPointDt.getUse())) {
						useValue = "WP";
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getTelecom() != null
				&& pdqPatient.getPatientPerson().getTelecom().size() > 0) {
			for (TEL tel : pdqPatient.getPatientPerson().getTelecom()) {
				ContactPointDt contactPointDt = new ContactPointDt();
				if (tel.getValue() != null && tel.getValue().startsWith("tel:")) {
					contactPointDt.setValue(tel.getValue().substring(4));
					contactPointDt.setSystem(ContactPointSystemEnum.PHONE);
					if (tel.getUse().contains(WorkPlaceAddressUse.WP)) {
						contactPointDt.setUse(ContactPointUseEnum.WORK);
					} else if (tel.getUse().contains(HomeAddressUse.H)
							|| tel.getUse().contains(HomeAddressUse.HP)) {
						contactPointDt.setUse(ContactPointUseEnum.HOME);
					} else
						if (tel.getUse().size() > 0 && "MC".equals(tel.getUse().get(0).getName())) {
						contactPointDt.setUse(ContactPointUseEnum.MOBILE);
					}
					patient.getTelecom().add(contactPointDt);
				}
				if (tel.getValue() != null && tel.getValue().startsWith("mailto:")) {
					contactPointDt.setValue(tel.getValue().substring(7));
					contactPointDt.setSystem(ContactPointSystemEnum.EMAIL);
					if (tel.getUse().contains(WorkPlaceAddressUse.WP)) {
						contactPointDt.setUse(ContactPointUseEnum.WORK);
					} else if (tel.getUse().contains(HomeAddressUse.H)
							|| tel.getUse().contains(HomeAddressUse.HP)) {
						contactPointDt.setUse(ContactPointUseEnum.HOME);
					}
					patient.getTelecom().add(contactPointDt);
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
			log.error("AcknowledgementCode: " + response.getAcknowledgementCode());
			log.error("Query error text: " + response.getErrorText());
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
			log.debug("pdq configure start");
			if (!pdqConsumerConfigured) {
				this.pdqConsumerConfigured = true;
				if (adapterCfg.auditSourceId != null) {
					PDQConsumerAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.auditSourceId);
				}
				if (adapterCfg.auditRepositoryUri != null) {
					PDQConsumerAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.auditRepositoryUri);
				}
				if (v3PdqConsumer == null) {
					v3PdqConsumer = new V3PdqConsumer(adapterCfg.pdqConsumerUri);
				}
			}
		} catch (Exception e) {
			log.error("configuring not successful", e);
			return false;
		}
		log.debug("configure end");
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
			log.debug("pix configure start");
			if (source && !pixSourceConfigured) {
				this.pixSourceConfigured = true;
				if (adapterCfg.auditSourceId != null) {
					PIXSourceAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.auditSourceId);
				}
				if (adapterCfg.auditRepositoryUri != null) {
					PIXSourceAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.auditRepositoryUri);
				}
				if (pixSource == null) {
					pixSource = new V3PixSource(adapterCfg.pixSourceUri);
				}
			}
			if (!source && !pixConsumerConfigured) {
				this.pixConsumerConfigured = true;
				if (adapterCfg.auditSourceId != null) {
					PIXConsumerAuditor.getAuditor().getConfig()
							.setAuditSourceId(adapterCfg.auditSourceId);
				}
				if (adapterCfg.auditRepositoryUri != null) {
					PIXConsumerAuditor.getAuditor().getConfig()
							.setAuditRepositoryUri(adapterCfg.auditRepositoryUri);
				}
				if (v3PixConsumer == null) {
					v3PixConsumer = new V3PixConsumer(adapterCfg.pixQueryUri);
				}

			}
		} catch (Exception e) {
			log.error("configuring not successful", e);
			return false;
		}
		log.debug("configure end");
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
	protected AddressDt getAddressDtFromAD(AD ad) {
		AddressDt addressDt = new AddressDt();
		if (ad.getUse() != null) {
			if (ad.getUse().size() > 0 && "H".equals(ad.getUse().get(0).getName())) {
				addressDt.setUse(AddressUseEnum.HOME);
			}
			if (ad.getUse().size() > 0 && "WP".equals(ad.getUse().get(0).getName())) {
				addressDt.setUse(AddressUseEnum.WORK);
			}
			if (ad.getUse().size() > 0 && "TMP".equals(ad.getUse().get(0).getName())) {
				addressDt.setUse(AddressUseEnum.TEMP);
			}
			if (ad.getUse().size() > 0 && "OLD".equals(ad.getUse().get(0).getName())) {
				addressDt.setUse(AddressUseEnum.OLD);
			}
		}
		if (ad.getStreetAddressLine() != null && ad.getStreetAddressLine().size() > 0) {
			for (AdxpStreetAddressLine addressStreetLine : ad.getStreetAddressLine()) {
				addressDt.addLine().setValue(getMixedValue(addressStreetLine.getMixed()));
			}
		}
		if (ad.getCity() != null && ad.getCity().size() > 0) {
			addressDt.setCity(getMixedValue(ad.getCity().get(0).getMixed()));
		}
		if (ad.getState() != null && ad.getState().size() > 0) {
			addressDt.setState(getMixedValue(ad.getState().get(0).getMixed()));
		}
		if (ad.getPostalCode() != null && ad.getPostalCode().size() > 0) {
			addressDt.setPostalCode(getMixedValue(ad.getPostalCode().get(0).getMixed()));
		}
		if (ad.getCountry() != null && ad.getCountry().size() > 0) {
			addressDt.setCountry(getMixedValue(ad.getCountry().get(0).getMixed()));
		}
		return addressDt;
	}

	/**
	 * Gets the home community patient id.
	 * 
	 * @param patient
	 *            the patient
	 * @return the home community patient id
	 */
	protected String getHomeCommunityPatientId(FhirPatient patient) {
		for (IdentifierDt identifierDt : patient.getIdentifier()) {
			if (identifierDt.getSystem().startsWith("urn:oid:")) {
				if (identifierDt.getSystem().substring(8).equals(this.homeCommunityOid)) {
					return identifierDt.getValue();
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
	protected String getMixedValue(FeatureMap mixed) {
		String returnValue = "";
		// if we have a mixed
		if (mixed.size() > 0) {
			returnValue = mixed.get(0).getValue().toString();
		}
		return returnValue;
	}

	/**
	 * Get the specified patient object
	 * 
	 * @param patientIndex
	 * @return PRPAMT201310UV02Patient - the patient object at the specified
	 *         index.
	 */
	protected PRPAMT201310UV02Patient getPatientByIndex(V3PdqConsumerResponse v3PdqConsumerResponse,
			int patientIndex) {
		return v3PdqConsumerResponse.getPdqResponse().getControlActProcess().getSubject()
				.get(patientIndex).getRegistrationEvent().getSubject1().getPatient();
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
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(element);
			transformer.transform(source, result);

			String xmlString = result.getWriter().toString();

			log.debug(test + "\r" + xmlString);
		} catch (Exception e) {
			log.debug(test + " problem encountered in printMessage");
		}
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
			AddressDt addressDt = patient.getBirthPlace();
			String adressOtherDesignation = null;
			if (addressDt.getLine().size() > 1) {
				adressOtherDesignation = addressDt.getLine().get(1).getValueAsString();
			}
			AD patientAddress = PixPdqV3Utils.createAD(addressDt.getLineFirstRep().getValue(),
					addressDt.getCity(), null, addressDt.getState(), addressDt.getCountry(),
					addressDt.getPostalCode(), adressOtherDesignation, null);
			v3PixSourceMessage.setPatienttBirthPlace(patientAddress);
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getBirthPlace() != null) {
			PRPAMT201310UV02BirthPlace birthplace = pdqPatient.getPatientPerson().getBirthPlace();
			AD addr = birthplace.getAddr();
			if (addr != null) {
				patient.setBirthPlace(getAddressDtFromAD(addr));
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
		IDatatype idDeceased = patient.getDeceased();
		if (idDeceased instanceof DateTimeDt) {
			DateTimeDt deceased = (DateTimeDt) idDeceased;
			if (deceased.getValue() != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				v3PixSourceMessage.setPatientDeceasedTime(dateFormat.format(deceased.getValue()));
				v3PixSourceMessage.setPatientDeceased(true);
			}
		}
		if (idDeceased instanceof BooleanDt) {
			BooleanDt deceased = (BooleanDt) idDeceased;
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
			if (pdqPatient.getPatientPerson().getDeceasedInd() != null
					&& pdqPatient.getPatientPerson().getDeceasedInd().isSetValue()) {
				BooleanDt dt = new BooleanDt();
				dt.setValue(pdqPatient.getPatientPerson().getDeceasedInd().isValue());
				patient.setDeceased(dt);
			}
			if (pdqPatient.getPatientPerson().getDeceasedTime() != null) {
				String deceasedTime = pdqPatient.getPatientPerson().getDeceasedTime().getValue();
				patient.setDeceased(new DateTimeDt(deceasedTime));
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
		if (patient.getEmployeeOccupation() != null && !patient.getEmployeeOccupation().isEmpty()) {
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
		if (pdqPatient != null && pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getAsEmployee() != null) {
			List<PRPAMT201310UV02Employee> employees = pdqPatient.getPatientPerson()
					.getAsEmployee();
			if (employees.size() > 0) {
				PRPAMT201310UV02Employee employee = employees.get(0);
				if (employee.getOccupationCode() != null
						&& employee.getOccupationCode().getCode() != null) {
					CodeableConceptDt employeeOccupationCode = new CodeableConceptDt();
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
		IDatatype iMultipleBirth = patient.getMultipleBirth();
		if (iMultipleBirth instanceof IntegerDt) {
			IntegerDt multipleBirth = (IntegerDt) iMultipleBirth;
			if (multipleBirth.getValue() != null) {
				v3PixSourceMessage.setPatientMultipleBirthOrderNumber(multipleBirth.getValue());
				v3PixSourceMessage.setPatientMultipleBirthIndicator(true);
			}
		}
		if (iMultipleBirth instanceof BooleanDt) {
			BooleanDt multipleBirth = (BooleanDt) iMultipleBirth;
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
			if (pdqPatient.getPatientPerson().getMultipleBirthInd() != null
					&& pdqPatient.getPatientPerson().getMultipleBirthInd().isSetValue()) {
				BooleanDt dt = new BooleanDt();
				dt.setValue(pdqPatient.getPatientPerson().getMultipleBirthInd().isValue());
				patient.setMultipleBirth(dt);
			}
			if (pdqPatient.getPatientPerson().getMultipleBirthOrderNumber() != null) {
				BigInteger birthOrderNumber = pdqPatient.getPatientPerson()
						.getMultipleBirthOrderNumber().getValue();
				patient.setMultipleBirth(new IntegerDt(birthOrderNumber.intValue()));
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
		if (patient.getNation() != null && !patient.getNation().isEmpty()) {
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getAsCitizen() != null) {
			List<org.hl7.v3.PRPAMT201310UV02Citizen> citizens = pdqPatient.getPatientPerson()
					.getAsCitizen();
			if (citizens.size() > 0) {
				PRPAMT201310UV02Nation nation = citizens.get(0).getPoliticalNation();
				if (nation != null && nation.getCode() != null
						&& nation.getCode().getCode() != null) {
					CodeableConceptDt nationCode = new CodeableConceptDt();
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
				patient.getBirthDateElement().getValueAsString().replaceAll("-", ""));
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getBirthTime() != null) {
			TS1 ts = pdqPatient.getPatientPerson().getBirthTime();
			String date = ts.getValue();
			if (date != null && date.length() >= 4) {
				String dateFhir = date.substring(0, 4);
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
			String gender = "";
			if ("male".equals(patient.getGender())) {
				gender = "M";
			} else if ("female".equals(patient.getGender())) {
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getAdministrativeGenderCode() != null) {
			CE gender = pdqPatient.getPatientPerson().getAdministrativeGenderCode();
			if ("M".equals(gender.getCode())) {
				patient.setGender(AdministrativeGenderEnum.MALE);
			} else if ("F".equals(gender.getCode())) {
				patient.setGender(AdministrativeGenderEnum.FEMALE);
			} else if ("U".equals(gender.getCode())) {
				patient.setGender(AdministrativeGenderEnum.OTHER);
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
					patient.getMaritalStatus().getValueAsEnum().toArray()[0].toString());
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getMaritalStatusCode() != null) {
			CE maritalStatusCode = pdqPatient.getPatientPerson().getMaritalStatusCode();
			if (maritalStatusCode.getCode() != null) {
				patient.setMaritalStatus(
						MaritalStatusCodesEnum.valueOf(maritalStatusCode.getCode()));
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
		HumanNameDt maidenName = patient.getMothersMaidenName();
		if (maidenName.isEmpty()) {
			String familyName = maidenName.getFamilyAsSingleString();
			String givenName = maidenName.getGivenAsSingleString();
			String otherName = ""; // other is resolved into given in
			// addPatientName
			String prefixName = maidenName.getPrefixAsSingleString();
			String suffixName = maidenName.getSuffixAsSingleString();
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getPersonalRelationship() != null) {
			for (PRPAMT201310UV02PersonalRelationship personalRelationship : pdqPatient
					.getPatientPerson().getPersonalRelationship()) {
				if (personalRelationship.getCode() != null
						&& "MTH".equals(personalRelationship.getCode().getCode())
						&& "2.16.840.1.113883.5.111"
								.equals(personalRelationship.getCode().getCodeSystem())) {
					COCTMT030007UVPerson motherRelationShipHolder = personalRelationship
							.getRelationshipHolder1();
					if (motherRelationShipHolder != null
							&& motherRelationShipHolder.getName() != null) {
						List<EN> names = motherRelationShipHolder.getName();
						if (names != null && names.size() > 0) {
							// ITI 2b Rev. 11.0 Final Text – 2014-09-23
							// MothersMaidenName Parameter (approx 6645)
							// This optional parameter specifies the maiden name
							// of the mother of the person whose
							// information is being queried. For this parameter
							// item, a single person name (PN) data item shall
							// be specified in the Person.value attribute.
							EN pn = names.get(0);
							HumanNameDt humanNameDt = new HumanNameDt();
							if (pn.getGiven() != null) {
								for (EnGiven given : pn.getGiven()) {
									humanNameDt.addGiven(getMixedValue(given.getMixed()));
								}
							}
							if (pn.getFamily() != null) {
								for (EnFamily family : pn.getFamily()) {
									humanNameDt.addFamily(getMixedValue(family.getMixed()));
								}
							}
							if (pn.getPrefix() != null) {
								for (EnPrefix prefix : pn.getPrefix()) {
									humanNameDt.addPrefix(getMixedValue(prefix.getMixed()));
								}
							}
							if (pn.getSuffix() != null) {
								for (EnSuffix suffix : pn.getSuffix()) {
									humanNameDt.addPrefix(getMixedValue(suffix.getMixed()));
								}
							}
							patient.setMothersMaidenName(humanNameDt);
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
		if (patient.getReligiousAffiliation() != null
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
		if (pdqPatient.getPatientPerson() != null
				&& pdqPatient.getPatientPerson().getReligiousAffiliationCode() != null) {
			CE religiousAffiliation = pdqPatient.getPatientPerson().getReligiousAffiliationCode();
			if (religiousAffiliation.getCode() != null) {
				CodeableConceptDt religion = new CodeableConceptDt();
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
		String organizationOid = "";
		String organizationName = "";
		String organizationTelecomValue = "";

		Organization organization = (Organization) patient.getManagingOrganization().getResource();

		if (organization != null && organization.getIdentifier().size() > 0) {
			IdentifierDt organizationId = organization.getIdentifier().get(0);
			if (organizationId.getSystem().startsWith("urn:oid:")) {
				organizationOid = organizationId.getSystem().substring(8);
			}
		}

		if (organization != null && organization.getName() != null) {
			organizationName = organization.getName();
		}

		if (organization != null && organization.getTelecom().size() > 0) {
			if (organization.getTelecom().size() > 0) {
				ContactPointDt contactPoint = organization.getTelecomFirstRep();
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
			Organization organization = new Organization();
			patient.getManagingOrganization().setResource(organization);

			if (pdqPatient.getProviderOrganization().getId() != null
					&& pdqPatient.getProviderOrganization().getId().size() > 0) {
				for (II id : pdqPatient.getProviderOrganization().getId()) {
					IdentifierDt identifierDt = new IdentifierDt();
					identifierDt.setValue("urn:oid:" + id.getRoot());
					organization.getIdentifier().add(identifierDt);
				}
			}

			EList<ON> ons = pdqPatient.getProviderOrganization().getName();
			if (ons != null && ons.size() > 0) {
				organization.setName(getMixedValue(ons.get(0).getMixed()));
			}

			EList<COCTMT150003UV03ContactParty> contactParties = pdqPatient
					.getProviderOrganization().getContactParty();
			if (contactParties != null && contactParties.size() > 0) {
				EList<TEL> tels = contactParties.get(0).getTelecom();
				if (tels != null && tels.size() > 0) {
					TEL tel = tels.get(0);
					if (tel.getValue() != null && tel.getValue().startsWith("tel:")) {
						ContactPointDt contactPointDt = new ContactPointDt();
						contactPointDt.setValue(tel.getValue().substring(4));
						contactPointDt.setSystem(ContactPointSystemEnum.PHONE);
						organization.getTelecom().add(contactPointDt);
					}
				}
			}
		}

	}

}
