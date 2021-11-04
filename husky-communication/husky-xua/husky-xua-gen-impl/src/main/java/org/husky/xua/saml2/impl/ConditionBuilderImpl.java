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
package org.husky.xua.saml2.impl;

import org.husky.xua.core.SecurityObjectBuilder;
import org.husky.xua.saml2.Condition;
import org.husky.xua.saml2.ConditionBuilder;

/**
 * <!-- @formatter:off -->
 * <div class="en">Class implementing the corresponding interface for Condition building.</div>
 * <div class="de">Die Klasse implementiert das entsprechende Interface um Conditions bilden zu k&ooml;nnen.</div>
 * <div class="fr"></div>
 * <div class="it"></div>
 * <!-- @formatter:on -->
 */
public class ConditionBuilderImpl implements ConditionBuilder,
		SecurityObjectBuilder<org.opensaml.saml.saml2.core.Condition, Condition> {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.core.SecurityObjectBuilder#create(java.lang.Object)
	 */
	@Override
	public Condition create(org.opensaml.saml.saml2.core.Condition aInternalObject) {
		return new ConditionImpl(aInternalObject);
	}

}