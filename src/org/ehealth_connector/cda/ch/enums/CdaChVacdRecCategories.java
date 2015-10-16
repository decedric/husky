package org.ehealth_connector.cda.ch.enums;

import java.util.Arrays;

import org.ehealth_connector.common.Code;

/**
 * Value Set valid from 20150101 Value-Set: CDA-CH-VACD rec-categories (OID:
 * 2.16.756.5.30.1.127.3.3.4)
 */
public enum CdaChVacdRecCategories {

	/** The rec base. */
	REC_BASE("41501", "Empfohlene Basisimpfungen", "Vaccinations recommandées de base",
			"Vaccinazioni raccomandate di base"),

	/** The rec compl. */
	REC_COMPL("41502", "Empfohlene ergänzende Impfungen",
			"Vaccinations recommandées complémentaires", "Vaccinazioni raccomandate complementari"),

	/** The rec risk. */
	REC_RISK("41503", "Empfohlene Impfungen für Risikogruppen",
			"Vaccinations recommandées à des groupes à risque",
			"Vaccinazioni raccomandate a die gruppi a rischio"),

	/** The no rec. */
	NO_REC("41504", "Impfungen ohne Empfehlungen", "Vaccinations sans recommandation d’utilisation",
			"Vaccinazioni senza raccomandazione d’utilizzo");

	/** The Constant CODE_SYSTEM_OID. */
	public static final String CODE_SYSTEM_OID = "2.16.756.5.30.1.127.3.3.4";

	/** The Constant CODE_SYSTEM_NAME. */
	public static final String CODE_SYSTEM_NAME = "CDA-CH-VACD rec-categories";

	/** The code. */
	private String code;

	/** The display name. */
	private String displayNameDe;

	/** The display name. */
	private String displayNameFr;

	/** The display name. */
	private String displayNameIt;

	/**
	 * <div class="en">Instantiates this Enum Object with a given Code and
	 * Display Name</div> <div class="de">Instantiiert dieses Enum Object
	 * mittels eines Codes und einem Display Name</div>.
	 *
	 * @param code
	 *            <br>
	 *            <div class="de"> code</div>
	 * @param displayNameDe
	 *            the display name de
	 * @param displayNameFr
	 *            the display name fr
	 * @param displayNameIt
	 *            the display name it
	 */
	private CdaChVacdRecCategories(String code, String displayNameDe, String displayNameFr,
			String displayNameIt) {
		this.code = code;
		this.displayNameDe = displayNameDe;
		this.displayNameFr = displayNameFr;
		this.displayNameIt = displayNameIt;
	}

	/**
	 * <div class="en">Gets the Enum with a given code</div>
	 * <div class="de">Liefert den Enum anhand eines gegebenen codes</div>.
	 *
	 * @param code
	 *            <br>
	 *            <div class="de"> code</div>
	 * @return <div class="en">the enum</div>
	 */
	public static CdaChVacdRecCategories getEnum(String code) {
		for (CdaChVacdRecCategories x : values()) {
			if (x.getCodeValue().equals(code)) {
				return x;
			}
		}
		return null;
	}

	/**
	 * <div class="en">Gets the ehealthconnector Code Object</div>
	 * <div class="de">Liefert das ehealthconnector Code Objekt</div>.
	 *
	 * @return <div class="en">the code</div>
	 */
	public Code getCode(LanguageCode languageCode) {
		String displayName = null;
		if (languageCode != null) {
			switch (languageCode) {
			case GERMAN:
				displayName = displayNameDe;
				break;
			case FRENCH:
				displayName = displayNameFr;
				break;
			case ITALIAN:
				displayName = displayNameIt;
				break;
			}
		}
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
	 * <div class="en">Gets the code system id.</div> <div class="de">Liefert
	 * die code system id.</div>
	 * 
	 * @return <div class="en">the code system id</div>
	 */
	public String getCodeSystemOid() {
		return CODE_SYSTEM_OID;
	}

	/**
	 * <div class="en">Gets the actual Code as string</div>
	 * <div class="de">Liefert den eigentlichen Code als String</div>.
	 *
	 * @return <div class="en">the code</div>
	 */
	public String getCodeValue() {
		return code;
	}

	/**
	 * <div class="en">Checks if a given enum is part of this value set.</div>
	 * <div class="de">Prüft, ob der gegebene enum Teil dieses Value Sets
	 * ist.</div>
	 * 
	 * 
	 * @param enumName
	 *            <br>
	 *            <div class="de"> enumName</div>
	 * @return true, if enum is in this value set
	 */
	public boolean isEnumOfValueSet(String enumName) {
		return Arrays.asList(values()).contains(enumName);
	}

	/**
	 * <div class="en">Checks if a given code value is in this value set.</div>
	 * <div class="de">Prüft, ob der gegebene code in diesem Value Sets
	 * vorhanden ist.</div>
	 * 
	 * @param codeValue
	 *            <br>
	 *            <div class="de"> code</div>
	 * @return true, if is in value set
	 */
	public boolean isInValueSet(String codeValue) {
		for (CdaChVacdRecCategories x : values()) {
			if (x.getCodeValue().equals(code)) {
				return true;
			}
		}
		return false;
	}

}