package org.ehealth_connector.cda.ihe.lab.enums;

import org.ehealth_connector.common.Code;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;

/*
*<div class="de">List von zulässigen Mime Types für XDS-Lab</div>
*<div class="fr"></div>
*/
public enum LabObservationMediaMimeType {

	/**
	 * <div class="en">BMP Image</div> <div class="de">BMP Image</div>
	 */
	BMP_IMAGE("image/bmp", "BMP Image"),
	/**
	 * <div class="en">GIF Image</div> <div class="de">GIF Image</div>
	 */
	GIF_IMAGE("image/gif", "GIF Image"),
	/**
	 * <div class="en">JPEG Image</div> <div class="de">JPEG Image</div>
	 */
	JPEG_IMAGE("image/jpeg", "JPEG Image"),
	/**
	 * <div class="en">PNG Image</div> <div class="de">PNG Image</div>
	 */
	PNG_IMAGE("image/png", "PNG Image");

	/**
	 * <div class="en">Code for BMP Image</div> <div class="de">Code für BMP
	 * Image</div>
	 */
	public static final String BMP_IMAGE_CODE = "image/bmp";

	/**
	 * <div class="en">Name of the Code System</div> <div class="de">Name des
	 * Codes Systems</div>
	 */
	public static final String CODE_SYSTEM_NAME = "Mime Types for Observation Media in XDS-Lab";

	/**
	 * <div class="en">Identifier of the Code System</div>
	 * <div class="de">Identifikator für das Code System</div>
	 */
	public static final String CODE_SYSTEM_OID = "";

	/**
	 * <div class="en">Code for GIF Image</div> <div class="de">Code für GIF
	 * Image</div>
	 */
	public static final String GIF_IMAGE_CODE = "image/gif";

	/**
	 * <div class="en">Code for JPEG Image</div> <div class="de">Code für JPEG
	 * Image</div>
	 */
	public static final String JPEG_IMAGE_CODE = "image/jpeg";

	/**
	 * <div class="en">Code for PNG Image</div> <div class="de">Code für PNG
	 * Image</div>
	 */
	public static final String PNG_IMAGE_CODE = "image/png";

	/**
	 * <div class="en">Gets the Enum with a given code</div>
	 * <div class="de">Liefert den Enum anhand eines gegebenen codes</div>
	 *
	 * @param code
	 *          <br>
	 *          <div class="de"> code</div>
	 * @return <div class="en">the enum</div>
	 */
	public static LabObservationMediaMimeType getEnum(String code) {
		for (LabObservationMediaMimeType x : values()) {
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
			Enum.valueOf(LabObservationMediaMimeType.class, enumName);
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
	 *          <br>
	 *          <div class="de"> code</div>
	 * @return true, if is in value set
	 */
	public static boolean isInValueSet(String codeValue) {
		for (LabObservationMediaMimeType x : values()) {
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
	protected String code;

	/**
	 * <div class="en">Human readable name</div> <div class="de">Menschenlesbarer
	 * Name</div>
	 */
	protected String displayName;

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
	private LabObservationMediaMimeType(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}

	/**
	 * <div class="en">Gets the Code of this Enum as MDHT Object.</div>
	 * <div class="de">Liefert den Code dieses Enum als MDHT Objekt.</div>
	 *
	 * @return <div class="en">The MDHT Code</div>
	 */
	public CD getCD() {
		CD cd = DatatypesFactory.eINSTANCE.createCD();
		cd.setCodeSystem(CODE_SYSTEM_OID);
		cd.setCode(code);
		cd.setDisplayName(displayName);
		return cd;
	}

	/**
	 * <div class="en">Gets the ehealthconnector Code Object</div>
	 * <div class="de">Liefert das ehealthconnector Code Objekt</div>
	 *
	 * @return <div class="en">the code</div>
	 */
	public Code getCode() {
		Code ehcCode = new Code(CODE_SYSTEM_OID, code, displayName);
		return ehcCode;
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