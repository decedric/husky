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

package org.ehealth_connector.cda.ch;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.ehealth_connector.cda.ch.enums.LanguageCode;
import org.ehealth_connector.cda.enums.Confidentiality;
import org.ehealth_connector.cda.enums.ParticipantType;
import org.ehealth_connector.common.DateUtil;
import org.ehealth_connector.common.EHealthConnectorVersions;
import org.ehealth_connector.common.Identificator;
import org.ehealth_connector.common.Organization;
import org.ehealth_connector.common.Patient;
import org.ehealth_connector.common.Person;
import org.ehealth_connector.common.Util;
import org.openhealthtools.ihe.utils.UUID;
import org.openhealthtools.mdht.uml.cda.AssignedCustodian;
import org.openhealthtools.mdht.uml.cda.AssignedEntity;
import org.openhealthtools.mdht.uml.cda.AssociatedEntity;
import org.openhealthtools.mdht.uml.cda.Authenticator;
import org.openhealthtools.mdht.uml.cda.Author;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.CDAPackage;
import org.openhealthtools.mdht.uml.cda.Custodian;
import org.openhealthtools.mdht.uml.cda.DataEnterer;
import org.openhealthtools.mdht.uml.cda.DocumentRoot;
import org.openhealthtools.mdht.uml.cda.InFulfillmentOf;
import org.openhealthtools.mdht.uml.cda.InfrastructureRootTypeId;
import org.openhealthtools.mdht.uml.cda.LegalAuthenticator;
import org.openhealthtools.mdht.uml.cda.Order;
import org.openhealthtools.mdht.uml.cda.Participant1;
import org.openhealthtools.mdht.uml.cda.ch.CDACH;
import org.openhealthtools.mdht.uml.cda.internal.resource.CDAResource;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil.Query;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.INT;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;

/**
 * CDA Dokument, das den Vorgaben der Spezifikation CDA-CH entspricht
 * 
 */
public abstract class CdaCh {

	public static EHealthConnectorVersions currentEhcVersion = EHealthConnectorVersions.EHealthConnectorR201503;
	protected CDACH doc = null; // The CDA Document
	private DocumentRoot docRoot = null; // The OHT-Element that helds the
	// document
	protected Query query;

	/**
	 * Constructor for CdaCh documents
	 * 
	 * <div class="de">Erstellt ein CdaCh Objekt</div> <div class="fr"></div>
	 * 
	 * @param doc
	 *            the CDA-CH Object in its MDHT representation
	 */
	public CdaCh(CDACH doc) {
		// this(doc, null, null);
		this.doc = doc;
		docRoot = CDAFactory.eINSTANCE.createDocumentRoot();
		docRoot.setClinicalDocument(doc);
	}

	/**
	 * Constructor that includes a stylesheet and a cascasing stylesheet into
	 * the document processing instructions and initalizes the standard document
	 * attributes.
	 * 
	 * <div class="de">Creates a CdaCh Object (Swiss cda document header)</div>
	 * <div class="fr"></div>
	 * 
	 * @param doc
	 *            the CDA-CH Object in its MDHT representation
	 * @param stylesheet
	 *            the stylesheet for the document (e.g.
	 *            '../../../../stylesheets/HL7.ch/CDA-CH/v1.2/cda-ch.xsl').
	 * @param css
	 *            the Cascasing stylesheet for the document (e.g.
	 *            '../../../../stylesheets/HL7.ch/CDA-CH/v1.2/cda-ch.xsl').
	 */
	public CdaCh(CDACH doc, String stylesheet, String css) {
		this.doc = doc;
		docRoot = CDAFactory.eINSTANCE.createDocumentRoot();
		docRoot.setClinicalDocument(doc);

		if (css != null) {
			addCss(css);
		}
		addStylesheet(stylesheet);
		initCdaCh();
	}

	/**
	 * <div class="de">Erstellt ein CdaCh Objekt mittels eines IHE DocumentRoot
	 * Objekts</div> <div class="fr"></div>
	 *
	 * @param root
	 *            <div class="de">DocumentRoot</div> <div class="fr"></div>
	 */
	public CdaCh(DocumentRoot root) {
		docRoot = root;
	}

	public void setInFulfillmentOf(Identificator id) {
		InFulfillmentOf ifo = CDAFactory.eINSTANCE.createInFulfillmentOf();
		Order o = CDAFactory.eINSTANCE.createOrder();
		o.getIds().add(id.getIi());

		ifo.setOrder(o);
		doc.getInFulfillmentOfs().add(ifo);
	}

	/**
	 * Fügt dem CDA Dokument einen Unterzeichner hinzu
	 * 
	 * @param authenticator
	 *            Unterzeichner
	 */
	public void addAuthenticator(
			org.ehealth_connector.common.Author authenticator) {
		Authenticator auth = CDAFactory.eINSTANCE.createAuthenticator();
		AssignedEntity entity = CDAFactory.eINSTANCE.createAssignedEntity();

		auth.setAssignedEntity(entity);
		entity.setAssignedPerson(authenticator.copyMdhtAuthor()
				.getAssignedAuthor().getAssignedPerson());

		doc.getAuthenticators().add(auth);
	}

	/**
	 * Fügt dem CDA Dokument einen Unterzeichner hinzu
	 * 
	 * @param authenticator
	 *            Unterzeichner
	 */
	public void addAuthenticator(Person authenticator) {
		Authenticator auth = CDAFactory.eINSTANCE.createAuthenticator();
		AssignedEntity entity = CDAFactory.eINSTANCE.createAssignedEntity();

		auth.setAssignedEntity(entity);
		entity.setAssignedPerson(authenticator.copyMdhtPerson());

		doc.getAuthenticators().add(auth);
	}

	/**
	 * Fügt einen Autoren hinzu.
	 * 
	 * @param author
	 *            Der Autor
	 */
	public void addAuthor(org.ehealth_connector.common.Author author) {
		Author docAuthor = author.copyMdhtAuthor();
		doc.getAuthors().add(docAuthor);
	}

	/**
	 * Fügt ein Cascading Stylesheet (CSS) zu den XML Processing Instructions
	 * hinzu
	 * 
	 * @param css
	 *            path to the CSS file
	 */
	public void addCss(String css) {
		// Add the stylesheet processing instructions to the document
		FeatureMapUtil.addProcessingInstruction(docRoot.getMixed(),
				"xml-stylesheet", "type=\"text/css\" href=\"" + css + "\"");
	}

	/**
	 * Fügt dem CDA Dokument einen Erfasser hinzu
	 * 
	 * @param dataEnterer
	 *            Erfasser oder Sachbearbeiter/-in, welche(r) das Dokument
	 *            erstellt oder Beiträge dazu geliefert hat.
	 */
	public void addDataEnterer(Person dataEnterer) {
		DataEnterer enterer = CDAFactory.eINSTANCE.createDataEnterer();
		AssignedEntity entity = CDAFactory.eINSTANCE.createAssignedEntity();

		enterer.setAssignedEntity(entity);
		entity.setAssignedPerson(dataEnterer.copyMdhtPerson());

		doc.getDataEnterer().setAssignedEntity(entity);
	}

	/**
	 * Fügt eine Versicherung hinzu
	 * 
	 * @param versicherung
	 *            Die Versicherung als Organization Objekt
	 */
	public void addInsurance(Organization versicherung) {
		addParticipant(versicherung, ParticipantType.Insurance);
	}

	/**
	 * Fügt dem CDA Dokument eine Partizipation hinzu
	 * 
	 * @param organization
	 *            Organisation
	 * @param participantType
	 *            Art der Partizipation (z.B. Versicherung)
	 */
	public void addParticipant(Organization organization,
			ParticipantType participantType) {
		// Set the given organization as Participant of this document.
		final Participant1 participant = CDAFactory.eINSTANCE
				.createParticipant1();
		doc.getParticipants().add(participant);
		final AssociatedEntity assEnt = CDAFactory.eINSTANCE
				.createAssociatedEntity();
		participant.setAssociatedEntity(assEnt);

		org.openhealthtools.mdht.uml.cda.Organization docOrganization = CDAFactory.eINSTANCE
				.createOrganization();
		docOrganization = organization.getMdhtOrganization();
		assEnt.setScopingOrganization(docOrganization);
	}

	/**
	 * Fügt ein Stylesheet zu den XML Processing Instructions hinzu
	 * 
	 * @param stylesheet
	 *            Path to the stylesheet (e.g.
	 *            '../../../../stylesheets/HL7.ch/CDA-CH/v1.2/cda-ch.xsl')
	 */
	public void addStylesheet(String stylesheet) {
		if (stylesheet != null) {
			// Add the stylesheet processing instructions to the document
			FeatureMapUtil.addProcessingInstruction(docRoot.getMixed(),
					"xml-stylesheet", "type=\"text/xsl\" href=\"" + stylesheet
							+ "\"");
		}
	}

	/**
	 * Gibt alle Unterzeichner des Dokuments zurück
	 * 
	 * @return die rechtlichen Unterzeichner
	 */
	public ArrayList<org.ehealth_connector.common.Person> getAuthenticators() {
		ArrayList<org.ehealth_connector.common.Person> persons = new ArrayList<org.ehealth_connector.common.Person>();
		for (Authenticator mAutor : doc.getAuthenticators()) {
			org.ehealth_connector.common.Person person = new org.ehealth_connector.common.Person(
					mAutor.getAssignedEntity().getAssignedPerson());
			persons.add(person);
		}
		return persons;
	}

	/**
	 * Gibt den Autor des Dokuments zurück
	 * 
	 * @return das eHealthConnector Author Objekt
	 */
	public org.ehealth_connector.common.Author getAuthor() {
		org.ehealth_connector.common.Author author = new org.ehealth_connector.common.Author(
				doc.getAuthors().get(0));
		return author;
	}

	/**
	 * Gibt alle Autoren des Dokuments zurück
	 * 
	 * @return das eHealthConnector Author Objekt
	 */
	public ArrayList<org.ehealth_connector.common.Author> getAuthors() {
		ArrayList<org.ehealth_connector.common.Author> authors = new ArrayList<org.ehealth_connector.common.Author>();
		for (Author mAutor : doc.getAuthors()) {
			org.ehealth_connector.common.Author author = new org.ehealth_connector.common.Author(
					mAutor);
			authors.add(author);
		}
		return authors;
	}

	/**
	 * Gibt den Verantwortlichen für das Dokument zurück
	 * 
	 * @return das openHealthTools Custodian Objekt
	 */
	public Custodian getCustodian() {
		return doc.getCustodian();
	}

	/**
	 * Gibt alle Autoren des Dokuments zurück
	 * 
	 * @return das eHealthConnector Author Objekt
	 */
	public org.ehealth_connector.common.Person getDataEnterer() {
		if (doc.getDataEnterer() != null) {
			if (doc.getDataEnterer().getAssignedEntity() != null) {
				if (doc.getDataEnterer().getAssignedEntity()
						.getAssignedPerson() != null) {
					org.ehealth_connector.common.Person person = new org.ehealth_connector.common.Person(
							doc.getDataEnterer().getAssignedEntity()
									.getAssignedPerson());
					return person;
				}
			}
		}
		return null;
	}

	public Identificator getId() {
		if (doc.getId() != null) {
			return new Identificator(doc.getId());
		}
		return null;
	}

	/**
	 * Gibt alle Versicherungen zurück
	 * 
	 * @param versicherung
	 *            Die Versicherung als Organization Objekt
	 * @return the arraylist of organizations
	 */
	public ArrayList<Organization> getInsurances(Organization versicherung) {
		ArrayList<Organization> organizations = new ArrayList<Organization>();
		for (Participant1 part : doc.getParticipants()) {
			if (part.getTypeCode().equals(ParticipantType.Insurance)) {
				Organization org = new Organization(part.getAssociatedEntity()
						.getScopingOrganization());
				organizations.add(org);
			}
		}
		return organizations;
	}

	public CS getLanuageCode() {
		if (doc.getLanguageCode() != null) {
			return doc.getLanguageCode();
		}
		return null;
	}

	/**
	 * Gets the legal authenticator of the document <div class="de">Gibt den
	 * juristisch verantwortlichen Unterzeichner des Dokuments zurück</div>
	 * 
	 * @return the legal authenticator
	 */
	public Person getLegalAuthenticator() {
		LegalAuthenticator la = doc.getLegalAuthenticator();
		Person p = new Person(la.getAssignedEntity().getAssignedPerson());
		return p;
	}

	public ByteArrayOutputStream getOutputStream() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			CDAUtil.save(doc, baos);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return baos;
	}

	/**
	 * Gibt alle Participants zurück
	 * 
	 * @param versicherung
	 *            Die Versicherung als Organization Objekt
	 * @return the arraylist of organizations
	 */
	public ArrayList<Organization> getParticipants(Organization versicherung) {
		ArrayList<Organization> organizations = new ArrayList<Organization>();
		for (Participant1 part : doc.getParticipants()) {
			Organization org = new Organization(part.getAssociatedEntity()
					.getScopingOrganization());
			organizations.add(org);
		}
		return organizations;
	}

	/**
	 * Liefert das Patientenobjekt zurück
	 *
	 * @return the patient
	 */
	public Patient getPatient() {
		Patient patient = new Patient(doc.getRecordTargets().get(0));
		return patient;
	}

	public Identificator getSetId() {
		if (doc.getSetId() != null) {
			return new Identificator(doc.getSetId().getRoot(), doc.getSetId()
					.getExtension());
		}
		return null;
	}

	public Date getTimestamp() {
		if (doc.getEffectiveTime() != null) {
			return DateUtil.parseDate(doc.getEffectiveTime());
		}
		return null;
	}

	public String getTitle() {
		return doc.getTitle().getText();
	}

	public Integer getVersion() {
		if (doc.getVersionNumber() != null) {
			return doc.getVersionNumber().getValue().intValue();
		}
		return null;
	}

	/**
	 * Gibt die XML-Repräsentation des Dokuments auf der Konsole aus
	 */
	public void printXmlToConsole() {
		try {
			CDAUtil.save(doc, System.out);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Speichert das CDA Dokument als XML Datei
	 * 
	 * @param fileName
	 *            Dateiname (inkl. Pfadangaben)
	 * @throws Exception
	 *             the exception
	 */
	public void saveToFile(String fileName) throws Exception {
		File yourFile = new File(fileName);
		if (!yourFile.exists()) {
			yourFile.createNewFile();
		}
		FileOutputStream oFile = new FileOutputStream(yourFile, false);

		// create emf resource
		CDAResource resource = (CDAResource) CDAResource.Factory.INSTANCE
				.createResource(URI.createURI(CDAPackage.eNS_URI));

		// add the document root to the resource
		docRoot.setClinicalDocument(doc);
		resource.getContents().add(docRoot);

		// save resource to console
		resource.save(oFile, null);
	}

	/**
	 * Gets the Confidentially Code
	 * 
	 * @return code
	 */
	public Confidentiality getConfidentialityCode() {
		if (doc.getConfidentialityCode() != null) {
			return Confidentiality.getEnum(doc.getConfidentialityCode()
					.getCode());
		}
		return null;
	}

	/**
	 * Sets Confidentially Code
	 * 
	 * @param code
	 *            If null, "N" for "normal" will be set.
	 */
	public void setConfidentialityCode(Confidentiality code) {
		CE confidentialityCode;
		if (code == null) {
			confidentialityCode = Confidentiality.NORMAL.getCE();
		} else {
			confidentialityCode = code.getCE();
		}
		doc.setConfidentialityCode(confidentialityCode);
	}

	/**
	 * Weist dem CDA Dokument die verwaltende Organisation zu
	 * 
	 * @param organization
	 *            verwaltende Organisation
	 */
	public void setCustodian(Organization organization) {
		// create and set the mdht Custodian object
		final Custodian mdhtCustodian = CDAFactory.eINSTANCE.createCustodian();
		doc.setCustodian(mdhtCustodian);

		final AssignedCustodian assCust = CDAFactory.eINSTANCE
				.createAssignedCustodian();
		mdhtCustodian.setAssignedCustodian(assCust);

		mdhtCustodian
				.getAssignedCustodian()
				.setRepresentedCustodianOrganization(
						Util.createCustodianOrganizationFromOrganization(organization));

		// Setzt die GLN des Arztes
		II id = DatatypesFactory.eINSTANCE.createII();
		if (organization.getMdhtOrganization().getIds().size() > 0) {
			id = organization.getMdhtOrganization().getIds().get(0);
		}
		mdhtCustodian.getAssignedCustodian()
				.getRepresentedCustodianOrganization().getIds().add(id);
	}

	public void setId(Identificator id) {
		if (id == null) {
			II docID = DatatypesFactory.eINSTANCE.createII();
			docID.setRoot("2.16.756.5.30.1.1.1.1");
			docID.setExtension(UUID.generate());
			doc.setId(docID);
		} else {
			doc.setId(id.getIi());
		}
	}

	public void setLanguageCode(LanguageCode language) {
		// Set language of the document
		doc.setLanguageCode(language.getCS());
	}

	/**
	 * Weist dem CDA Dokument einen rechtsgültigen Unterzeichner hinzu
	 * 
	 * @param legalAuthenticator
	 *            rechtsgültiger Unterzeichner
	 */
	public void setLegalAuthenticator(
			org.ehealth_connector.common.Author legalAuthenticator) {
		doc.setLegalAuthenticator(Util
				.createLagalAuthenticatorFromAuthor(legalAuthenticator));
	}

	/**
	 * Weist dem CDA Dokument den Patienten zu
	 * 
	 * @param patient
	 *            Patient
	 */
	public void setPatient(Patient patient) {
		doc.getRecordTargets().add(patient.getMdhtRecordTarget());
	}

	public DocumentRoot getDocRoot() {
		return this.docRoot;
	}

	public void setTimestamp(Date date) {
		if (date == null) {
			doc.setEffectiveTime(DateUtil.nowAsTS());
		} else {
			try {
				doc.setEffectiveTime(DateUtil.createTSFromEuroDate(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setVersion(String guidVersion1, Integer version) {
		setSetId(guidVersion1);
		setVersionNumber(version);
	}

	protected void setSetId(String id) {
		if (id == null) {
			doc.setSetId(EcoreUtil.copy(doc.getId()));
		} else {
			II ii = DatatypesFactory.eINSTANCE.createII();
			ii.setRoot("2.16.756.5.30.1.1.1.1");
			ii.setExtension(id);
			doc.setSetId(ii);
		}
	}

	protected void setTitle(String title) {
		ST titleSt = DatatypesFactory.eINSTANCE.createST();
		titleSt.addText(title);
		doc.setTitle(titleSt);
	}

	protected void setVersionNumber(Integer number) {
		INT i = DatatypesFactory.eINSTANCE.createINT();
		if (number == null) {
			i.setValue(1);
		} else {
			i.setValue(number);
		}
		doc.setVersionNumber(i);
	}

	/**
	 * Sets the TypeId to the default value ("2.16.840.1.113883.1.3",
	 * "POCD_HD000040")
	 */
	private void setTypeId() {
		// Set Type ID
		// Identifies the Type of the xml document
		InfrastructureRootTypeId typeId = CDAFactory.eINSTANCE
				.createInfrastructureRootTypeId();
		doc.setTypeId(typeId);
		typeId.setRoot("2.16.840.1.113883.1.3");
		typeId.setExtension("POCD_HD000040");
	}

	public void initCdaCh() {
		// Set the eHealthConnector comment
		FeatureMapUtil.addComment(
				docRoot.getMixed(),
				"Document based on CDA R2 generated by "
						+ currentEhcVersion.getSystemVersionName()
						+ ", Release Date "
						+ currentEhcVersion.getReleaseDate());

		// Add the stylesheet processing instructions to the document root using
		// featuremaputil
		// set xml namespace
		docRoot.getXMLNSPrefixMap().put("", CDAPackage.eNS_URI);

		// Set OID of the document
		setId(null);
		setVersion(null, null);

		setConfidentialityCode(null);

		// Set creation time of the document
		setTimestamp(null);

		// Fix RealmCode
		CS cs = DatatypesFactory.eINSTANCE.createCS();
		cs.setCode("CH");
		doc.getRealmCodes().clear();
		doc.getRealmCodes().add(cs);

		// Type ID
		setTypeId();
	}
}
