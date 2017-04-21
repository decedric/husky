/*
 * The authorship of this project and accompanying materials is held by medshare GmbH, Switzerland.
 * All rights reserved. https://medshare.net
 *
 * Source code, documentation and other resources have been contributed by various people.
 * Project Team: https://sourceforge.net/p/ehealthconnector/wiki/Team/
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

/**
 * Enum that contains the different Versions of the eHealtConnector
 *
 */
public enum EHealthConnectorVersions {
	/**
	 * OID: 2.16.756.5.30.1.139.1.1.1 eHealthConnector Proof of Concept
	 */
	EHealthConnectorR201402("2.16.756.5.30.1.139.1.1.1", "eHealthConnector Proof of Concept", "20140211"),

	/**
	 * OID: 2.16.756.5.30.1.139.1.1.2 eHealthConnector R201503
	 */
	EHealthConnectorR201503("2.16.756.5.30.1.139.1.1.2", "eHealthConnector R201503", "20150401"),

	/**
	 * OID: 2.16.756.5.30.1.139.1.1.3 eHealthConnector Rxxxxxx
	 */
	EHealthConnectorDev("2.16.756.5.30.1.139.1.1.3", "eHealthConnector trunk", "xxxxxxxx"),

	/**
	 * OID: 2.16.756.5.30.1.139.1.1.4 eHealthConnector R201510
	 */
	EHealthConnectorR201510("2.16.756.5.30.1.139.1.1.4", "eHealthConnector R201510", "R20151031"),

	/**
	 * OID: 2.16.756.5.30.1.139.1.1.5 eHealthConnector R201604
	 */
	EHealthConnectorR201604("2.16.756.5.30.1.139.1.1.5", "eHealthConnector R201604", "R20160430"),

	/**
	 * OID: 2.16.756.5.30.1.139.1.1.6 eHealthConnector R201611
	 */
	EHealthConnectorR201611("2.16.756.5.30.1.139.1.1.6", "eHealthConnector R201611", "R201611xx");

	public static EHealthConnectorVersions getCurrentVersion() {
		return EHealthConnectorDev;
	}

	private String oid;
	private String systemVersionName;

	private String releaseDate;

	private EHealthConnectorVersions(String oid, String systemVersionName, String releaseDate) {
		this.oid = oid;
		this.systemVersionName = systemVersionName;
		this.releaseDate = releaseDate;
	}

	public String getOid() {
		return oid;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getSystemVersionName() {
		return systemVersionName;
	}

}
