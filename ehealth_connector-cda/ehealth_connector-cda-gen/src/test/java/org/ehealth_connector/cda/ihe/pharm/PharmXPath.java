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
 * Year of publication: 2015
 *
 *******************************************************************************/

package org.ehealth_connector.cda.ihe.pharm;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

/**
 * The Class PharmXPath.
 */
public class PharmXPath {

	static private XPathFactory xpathFactory = XPathFactory.newInstance();

	/**
	 * Gets the XPath with namespaces prefix set up for cda and pharm
	 *
	 * @return the xpath
	 */
	static public XPath getXPath() {

		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				if ("cda".equals(prefix)) {
					return "urn:hl7-org:v3";
				} else if ("sdtc".equals(prefix)) {
					return "urn:hl7-org:sdtc";
				} else if ("xsi".equals(prefix)) {
					return "http://www.w3.org/2001/XMLSchema-instance";
				} else if ("pharm".equals(prefix)) {
					return "urn:ihe:pharm:medication";
				}
				return null;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				if ("urn:hl7-org:v3".equals(namespaceURI)) {
					return "cda";
				} else if ("urn:hl7-org:sdtc".equals(namespaceURI)) {
					return "sdtc";
				} else if ("http://www.w3.org/2001/XMLSchema-instance".equals(namespaceURI)) {
					return "xsi";
				} else if ("urn:1ihe:pharm:medication".equals(namespaceURI)) {
					return "pharm";
				}
				return null;
			}

			@Override
			public Iterator<String> getPrefixes(String namespaceURI) {
				return null;
			}
		});
		return xpath;
	}

}
