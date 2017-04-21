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

import java.util.ArrayList;
import java.util.List;

import org.ehealth_connector.common.Name;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;

/**
 * MdhtEntryObservationFacade is a facade for extending the mdht objects
 * generated by the model The design enables that all derived convenience
 * objects can use the underlying mdht model but the exposing api of the classes
 * is independent of the mdht implementation.
 *
 * @param <E>
 *            the model type to provide for implementing the facade to it,
 *            extending an Participant
 */
public class MdhtPersonFacade<E extends org.openhealthtools.mdht.uml.cda.Person>
		extends MdhtFacade<E> {

	/**
	 * Instantiates a new facade for the provided mdht object.
	 *
	 * @param mdht
	 *            the mdht model object
	 */
	protected MdhtPersonFacade(E mdht) {
		super(mdht, null, null);
	}

	/**
	 * <div class="en">Gets the complete name.</div> <div class="de">Liefert den
	 * ganzen Namen (z.B. "Dr. Allzeit Bereit der Dritte")</div>
	 * <div class="fr"></div> <div class="it"></div>
	 *
	 * @return <div class="en">the complete name</div>
	 */
	public String getCompleteName() {
		if (!getMdht().getNames().isEmpty()) {
			final Name name = new Name(getMdht().getNames().get(0));
			return name.getCompleteName();
		}
		return null;
	}

	/**
	 * <div class="en">Gets the names.</div> <div class="de">Liefert alle
	 * Nachnamen</div> <div class="fr"></div> <div class="it"></div>
	 *
	 * @return <div class="en">the names</div>
	 */
	public List<Name> getNames() {
		final List<Name> nl = new ArrayList<Name>();
		for (final PN mName : getMdht().getNames()) {
			final Name name = new Name(mName);
			nl.add(name);
		}
		return nl;
	}
}
