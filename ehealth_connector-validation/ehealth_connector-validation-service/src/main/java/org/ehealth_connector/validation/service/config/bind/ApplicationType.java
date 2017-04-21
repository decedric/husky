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
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.04.08 at 08:04:19 AM CEST
//
package org.ehealth_connector.validation.service.config.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ApplicationType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ApplicationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="downloads-url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jquery-theme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="document-schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationType", propOrder = { "downloadsUrl", "theme", "documentSchema",
		"pdfLevel", "pdfReportingLevel", "licenseKey" })
public class ApplicationType {

	@XmlElement(name = "downloads-url")
	protected String downloadsUrl;
	@XmlElement(name = "jquery-theme")
	protected String theme;
	@XmlElement(name = "document-schema")
	protected String documentSchema;
	@XmlElement(name = "pdf-level")
	protected String pdfLevel;
	@XmlElement(name = "pdf-reporting-level")
	protected String pdfReportingLevel;
	@XmlElement(name = "license-key")
	protected String licenseKey;

	/**
	 * Gets the value of documentSchema.
	 *
	 * @return the value of documentSchema
	 *
	 */
	public String getDocumentSchema() {
		return documentSchema;
	}

	/**
	 * Gets the downloadsUrl.
	 *
	 * @return the downloadsUrl
	 *
	 */
	public String getDownloadsUrl() {
		return downloadsUrl;
	}

	/**
	 * Gets the LicenseKey for the external PDF-Tools PdfValidator engine.
	 *
	 * @return the LicenseKey for the external PDF-Tools PdfValidator engine
	 */
	public String getLicenseKey() {
		return licenseKey;
	}

	/**
	 * Gets the PdfLevel to be validated by the external PDF-Tools PdfValidator
	 * engine.
	 *
	 * @return the PdfLevel to be validated by the external PDF-Tools
	 *         PdfValidator engine.
	 */
	public String getPdfLevel() {
		return pdfLevel;
	}

	/**
	 * Gets the level of messages to be reported by the external PDF-Tools
	 * PdfValidator engine.
	 *
	 * @return the level of messages to be reported by the external PDF-Tools
	 *         PdfValidator engine.
	 */
	public String getPdfReportingLevel() {
		return pdfReportingLevel;
	}

	/**
	 * Gets the theme (JQuery Theme). This is only used by Online CDA
	 * Validators.
	 *
	 * @return the value of the theme (JQuery Theme)
	 *
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Sets the value of documentSchema
	 *
	 * @param value
	 *            the value of documentSchema
	 */
	public void setDocumentSchema(String value) {
		this.documentSchema = value;
	}

	/**
	 * Sets the downloadsUrl
	 *
	 * @param value
	 *            the downloadsUrl
	 */
	public void setDownloadsUrl(String value) {
		this.downloadsUrl = value;
	}

	/**
	 * Sets the LicenseKey for the external PDF-Tools PdfValidator engine.
	 *
	 * @param licenseKey
	 *            the LicenseKey for the external PDF-Tools PdfValidator engine.
	 */
	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	/**
	 * Sets the PdfLevel to be validated by the external PDF-Tools PdfValidator
	 * engine.
	 *
	 * @param pdfLevel
	 *            the PdfLevel to be validated by the external PDF-Tools
	 *            PdfValidator engine.
	 */
	public void setPdfLevel(String pdfLevel) {
		this.pdfLevel = pdfLevel;
	}

	/**
	 * Sets the level of messages to be reported by the external PDF-Tools
	 * PdfValidator engine.
	 *
	 * @param pdfReportingLevel
	 *            the level of messages to be reported by the external PDF-Tools
	 *            PdfValidator engine.
	 */
	public void setPdfReportingLevel(String pdfReportingLevel) {
		this.pdfReportingLevel = pdfReportingLevel;
	}

	/**
	 * Sets the theme (JQuery Theme). This is only used by Online CDA
	 * Validators.
	 *
	 * @param value
	 *            the theme (JQuery Theme). This is only used by Online CDA
	 *            Validators.
	 */
	public void setTheme(String value) {
		this.theme = value;
	}
}
