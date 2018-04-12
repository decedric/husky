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
package org.ehealth_connector.security.saml2.impl;

import java.util.Calendar;

import org.ehealth_connector.security.core.SecurityObjectBuilder;
import org.ehealth_connector.security.saml2.SubjectConfirmation;
import org.ehealth_connector.security.saml2.SubjectConfirmationBuilder;
import org.joda.time.DateTime;

/**
 * <!-- @formatter:off -->
 * <div class="en">HEREISENGLISH</div>
 * <div class="de">HIERISTDEUTSCH</div>
 * <div class="fr">VOICIFRANCAIS</div>	
 * <div class="it">ITALIANO</div>
 * <!-- @formatter:on -->
 *
 */
public class SubjectConfirmationBuilderImpl implements SubjectConfirmationBuilder,
		SecurityObjectBuilder<org.opensaml.saml.saml2.core.SubjectConfirmation, SubjectConfirmation> {

	private org.opensaml.saml.saml2.core.SubjectConfirmation subjectConfirmation;
	private org.opensaml.saml.saml2.core.SubjectConfirmationData subjectConfirmationData;

	public SubjectConfirmationBuilderImpl() {
		subjectConfirmation = new org.opensaml.saml.saml2.core.impl.SubjectConfirmationBuilder().buildObject();
		subjectConfirmationData = new org.opensaml.saml.saml2.core.impl.SubjectConfirmationDataBuilder().buildObject();
		subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.ehealth_connector.security.saml2.SubjectConfirmationBuilder#method(java.lang.String)
	 */
	@Override
	public SubjectConfirmationBuilder method(String aMethod) {
		if (aMethod != null) {
			subjectConfirmation.setMethod(aMethod);
		}
		return this;
	}

	@Override
	public SubjectConfirmationBuilder inResponseTo(String aResponseTo) {
		if (aResponseTo != null) {
			subjectConfirmationData.setInResponseTo(aResponseTo);
			;
		}
		return this;
	}

	@Override
	public SubjectConfirmationBuilder notOnOrAfter(Calendar aNotOnOrAfter) {
		if (aNotOnOrAfter != null) {
			final DateTime dateTime = new DateTime(aNotOnOrAfter.getTimeInMillis());
			subjectConfirmationData.setNotOnOrAfter(dateTime);
		}
		return this;
	}

	@Override
	public SubjectConfirmationBuilder notBefore(Calendar aNotBefore) {
		if (aNotBefore != null) {
			final DateTime dateTime = new DateTime(aNotBefore.getTimeInMillis());
			subjectConfirmationData.setNotBefore(dateTime);
		}
		return this;
	}

	@Override
	public SubjectConfirmationBuilder address(String aAddress) {
		if (aAddress != null) {
			subjectConfirmationData.setAddress(aAddress);
		}
		return this;
	}

	@Override
	public SubjectConfirmationBuilder recipient(String aRecipient) {
		if (aRecipient != null) {
			subjectConfirmationData.setRecipient(aRecipient);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.ehealth_connector.security.saml2.SubjectConfirmationBuilder#create()
	 */
	@Override
	public SubjectConfirmation create() {
		return new SubjectConfirmationImpl(subjectConfirmation);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.ehealth_connector.security.core.SecurityObjectBuilder#create(java.lang.Object)
	 */
	@Override
	public SubjectConfirmation create(org.opensaml.saml.saml2.core.SubjectConfirmation aInternalObject) {
		return new SubjectConfirmationImpl(aInternalObject);
	}

}