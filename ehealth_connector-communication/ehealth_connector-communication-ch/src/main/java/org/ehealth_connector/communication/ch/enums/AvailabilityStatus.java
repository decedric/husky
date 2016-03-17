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
package org.ehealth_connector.communication.ch.enums;

import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.enums.CodedMetadataEnumInterface;
import org.ehealth_connector.common.utils.XdsMetadataUtil;
import org.openhealthtools.ihe.xds.metadata.AvailabilityStatusType;
import org.openhealthtools.ihe.xds.metadata.CodedMetadataType;
import org.openhealthtools.ihe.xds.metadata.MetadataFactory;

/**
 * <div class="de">Ein XDS Dokument hat immer einen Verfügbarkeitsstatus.
 * Entweder approved (genehmigt) oder deprecated (abgelehnt).</div>
 * <div class="fr"></div>
 */
public enum AvailabilityStatus implements CodedMetadataEnumInterface {

	/**
	 * <div class="de">genehmigt</div> <div class="fr">approuvé</div>
	 * <div class="it">approvato</div>
	 */
	APPROVED("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved", "Approved"),
	/**
	 * <div class="de">abgelehnt</div> <div class="fr">refusé</div>
	 * <div class="it">respinto</div>
	 */
	DEPRECATED("urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated", "Deprecated");

	/**
	 * <div class="de">Code für genehmigt</div> <div class="fr">Code de
	 * approuvé</div> <div class="it">Code per approvato</div>
	 */
	public static final String APPROVED_CODE = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";

	/**
	 * <div class="en">Name of the Code System</div> <div class="de">Name des
	 * Codes Systems</div>
	 */
	public static final String CODE_SYSTEM_NAME = "epd_xds_classCode";

	/**
	 * <div class="en">Identifier of the Code System</div>
	 * <div class="de">Identifikator für das Code System</div>
	 */
	public static final String CODE_SYSTEM_OID = "2.16.756.5.30.1.127.3.10.1.2";

	/**
	 * <div class="de">Code für abgelehnt</div> <div class="fr">Code de
	 * refusé</div> <div class="it">Code per respinto</div>
	 */
	public static final String DEPRECATED_CODE = "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated";

	/**
	 * <div class="en">Return the OHT Enum Object with a given OHT
	 * AvailabilityStatusType. Return null, if the status is not present in this
	 * object</div> <div class="de">Liefert dieses Enum Objekt anhand eines OHT
	 * AvailabilityStatusType zurück. Liefert null, wenn der Status in diesem
	 * Objekt nicht vorhanden ist.</div>
	 *
	 * @param availabilityStatusType
	 *          <div class="en">the status of the document as OHT
	 *          AvailabilityStatusType </div> <div class="de">der Dokumentenstatus
	 *          als OHT AvailabilityStatusType</div>
	 * @return <div class="en">the status of the document</div>
	 *         <div class="de">der Dokumentenstatus</div>
	 */
	public static AvailabilityStatus getByOhtAvailabilityStatusType(
			AvailabilityStatusType availabilityStatusType) {
		AvailabilityStatus retVal = null;
		if (availabilityStatusType.equals(AvailabilityStatusType.APPROVED_LITERAL)) {
			retVal = AvailabilityStatus.APPROVED;
		} else if (availabilityStatusType.equals(AvailabilityStatusType.DEPRECATED_LITERAL)) {
			retVal = AvailabilityStatus.DEPRECATED;
		}
		return retVal;
	}

	/**
	 * <div class="en">Gets the Enum with a given code</div>
	 * <div class="de">Liefert den Enum anhand eines gegebenen codes</div>
	 *
	 * @param code
	 *          <br>
	 *          <div class="de"> code</div>
	 * @return <div class="en">the enum</div>
	 */
	public static AvailabilityStatus getEnum(String code) {
		for (final AvailabilityStatus x : values()) {
			if (x.getCodeValue().equals(code)) {
				return x;
			}
		}
		return null;
	}

	/**
	 * <div class="en">Checks if a given enum is part of this value set.</div>
	 * <div class="de">Prüft, ob der gegebene enum Teil dieses Value Sets
	 * ist.</div>
	 *
	 * @param enumName
	 *          <br>
	 *          <div class="de"> enumName</div>
	 * @return true, if enum is in this value set
	 */
	public static boolean isEnumOfValueSet(String enumName) {
		if (enumName == null) {
			return false;
		}
		try {
			Enum.valueOf(AvailabilityStatus.class, enumName);
			return true;
		} catch (final IllegalArgumentException ex) {
			return false;
		}
	}

	/**
	 * <div class="en">Checks if a given code value is in this value set.</div>
	 * <div class="de">Prüft, ob der gegebene code in diesem Value Set vorhanden
	 * ist.</div>
	 *
	 * @param codeValue
	 *          <div class="de">code</div>
	 * @return true, if one enum of this valueset contains the given code
	 */
	public static boolean isInValueSet(String codeValue) {
		for (AvailabilityStatus x : values()) {
			if (x.getCodeValue().equals(codeValue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <div class="en">Machine interpretable and (inside this class) unique
	 * code</div> <div class="de">Maschinen interpretierbarer und (innerhalb
	 * dieser Klasse) eindeutiger Code</div>
	 */
	private String code;

	/**
	 * <div class="en">Human readable name</div> <div class="de">Menschenlesbarer
	 * Name</div>
	 */
	private String displayName;

	/**
	 * <div class="en">Instantiates this Enum Object with a given Code and Display
	 * Name</div> <div class="de">Instantiiert dieses Enum Object mittels eines
	 * Codes und einem Display Name</div>
	 *
	 * @param code
	 *          <br>
	 *          <div class="de"> code</div>
	 * @param displayName
	 *          <br>
	 *          <div class="de"> display name</div>
	 */
	private AvailabilityStatus(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}

	/**
	 * <div class="en">Gets the AvailabilityStatus as OHT AvailabilityStatusType
	 * Object.</div> <div class="de">Liefert AvailabilityStatus als OHT
	 * AvailabilityStatusType Objekt.</div>
	 *
	 * @return <div class="en">the address use as postal address use</div>
	 */
	public AvailabilityStatusType getAsOhtAvailabilityStatusType() {
		switch (this) {
		case APPROVED:
			return AvailabilityStatusType.APPROVED_LITERAL;
		case DEPRECATED:
			return AvailabilityStatusType.DEPRECATED_LITERAL;
		default:
			return AvailabilityStatusType.APPROVED_LITERAL;
		}
	}

	/**
	 * <div class="en">Gets the ehealthconnector Code Object</div>
	 * <div class="de">Liefert das ehealthconnector Code Objekt</div>
	 *
	 * @return <div class="en">the code</div>
	 */
	public Code getCode() {
		final Code ehcCode = new Code(CODE_SYSTEM_OID, code, displayName);
		return ehcCode;
	}

	/**
	 * <div class="en">Gets the OHT CodedMetadataType Object</div>
	 * <div class="de">Liefert das OHT CodedMetadataType Objekt</div>
	 *
	 * @return <div class="en">the codedMetadataType</div>
	 */
	@Override
	public CodedMetadataType getCodedMetadataType() {
		final CodedMetadataType cmt = MetadataFactory.eINSTANCE.createCodedMetadataType();
		cmt.setSchemeName(CODE_SYSTEM_OID);
		cmt.setCode(this.getCodeValue());
		cmt.setDisplayName(XdsMetadataUtil.createInternationalString(this.getDisplayName(), "de-ch"));
		return cmt;
	}

	/**
	 * <div class="en">Gets the code system name.</div> <div class="de">Liefert
	 * code system name.</div>
	 *
	 * @return <div class="en">the code system name</div>
	 */
	public String getCodeSystemName() {
		return CODE_SYSTEM_NAME;
	}

	/**
	 * <div class="en">Gets the code system id.</div> <div class="de">Liefert die
	 * code system id.</div>
	 *
	 * @return <div class="en">the code system id</div>
	 */
	public String getCodeSystemOid() {
		return CODE_SYSTEM_OID;
	}

	/**
	 * <div class="en">Gets the actual Code as string</div>
	 * <div class="de">Liefert den eigentlichen Code als String</div>
	 *
	 * @return <div class="en">the code</div>
	 */
	public String getCodeValue() {
		return this.code;
	}

	/**
	 * <div class="en">Gets the display name.</div> <div class="de">Liefert
	 * display name.</div>
	 *
	 * @return <div class="en">the display name</div>
	 */
	public String getDisplayName() {
		return this.displayName;
	}
}
