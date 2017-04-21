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
package org.ehealth_connector.cda;

import org.ehealth_connector.common.utils.Util;
import org.openhealthtools.mdht.uml.cda.Observation;

/**
 * MdhtEntryObservationFacade is a facade for extending the mdht objects
 * generated by the model The design enables that all derived convenience
 * objects can use the underlying mdht model but the exposing api of the classes
 * is independent of the mdht implementation.
 *
 * @param <E>
 *            the model type to provide for implemting the facade to it,
 *            extending an observation (Act)
 */
public class MdhtEntryObservationFacade<E extends Observation> extends MdhtFacade<E> {

	/**
	 * Instantiates a new facade for the provided mdht object.
	 *
	 * @param mdht
	 *            the mdht model object
	 */
	protected MdhtEntryObservationFacade(E mdht) {
		super(mdht, null, null);
	}

	/**
	 * Gets the text reference.
	 *
	 * @return the text reference
	 */
	@Override
	public String getTextReference() {
		if ((this.getMdht().getText() != null)
				&& (this.getMdht().getText().getReference() != null)) {
			return this.getMdht().getText().getReference().getValue();
		}
		return null;
	}

	/**
	 * Sets the text reference.
	 *
	 * @param value
	 *            the new text reference
	 */
	@Override
	public void setTextReference(String value) {
		this.getMdht().setText(Util.createReference(value));
	}

}
