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
 * Year of publication: 2016
 *
 *******************************************************************************/

package org.ehealth_connector.cda.ihe.pharm.enums;

import org.ehealth_connector.common.Code;

/**
 * The Enum PharmaceuticalAdviceStatusList see IHE PHARM PADV 6.5.1 IHE
 * Pharmaceutical Advice Status List
 */
public enum PharmaceuticalAdviceStatusList {

	CANCEL("CANCEL"), CHANGE("CHANGE"), OK("OK"), REFUSE("REFUSE"), SUSPEND("SUSPEND");

	/** The Constant CODE_SYSTEM_NAME. */
	public static final String CODE_SYSTEM_NAME = "IHE Pharmaceutical Advice Status List";

	/** The Constant CODE_SYSTEM_OID. */
	public static final String CODE_SYSTEM_OID = "1.3.6.1.4.1.19376.1.9.2.1";

	/**
	 * Gets the enum.
	 *
	 * @param code
	 *            the code
	 * @return the enum
	 */
	public static PharmaceuticalAdviceStatusList getEnum(String code) {
		for (final PharmaceuticalAdviceStatusList x : values()) {
			if (x.code.equals(code)) {
				return x;
			}
		}
		return null;
	}

	/** The code. */
	private String code;

	/**
	 * Instantiates a new pharmaceutical advice status list.
	 *
	 * @param code
	 *            the code
	 */
	private PharmaceuticalAdviceStatusList(String code) {
		this.code = code;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Code getCode() {
		final Code ehcCode = new Code(CODE_SYSTEM_OID, code, null);
		return ehcCode;
	}

}
