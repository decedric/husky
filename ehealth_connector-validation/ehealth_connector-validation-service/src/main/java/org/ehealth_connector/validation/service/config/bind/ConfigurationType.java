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

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.ehealth_connector.common.utils.FileUtil;
import org.ehealth_connector.common.utils.Util;
import org.ehealth_connector.validation.service.config.ConfigurationException;

/**
 * <p>
 * Java class for ConfigurationType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ConfigurationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="application" type="{}ApplicationType"/>
 *         &lt;element name="schematron" type="{}SchematronType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="baseDir" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigurationType", propOrder = { "application", "schematron" })
public class ConfigurationType {

	@XmlElement(required = true)
	protected ApplicationType application;

	@XmlElement(required = true)
	protected SchematronType schematron;

	@XmlAttribute
	@XmlSchemaType(name = "anySimpleType")
	protected String baseDir;

	@XmlAttribute
	@XmlSchemaType(name = "anySimpleType")
	protected String workDir;

	/**
	 * Gets the value of the application property.
	 *
	 * @return possible object is {@link ApplicationType }
	 *
	 */
	public ApplicationType getApplication() {
		return application;
	}

	/**
	 * Gets the value of the baseDir property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public File getBaseDir() {
		File retVal = null;
		if (baseDir == null) {
			String catalinaBase = System.getProperty("catalina.base");
			if (catalinaBase != null)
				baseDir = new File(catalinaBase).getAbsolutePath();
		}
		if (baseDir != null)
			retVal = new File(baseDir).getAbsoluteFile();
		return retVal;
	}

	/**
	 * Gets the value of the ConfigurationDir property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public File getConfigurationDir() {
		String configDir = System.getProperty("configuration.dir");
		return new File(configDir).getAbsoluteFile();
	}

	/**
	 * Gets the value of the schematron property.
	 *
	 * @return possible object is {@link SchematronType }
	 *
	 */
	public SchematronType getSchematron() {
		return schematron;
	}

	/**
	 * Gets the value of the userDir property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public File getUserDir() {
		return new File(System.getProperty("user.dir")).getAbsoluteFile();
	}

	/**
	 * Returns the application's work directory. This is the directory, where
	 * the transformed (pre-compiled) rule-sets (the <cite>Schematron
	 * Validator</cite> files) and the archived rule-sets (the .zip file) are
	 * placed.
	 *
	 * @return the application's work directory.
	 */
	public File getWorkDir() throws ConfigurationException {
		File wD = null;
		if (workDir == null) {
			File baseDir = getBaseDir();
			if (baseDir == null)
				baseDir = new File(Util.getTempDirectory());
			wD = new File(FileUtil.combinePath(baseDir.getAbsolutePath(), "work"));
			if (!wD.isDirectory()) {
				if (!wD.mkdir()) {
					throw new ConfigurationException(
							String.format("Could not create working directory: '%s'", wD));
				}
			}
			if (!wD.canWrite()) {
				throw new ConfigurationException(
						String.format("Working directory is not writable: '%s'", wD));
			}
		} else
			wD = new File(workDir);

		return wD.getAbsoluteFile();
	}

	/**
	 * Sets the value of the application property.
	 *
	 * @param value
	 *            allowed object is {@link ApplicationType }
	 *
	 */
	public void setApplication(ApplicationType value) {
		this.application = value;
	}

	/**
	 * Sets the value of the baseDir property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBaseDir(String value) {
		this.baseDir = value;
	}

	/**
	 * Sets the value of the schematron property.
	 *
	 * @param value
	 *            allowed object is {@link SchematronType }
	 *
	 */
	public void setSchematron(SchematronType value) {
		this.schematron = value;
	}

	/**
	 * Sets the application's work directory. This is the directory, where the
	 * transformed (pre-compiled) rule-sets (the <cite>Schematron
	 * Validator</cite> files) and the archived rule-sets (the .zip file) are
	 * placed.
	 *
	 * @param workDir
	 *            the application's work directory
	 */
	public void setWorkDir(File workDir) {
		this.workDir = workDir.getAbsolutePath();
	}

}
