//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.04.08 at 10:09:10 AM CEST
//

package org.ehealth_connector.validation.service.schematron.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="prefix" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="uri" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ns-prefix-in-attribute-values")
public class NsPrefixInAttributeValues {

	@XmlAttribute(name = "prefix", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NMTOKEN")
	protected String prefix;
	@XmlAttribute(name = "uri", required = true)
	@XmlSchemaType(name = "anySimpleType")
	protected String uri;

	/**
	 * Gets the value of the prefix property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Gets the value of the uri property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the value of the prefix property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setPrefix(String value) {
		this.prefix = value;
	}

	/**
	 * Sets the value of the uri property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setUri(String value) {
		this.uri = value;
	}

}
