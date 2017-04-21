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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SchematronType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="SchematronType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="rule-set" type="{}RuleSetType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dir" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SchematronType", propOrder = { "ruleSets" })
public class SchematronType {

	@XmlElement(name = "rule-set", required = true)
	protected List<RuleSetType> ruleSets;
	@XmlAttribute(name = "dir", required = true)
	protected String directory;

	/**
	 * Gets the value of the directory property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * Gets the value of the ruleSets property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the ruleSets property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows: <pre>
	 *    getRuleSets().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link RuleSetType }
	 *
	 *
	 */
	public List<RuleSetType> getRuleSets() {
		if (ruleSets == null) {
			ruleSets = new ArrayList<RuleSetType>();
		}
		return this.ruleSets;
	}

	/**
	 * Sets the value of the directory property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setDirectory(String value) {
		this.directory = value;
	}

}
