/*
 *
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
package org.ehealth_connector.security.helpers;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.core.xml.XMLObject;

/**
 * <!-- @formatter:off -->
 * <div class="en">HEREISENGLISH</div>
 * <div class="de">HIERISTDEUTSCH</div>
 * <div class="fr">VOICIFRANCAIS</div>
 * <div class="it">ITALIANO</div>
 * 
 * <!-- @formatter:on -->
 */
public class ListXMLObjectHelper<T extends XMLObject> {
	public T getComponent(Class<? extends XMLObject> aType, List<XMLObject> aList) {
		for (final XMLObject item : aList) {
			if (item.getClass().equals(aType)) {
				return (T) item;
			}
		}
		return null;
	}

	public List<T> getComponentList(Class<? extends XMLObject> aType, List<XMLObject> aList) {
		final List<T> retVal = new ArrayList<>();
		for (final XMLObject item : aList) {
			if (item.getClass().equals(aType)) {
				retVal.add((T) item);
			}
		}
		return retVal;
	}
}