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
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2020.07.09 um 01:07:39 PM CEST
//

package org.husky.common.hl7cdar2;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * A character string that may have a type-tag signifying its role in the
 * address. Typical parts that exist in about every address are street, house
 * number, or post box, postal code, city, country but other roles may be
 * defined regionally, nationally, or on an enterprise level (e.g. in military
 * addresses). Addresses are usually broken up into lines, which are indicated
 * by special line-breaking delimiter elements (e.g., DEL).
 *
 *
 * <p>
 * Java-Klasse für ADXP complex type.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ADXP">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ST">
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}AddressPartType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADXP")
@XmlSeeAlso({ AdxpPostalCode.class, AdxpStreetName.class, AdxpCountry.class,
        AdxpDeliveryInstallationArea.class, AdxpDirection.class, AdxpDeliveryMode.class,
        AdxpUnitID.class, AdxpBuildingNumberSuffix.class, AdxpStreetAddressLine.class,
        AdxpDeliveryInstallationType.class, AdxpUnitType.class, AdxpCareOf.class,
        AdxpHouseNumber.class, AdxpCounty.class, AdxpCity.class, AdxpCensusTract.class,
        AdxpState.class, AdxpDeliveryAddressLine.class, AdxpPrecinct.class,
        AdxpHouseNumberNumeric.class, AdxpStreetNameType.class, AdxpStreetNameBase.class,
        AdxpPostBox.class, AdxpDeliveryModeIdentifier.class, AdxpAdditionalLocator.class,
        AdxpDeliveryInstallationQualifier.class, AdxpDelimiter.class })
public class ADXP extends ST {

    @XmlAttribute(name = "partType")
    protected List<String> partType;

    /**
     * Gets the value of the partType property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the partType property.
     *
     * <p>
     * For example, to add a new item, do as follows: <pre>
     *    getPartType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getPartType() {
        if (partType == null) {
            partType = new ArrayList<String>();
        }
        return this.partType;
    }

}
