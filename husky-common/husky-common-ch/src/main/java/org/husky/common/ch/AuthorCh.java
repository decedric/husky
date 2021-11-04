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
package org.husky.common.ch;

import org.husky.common.ch.enums.AuthorRole;
import org.husky.common.ch.enums.AuthorSpeciality;
import org.husky.common.hl7cdar2.POCDMT000040Author;
import org.husky.common.model.Author;
import org.husky.common.model.Name;

/**
 * The Swis derivation of an Author (adds features for GLN).
 */
public class AuthorCh extends org.husky.common.model.Author {

	/**
	 * Instantiates a new instance.
	 */
	public AuthorCh() {
		super();
	}

	/**
	 * Default constructor to instantiate the object.
	 *
	 * @param aAuthor
	 *            the generic author
	 */
	public AuthorCh(Author aAuthor) {
		super(aAuthor.getAuthorMdht());
	}

	/**
	 * Instantiates a new instance.
	 *
	 * @param name
	 *            the name
	 */
	public AuthorCh(Name name) {
		super(name);
	}

	/**
	 * Instantiates a new instance.
	 *
	 * @param name
	 *            Name
	 * @param gln
	 *            Global Location Number (GLN)
	 */
	public AuthorCh(Name name, String gln) {
		super(name, gln);
	}

	/**
	 * Instantiates a new instance.
	 *
	 * @param authorMdht
	 *            the MDHT Author Object
	 */
	public AuthorCh(POCDMT000040Author authorMdht) {
		super(authorMdht);
	}

	/**
	 * Gets the author function code. This element specifies in which role or
	 * function the author is related to this document (e.g. Code "40001" for
	 * "Apotheker"). The code can also be used as author role, when a document
	 * is beeing sent (via XDS, XDM, XDR).
	 *
	 * @return code the function code
	 */
	public AuthorRole getRoleFunctionEnum() {
		return AuthorRole.getEnum(getAuthorMdht().getFunctionCode().getCode());
	}

	/**
	 * Sets the author speciality code. This element specifies the speciality
	 * ("Fachrichtung", e.g. "Fachärztin/Facharzt für Allgemeine Innere Medizin"
	 * ) of the author. The code can also be used as author speciality, when a
	 * document is beeing sent (via XDS, XDM, XDR).
	 *
	 * @return code the speciality code
	 */
	public AuthorSpeciality getSpecialityEnum() {
		return AuthorSpeciality.getEnum(getAsAuthor().getCode().getCode());
	}

	/**
	 * Sets the author function code. This element specifies in which role or
	 * function the author is related to this document (e.g. Code "40001" for
	 * "Apotheker"). The code can also be used as author role, when a document
	 * is beeing sent (via XDS, XDM, XDR).
	 *
	 * @param code
	 *            the function code
	 */
	public void setRoleFunction(AuthorRole code) {
		getAuthorMdht().setFunctionCode(code.getCode().getHl7CdaR2Ce());
	}

	/**
	 * Sets the author speciality code. This element specifies the speciality
	 * ("Fachrichtung", e.g. Code "50001" for "Fachärztin/Facharzt für
	 * Allgemeine Innere Medizin") of the author. The code can also be used as
	 * author speciality, when a document is beeing sent (via XDS, XDM, XDR).
	 *
	 * @param code
	 *            the speciality code
	 */
	public void setSpeciality(AuthorSpeciality code) {
		getAsAuthor().setCode(code.getCode().getHl7CdaR2Ce());
	}
}