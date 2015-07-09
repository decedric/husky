package org.ehealth_connector.cda;

import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.ehealth_connector.cda.ch.CdaCh;
import org.ehealth_connector.cda.enums.StatusCode;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.Util;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.ExternalDocument;
import org.openhealthtools.mdht.uml.cda.Reference;
import org.openhealthtools.mdht.uml.cda.ch.CDACHBodyExtRef;
import org.openhealthtools.mdht.uml.cda.ch.CDACHMSETBodyImmunizationL3Reason;
import org.openhealthtools.mdht.uml.cda.ch.CHFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.vocab.ActClassDocument;
import org.openhealthtools.mdht.uml.hl7.vocab.ActMood;

/**
 * Represents a Reason
 */
public class Reason {

	private final CDACHMSETBodyImmunizationL3Reason mReason;
	private CDACHBodyExtRef mExtRef = null;

	/**
	 * Standard constructor
	 */
	public Reason() {
		mReason = CHFactory.eINSTANCE.createCDACHMSETBodyImmunizationL3Reason().init();

		// Fix Template ID
		for (II i : mReason.getTemplateIds()) {
			if (i.getRoot().equals("2.16.756.5.30.1.1.1.1.3.2.1")) {
				i.setExtension("CDA-CH.VACD.Body.MediL3.Reason");
			}
		}
		// Set Status Code
		mReason.setStatusCode(StatusCode.COMPLETED.getCS());
	}

	/**
	 * Instantiates a Reason with a given mdht reason object
	 * 
	 * @param reason
	 *          the reason object
	 */
	public Reason(CDACHMSETBodyImmunizationL3Reason reason) {
		mReason = reason;
		// Check if there are references to an extern document. If so,
		// initalialize the class member.
		for (Reference r : reason.getReferences())
			for (II id : r.getTemplateIds()) {
				if (id.getRoot().equals(CdaCh.OID_V1)) {
					mExtRef = (CDACHBodyExtRef) r;
				}
			}
	}

	/**
	 * Instantiates a Reason object with a code
	 * 
	 * @param code
	 *          the code
	 */
	public Reason(Code code) {
		this();
		setCode(code);
		mReason.setCode(code.getCD());
	}

	/**
	 * Instantiates a Reason with a given code, a reference and an ID
	 * 
	 * @param code
	 *          the code
	 * @param reference
	 *          the reference
	 * @param id
	 *          the id. If null, an ID will be generated
	 */
	public Reason(Code code, URL reference, String id) {
		this(code);
		initExtRef();
		setReference(reference);
		setReferenceId(id);
	}

	/**
	 * Gets a copy of the mdht CDACHBodyExtRef object
	 * 
	 * @return the CDACHBodyExtRef
	 */
	public CDACHBodyExtRef copyMdhtCDACHBodyExtRef() {
		return EcoreUtil.copy(mExtRef);
	}

	/**
	 * Gets a copy of the mdht CDACHMSETBodyImmunizationL3Reason
	 * 
	 * @return the CDACHMSETBodyImmunizationL3Reason
	 */
	public CDACHMSETBodyImmunizationL3Reason copyMdhtCDACHMSETBodyImmunizationL3Reason() {
		return EcoreUtil.copy(mReason);
	}

	/**
	 * Gets the code
	 * 
	 * @return the code
	 */
	public Code getCode() {
		return new Code(mReason.getCode());
	}

	/**
	 * Gets the CDACHBodyExtRef
	 * 
	 * @return the CDACHBodyExtRef
	 */
	public CDACHBodyExtRef getMdhtCDACHBodyExtRef() {
		return mExtRef;
	}

	/**
	 * Gets the CDACHMSETBodyImmunizationL3Reason
	 * 
	 * @return the CDACHMSETBodyImmunizationL3Reason
	 */
	public CDACHMSETBodyImmunizationL3Reason getMdhtCDACHMSETBodyImmunizationL3Reason() {
		return mReason;
	}

	/**
	 * Gets the Reference to an external object as string
	 * 
	 * @return the reference
	 */
	public String getReference() {
		if (mExtRef != null && mExtRef.getExternalDocument() != null
				&& mExtRef.getExternalDocument().getText() != null) {
			return mExtRef.getExternalDocument().getText().getReference().getValue();
		} else
			return null;
	}

	/**
	 * Gets the id of the reference as string
	 * 
	 * @return the reference id
	 */
	public String getReferenceId() {
		if (mExtRef != null && mExtRef.getExternalDocument() != null
				&& mExtRef.getExternalDocument().getText() != null
				&& mExtRef.getExternalDocument().getIds().get(0) != null) {
			return mExtRef.getExternalDocument().getIds().get(0).getExtension();
		} else {
			return null;
		}
	}

	public void setCode(Code code) {
		mReason.setCode(code.getCD());
	}

	/**
	 * Sets a reference to an external Document.
	 * 
	 * @param reference
	 *          The Reference URL (e.g.
	 *          'http://www.bag.admin.ch/ekif/04423/04428/index.html?lang=de')
	 */
	public void setReference(URL reference) {
		if (mExtRef == null) {
			initExtRef();
		}

		mExtRef.getExternalDocument().setText(Util.createReference(reference.toString()));
	}

	/**
	 * Sets the reference id
	 * 
	 * @param id
	 *          the if of the reference (if null, an id will be generated)
	 */
	public void setReferenceId(String id) {
		if (mExtRef == null) {
			initExtRef();
		}

		// Set the id or generate if null
		II docIi = DatatypesFactory.eINSTANCE.createII();
		docIi.setRoot("2.16.756.5.30.1.1.1.1.3.6.21");
		if (id == null) {
			docIi.setExtension(org.openhealthtools.ihe.utils.UUID.generate());
		} else {
			docIi.setExtension(id);
		}
		mExtRef.getExternalDocument().getIds().clear();
		mExtRef.getExternalDocument().getIds().add(docIi);
	}

	private void initExtRef() {
		mExtRef = CHFactory.eINSTANCE.createCDACHBodyExtRef().init();
		ExternalDocument e = CDAFactory.eINSTANCE.createExternalDocument();
		mReason.getReferences().add(mExtRef);
		mExtRef.setExternalDocument(e);

		// Fix Template ID
		mExtRef.getTemplateIds().clear();
		II ii = DatatypesFactory.eINSTANCE.createII(CdaCh.OID_V1, "CDA-CH.Body.ExtRef");
		mExtRef.getTemplateIds().add(ii);

		// Set attributes
		e.setClassCode(ActClassDocument.DOC);
		e.setMoodCode(ActMood.EVN);
		setReferenceId(null);
	}
}