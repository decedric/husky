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
package org.ehealth_connector.common;

import java.util.ArrayList;

import org.ehealth_connector.common.basetypes.CodeBaseType;
import org.ehealth_connector.common.enums.NullFlavor;
import org.ehealth_connector.common.hl7cdar2.CD;
import org.ehealth_connector.common.hl7cdar2.ED;
import org.ehealth_connector.common.mdht.enums.ValueSetEnumInterface;

/**
 * <div class="en">The class Code contains all necessary fields for a coded
 * element. This class also provides mapping methods to other data types. <div>
 *
 * <div class="de">Die Klasse Code enthält alle notwendigen Felder für ein
 * codiertes Element. Diese Klasse bietet auch Zuordnungsmethoden für andere
 * Datentypen.<div>
 *
 */
public class Code extends CodeBaseType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5410972500384690838L;

	/**
	 * <div class="en">Creates the base type from the given HL7 CDA R2
	 * value.<div>
	 *
	 * <div class="de">Erstellt den Basistyp aus dem angegebenen HL7 CDA R2
	 * Wert.<div>
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 value
	 * @return the base type
	 */
	public static CodeBaseType createCodeBaseType(
			org.ehealth_connector.common.hl7cdar2.CD hl7CdaR2Value) {
		CodeBaseType retVal = new CodeBaseType();

		if (hl7CdaR2Value != null) {
			String nullFlavor = null;
			if (hl7CdaR2Value.nullFlavor != null)
				if (hl7CdaR2Value.nullFlavor.size() > 0)
					nullFlavor = hl7CdaR2Value.nullFlavor.get(0);
			if (nullFlavor != null)
				retVal.setNullFlavor(NullFlavor.getEnum(nullFlavor));
			else {

				retVal.setCode(hl7CdaR2Value.getCode());
				retVal.setCodeSystem(hl7CdaR2Value.getCodeSystem());
				retVal.setCodeSystemName(hl7CdaR2Value.getCodeSystemName());
				retVal.setCodeSystemVersion(hl7CdaR2Value.getCodeSystemVersion());
				retVal.setDisplayName(hl7CdaR2Value.getDisplayName());

				ED ed = hl7CdaR2Value.getOriginalText();
				if (ed != null)
					retVal.setOriginalText(ed.xmlContent);
			}

			for (CD item : hl7CdaR2Value.getTranslation()) {
				retVal.addCodeTranslation(createCodeBaseType(item));
			}

			// setDisplayNameTranslationList() is not implemented, because
			// it is
			// not supported by CD
		} else
			retVal.setNullFlavor(NullFlavor.NOT_AVAILABLE);

		return retVal;

	}

	/**
	 * <div class="en">Creates the HL7 CDA R2 data type from the given base
	 * type.<div>
	 *
	 * <div class="de">Erstellt den HL7 CDA R2 Datentyp aus dem angegebenen
	 * Basistyp.<div>
	 *
	 * @param baseType
	 *            the base type
	 * @return the HL7 CDA R2 data typed value
	 */
	public static org.ehealth_connector.common.hl7cdar2.CD createHl7CdaR2Cd(CodeBaseType baseType) {
		org.ehealth_connector.common.hl7cdar2.CD retVal = null;
		if (baseType != null) {
			retVal = new org.ehealth_connector.common.hl7cdar2.CD();
			String value;

			NullFlavor nf = baseType.getNullFlavor();
			if (nf != null) {
				if (retVal.nullFlavor == null)
					retVal.nullFlavor = new ArrayList<String>();
				retVal.nullFlavor.add(nf.getCodeValue());
			} else {

				value = baseType.getCode();
				if (value != null) {
					retVal.setCode(value);
				}

				value = baseType.getCodeSystem();
				if (value != null) {
					retVal.setCodeSystem(value);
				}

				value = baseType.getCodeSystemName();
				if (value != null) {
					retVal.setCodeSystemName(value);
				}

				value = baseType.getCodeSystemVersion();
				if (value != null) {
					retVal.setCodeSystemVersion(value);
				}

				value = baseType.getDisplayName();
				if (value != null) {
					retVal.setDisplayName(value);
				}

				value = baseType.getOriginalText();
				if (value != null) {
					ED ed = new ED();
					ed.xmlContent = value;
					retVal.setOriginalText(ed);
				}
			}

			for (CodeBaseType item : baseType.getCodeTranslationList()) {
				retVal.getTranslation().add(createHl7CdaR2Cd(item));
			}

			// listDisplayNameTranslations() is not implemented, because it
			// is
			// not
			// supported by CD
		}
		return retVal;

	}

	/**
	 * <div class="en">Creates the HL7 CDA R2 data type from the given base
	 * type.<div>
	 *
	 * <div class="de">Erstellt den HL7 CDA R2 Datentyp aus dem angegebenen
	 * Basistyp.<div>
	 *
	 * @param baseType
	 *            the base type
	 * @return the HL7 CDA R2 data typed value
	 */
	public static org.ehealth_connector.common.hl7cdar2.CE createHl7CdaR2Ce(CodeBaseType baseType) {
		org.ehealth_connector.common.hl7cdar2.CE retVal = null;
		if (baseType != null) {
			retVal = new org.ehealth_connector.common.hl7cdar2.CE();
			String value;

			NullFlavor nf = baseType.getNullFlavor();
			if (nf != null) {
				if (retVal.nullFlavor == null)
					retVal.nullFlavor = new ArrayList<String>();
				retVal.nullFlavor.add(nf.getCodeValue());
			} else {

				value = baseType.getCode();
				if (value != null) {
					retVal.setCode(value);
				}

				value = baseType.getCodeSystem();
				if (value != null) {
					retVal.setCodeSystem(value);
				}

				value = baseType.getCodeSystemName();
				if (value != null) {
					retVal.setCodeSystemName(value);
				}

				value = baseType.getCodeSystemVersion();
				if (value != null) {
					retVal.setCodeSystemVersion(value);
				}

				value = baseType.getDisplayName();
				if (value != null) {
					retVal.setDisplayName(value);
				}

				value = baseType.getOriginalText();
				if (value != null) {
					ED ed = new ED();
					ed.xmlContent = value;
					retVal.setOriginalText(ed);
				}
			}

			for (CodeBaseType item : baseType.getCodeTranslationList()) {
				retVal.getTranslation().add(createHl7CdaR2Cd(item));
			}

			// listDisplayNameTranslations() is not implemented, because it is
			// not
			// supported by CD
		}

		return retVal;

	}

	/**
	 * <div class="en">Creates the HL7 CDA R2 data type from the given base
	 * type.<div>
	 *
	 * <div class="de">Erstellt den HL7 CDA R2 Datentyp aus dem angegebenen
	 * Basistyp.<div>
	 *
	 * @param baseType
	 *            the base type
	 * @return the HL7 CDA R2 data typed value
	 */
	public static org.ehealth_connector.common.hl7cdar2.CS createHl7CdaR2Cs(CodeBaseType baseType) {
		org.ehealth_connector.common.hl7cdar2.CS retVal = null;
		if (baseType != null) {
			retVal = new org.ehealth_connector.common.hl7cdar2.CS();
			String value;

			NullFlavor nf = baseType.getNullFlavor();
			if (nf != null) {
				if (retVal.nullFlavor == null)
					retVal.nullFlavor = new ArrayList<String>();
				retVal.nullFlavor.add(nf.getCodeValue());
			} else {

				value = baseType.getCode();
				if (value != null) {
					retVal.setCode(value);
				}

				value = baseType.getCodeSystem();
				if (value != null) {
					retVal.setCodeSystem(value);
				}

				value = baseType.getCodeSystemName();
				if (value != null) {
					retVal.setCodeSystemName(value);
				}

				value = baseType.getCodeSystemVersion();
				if (value != null) {
					retVal.setCodeSystemVersion(value);
				}

				value = baseType.getDisplayName();
				if (value != null) {
					retVal.setDisplayName(value);
				}

				value = baseType.getOriginalText();
				if (value != null) {
					ED ed = new ED();
					ed.xmlContent = value;
					retVal.setOriginalText(ed);
				}
			}

			for (CodeBaseType item : baseType.getCodeTranslationList()) {
				retVal.getTranslation().add(createHl7CdaR2Cd(item));
			}

			// listDisplayNameTranslations() is not implemented, because it is
			// not
			// supported by CD
		}

		return retVal;

	}

	/**
	 * Instantiates a new code. Default constructor.
	 */
	public Code() {
	}

	/**
	 * <div class="en">Instantiates a new instance from the given base
	 * type.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen
	 * Basistyp.<div>
	 *
	 * @param baseType
	 *            the base type
	 */
	public Code(CodeBaseType baseType) {
		initFromBaseType(baseType);
	}

	/**
	 * <div class="en">Instantiates a new instance from the given information.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen Daten.<div>
	 *
	 * @param code       the code
	 * @param codeSystem the code System
	 * @param display    the display name
	 */
	public Code(String code, String codeSystem, String display) {
		setCode(code);
		setCodeSystem(codeSystem);
		setDisplayName(display);
	}

	/**
	 * <div class="en">Instantiates a new instance from the given null flavor.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen Null
	 * Flavor.<div>
	 *
	 * @param nf the null flavor
	 */
	public Code(NullFlavor nf) {
		setNullFlavor(nf);
	}

	/**
	 * <div class="en">Instantiates a new instance from the given HL7 CDA R2
	 * data type.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen HL7 CDA R2
	 * Datentyp.<div>
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 data type
	 */
	public Code(org.ehealth_connector.common.hl7cdar2.CD hl7CdaR2Value) {
		initFromHl7CdaR2(hl7CdaR2Value);
	}

	/**
	 * <div class="en">Instantiates a new instance from the given HL7 CDA R2
	 * data type.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen HL7 CDA R2
	 * Datentyp.<div>
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 data type
	 */
	public Code(org.ehealth_connector.common.hl7cdar2.CE hl7CdaR2Value) {
		initFromHl7CdaR2(hl7CdaR2Value);
	}

	/**
	 * <div class="en">Instantiates a new instance from the given HL7 CDA R2
	 * data type.<div>
	 *
	 * <div class="de">Instanziiert eine neue Instanz vom angegebenen HL7 CDA R2
	 * Datentyp.<div>
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 data type
	 */
	public Code(org.ehealth_connector.common.hl7cdar2.CS hl7CdaR2Value) {
		initFromHl7CdaR2(hl7CdaR2Value);
	}

	/**
	 * Instantiates a new code based on an enum value.
	 *
	 * @param enumValue
	 *            the enum value
	 */
	public Code(ValueSetEnumInterface enumValue) {
		CodeBaseType codeBt = CodeBaseType.builder().withCode(enumValue.getCodeValue())
				.withCodeSystem(enumValue.getCodeSystemId()).build();
		if (enumValue.getCodeSystemName() != null)
			if (!"".equals(enumValue.getCodeSystemName()))
				codeBt.setCodeSystemName(enumValue.getCodeSystemName());

		if (enumValue.getDisplayName() != null)
			if (!"".equals(enumValue.getDisplayName()))
				codeBt.setDisplayName(enumValue.getDisplayName());

		initFromBaseType(codeBt);

	}

	/**
	 * <div class="en">Gets the HL7 CDA R2 data type from the current
	 * instance.<div>
	 *
	 * <div class="de">Ruft den HL7 CDA R2 Datentyp aus der aktuellen Instanz
	 * ab.<div>
	 *
	 * @return the HL7 CDA R2 data type
	 */
	public org.ehealth_connector.common.hl7cdar2.CD getHl7CdaR2Cd() {
		return createHl7CdaR2Cd(this);
	}

	/**
	 * <div class="en">Gets the HL7 CDA R2 data type from the current
	 * instance.<div>
	 *
	 * <div class="de">Ruft den HL7 CDA R2 Datentyp aus der aktuellen Instanz
	 * ab.<div>
	 *
	 * @return the HL7 CDA R2 data type
	 */
	public org.ehealth_connector.common.hl7cdar2.CE getHl7CdaR2Ce() {
		return createHl7CdaR2Ce(this);
	}

	/**
	 * <div class="en">Gets the HL7 CDA R2 data type from the current
	 * instance.<div>
	 *
	 * <div class="de">Ruft den HL7 CDA R2 Datentyp aus der aktuellen Instanz
	 * ab.<div>
	 *
	 * @return the HL7 CDA R2 data type
	 */
	public org.ehealth_connector.common.hl7cdar2.CS getHl7CdaR2Cs() {
		return createHl7CdaR2Cs(this);
	}

	/**
	 * Inits from the base type.
	 *
	 * @param baseType
	 *            the base type
	 */
	private void initFromBaseType(CodeBaseType baseType) {
		if (baseType != null) {
			setCode(baseType.getCode());
			setCodeSystem(baseType.getCodeSystem());
			setCodeSystemName(baseType.getCodeSystemName());
			setCodeSystemVersion(baseType.getCodeSystemVersion());
			setCodeTranslationList(baseType.getCodeTranslationList());
			setDisplayName(baseType.getDisplayName());
			setDisplayNameTranslationList(baseType.getDisplayNameTranslationList());
			setOriginalText(baseType.getOriginalText());
			setNullFlavor(baseType.getNullFlavor());
		} else
			setNullFlavor(NullFlavor.NOT_AVAILABLE);

	}

	/**
	 * Inits from the HL7 CDA R2 data type.
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 data type value
	 */
	private void initFromHl7CdaR2(org.ehealth_connector.common.hl7cdar2.CD hl7CdaR2Value) {
		initFromBaseType(createCodeBaseType(hl7CdaR2Value));
	}

	/**
	 * <div class="en">Sets the fields of the current instance by the given base
	 * type.<div>
	 *
	 * <div class="de">Legt die Felder der aktuellen Instanz durch den
	 * angegebenen Basistyp fest.<div>
	 *
	 * @param baseType
	 *            the base type
	 */
	public void set(CodeBaseType baseType) {
		initFromBaseType(baseType);
	}

	/**
	 * <div class="en">Sets the fields of the current instance by the given HL7
	 * CDA R2 data type.<div>
	 *
	 * <div class="de">Legt die Felder der aktuellen Instanz durch den
	 * angegebenen HL7 CDA R2 Datentyp fest.<div>
	 *
	 * @param hl7CdaR2Value
	 *            the HL7 CDA R2 data typed value
	 */
	public void set(org.ehealth_connector.common.hl7cdar2.CD hl7CdaR2Value) {
		initFromHl7CdaR2(hl7CdaR2Value);
	}

}
