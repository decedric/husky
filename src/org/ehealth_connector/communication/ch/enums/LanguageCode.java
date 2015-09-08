package org.ehealth_connector.communication.ch.enums;

import java.util.Arrays;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.XdsUtil;
import org.openhealthtools.ihe.xds.metadata.CodedMetadataType;
import org.openhealthtools.ihe.xds.metadata.MetadataFactory;

/*
*<div class="de">Definiert die Sprache in welcher das Dokument verfasst wurde. Die Werte richten sich nach der IETF RFC 3066 Norm.</div>
*<div class="fr"></div>
*/
public enum LanguageCode implements CodedMetadataEnumInterface {

	/** 
	*<div class="de">Deutsch</div>
	*<div class="fr">Allemand</div>
	*<div class="it">Tedesco</div>
	*/
	DEUTSCH ("de-CH", "Deutsch"),
	/** 
	*<div class="de">Französisch</div>
	*<div class="fr">Français </div>
	*<div class="it">Francese</div>
	*/
	FRANZÖSISCH ("fr-CH", "Französisch"),
	/** 
	*<div class="de">Italienisch</div>
	*<div class="fr">Italien</div>
	*<div class="it">Italiano</div>
	*/
	ITALIENISCH ("it-CH", "Italienisch"),
	/** 
	*<div class="de">Rätoromanisch</div>
	*<div class="fr">Rhéto-roman</div>
	*<div class="it">Romancio</div>
	*/
	RÄTOROMANISCH ("rm", "Rätoromanisch"),
	/** 
	*<div class="de">Englisch</div>
	*<div class="fr">Anglais</div>
	*<div class="it">Inglese</div>
	*/
	ENGLISCH ("en-US", "Englisch");

	/** 
	*<div class="de">Code für Deutsch</div>
	*<div class="fr">Code de Allemand</div>
	*<div class="it">Code per Tedesco</div>
	*/
	public static final String DEUTSCH_CODE="de-CH";

	/** 
	*<div class="de">Code für Französisch</div>
	*<div class="fr">Code de Français </div>
	*<div class="it">Code per Francese</div>
	*/
	public static final String FRANZÖSISCH_CODE="fr-CH";

	/** 
	*<div class="de">Code für Italienisch</div>
	*<div class="fr">Code de Italien</div>
	*<div class="it">Code per Italiano</div>
	*/
	public static final String ITALIENISCH_CODE="it-CH";

	/** 
	*<div class="de">Code für Rätoromanisch</div>
	*<div class="fr">Code de Rhéto-roman</div>
	*<div class="it">Code per Romancio</div>
	*/
	public static final String RÄTOROMANISCH_CODE="rm";

	/** 
	*<div class="de">Code für Englisch</div>
	*<div class="fr">Code de Anglais</div>
	*<div class="it">Code per Inglese</div>
	*/
	public static final String ENGLISCH_CODE="en-US";



	/**
	* <div class="en">Identifier of the Code System</div>
	* <div class="de">Identifikator für das Code System</div>
	*/
	public static final String CODE_SYSTEM_OID="2.16.756.5.30.1.127.3.10.1.13";

	/**
	* <div class="en">Name of the Code System</div>
	* <div class="de">Name des Codes Systems</div>
	*/
	public static final String CODE_SYSTEM_NAME="epd_xds";


	/**
	* <div class="en">Machine interpretable and (inside this class) unique code</div>
	* <div class="de">Maschinen interpretierbarer und (innerhalb dieser Klasse) eindeutiger Code</div>
	*/
	protected String code;

	/**
	* <div class="en">Human readable name</div>
	* <div class="de">Menschenlesbarer Name</div>
	*/
	protected String displayName;
	
	/**
	* <div class="en">Instantiates this Enum Object with a given Code and Display Name</div>
	* <div class="de">Instantiiert dieses Enum Object mittels eines Codes und einem Display Name</div>
	*
	* @param code <br>
	*	<div class="de"> code</div>
	* @param displayName <br>
	*	<div class="de"> display name</div>
	*/
	private LanguageCode (String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
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
	* <div class="en">Gets the OHT CodedMetadataType Object</div>
	* <div class="de">Liefert das OHT CodedMetadataType Objekt</div>
	*
	* @return <div class="en">the codedMetadataType</div>
	*/
	public CodedMetadataType getCodedMetadataType() {
		CodedMetadataType cmt = MetadataFactory.eINSTANCE.createCodedMetadataType();
		cmt.setSchemeName(CODE_SYSTEM_OID);
		cmt.setCode(this.getCodeValue());
		cmt.setDisplayName(XdsUtil.createInternationalString(this.getDisplayName(), "de-ch"));
		return cmt;
	}

	/**
	* <div class="en">Gets the code system name.</div>
	* <div class="de">Liefert code system name.</div>
	*
	* @return <div class="en">the code system name</div>
	*/
	public String getCodeSystemName() {
		return CODE_SYSTEM_NAME;
	}

	/**
	* <div class="en">Gets the code system id.</div>
	* <div class="de">Liefert die code system id.</div>
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
	* <div class="en">Gets the display name.</div>
	* <div class="de">Liefert display name.</div>
	*
	* @return <div class="en">the display name</div>
	*/
	public String getDisplayName() {
		return this.displayName;
	}
 
	/**
	* <div class="en">Gets the Enum with a given code</div>
	* <div class="de">Liefert den Enum anhand eines gegebenen codes</div>
	*
	* @param code <br>
	*      <div class="de"> code</div>
	* @return <div class="en">the enum</div>
	*/
	public static LanguageCode getEnum(String code) {
		for (LanguageCode x : values()) {
			if (x.getCodeValue().equals(code)) {
				return x;
			}
		}
		return null;
	}
  
	/**
	* <div class="en">Checks if a given enum is part of this value set.</div>
	* <div class="de">Prüft, ob der gegebene enum Teil dieses Value Sets ist.</div>
	*
	*
	* @param enumName <br>
	*      <div class="de"> enumName</div>
	* @return true, if enum is in this value set
	*/
	public boolean isEnumOfValueSet(String enumName) {
		return Arrays.asList(values()).contains(enumName);
	}

	/**
	* <div class="en">Checks if a given code value is in this value set.</div>
	* <div class="de">Prüft, ob der gegebene code in diesem Value Sets vorhanden ist.</div>
	*
	* @param codeValue <br>
	*      <div class="de"> code</div>
	* @return true, if is in value set
	*/
	public boolean isInValueSet(String codeValue) {
		for (LanguageCode x : values()) {
			if (x.getCodeValue().equals(code)) {
				return true;
			}
		}
		return false;
	}

}