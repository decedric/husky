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
 * Year of publication: 2016
 *
 *******************************************************************************/
package org.ehealth_connector.cda.ch;

import java.util.Date;

import org.ehealth_connector.cda.AbstractAllergyProblem;
import org.ehealth_connector.cda.ch.utils.CdaChUtil;
import org.ehealth_connector.cda.enums.AllergiesAndIntolerances;
import org.ehealth_connector.common.Code;
import org.ehealth_connector.common.Identificator;
import org.openhealthtools.mdht.uml.cda.ihe.AllergyIntolerance;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;

/**
 * <div class="en">A class representing the allergy problem information.</div>
 * <div class="de">Eine Klasse die die Informationen zu Allergieprobleme
 * representiert.</div>
 *
 */
public class AllergyProblem extends AbstractAllergyProblem {

	/**
	 * Default constructor to instanciate the object.
	 */
	public AllergyProblem() {

	}

	/**
	 * Instantiates a new allergy problem.
	 *
	 * @param allergy
	 *            the allergy
	 */
	public AllergyProblem(AllergiesAndIntolerances allergy) {
		super(allergy);
	}

	/**
	 * Instantiates a new allergy problem.
	 *
	 * @param kindOfAllergy
	 *            the kind of allergy
	 * @param problem
	 *            the problem
	 * @param startOfProblem
	 *            the start of problem
	 * @param endOfProblem
	 *            the end of problem
	 */
	public AllergyProblem(AllergiesAndIntolerances kindOfAllergy, Code problem, Date startOfProblem,
			Date endOfProblem) {
		super(kindOfAllergy, problem, startOfProblem, endOfProblem);
	}

	/**
	 * Instantiates a new allergy problem.
	 *
	 * @param kindOfAllergy
	 *            the kind of allergy
	 * @param problem
	 *            the problem
	 * @param startOfProblem
	 *            the start of problem
	 * @param endOfProblem
	 *            the end of problem
	 * @param internalProblemId
	 *            the internal problem id
	 */
	public AllergyProblem(AllergiesAndIntolerances kindOfAllergy, Code problem, Date startOfProblem,
			Date endOfProblem, Identificator internalProblemId) {
		super(kindOfAllergy, problem, startOfProblem, endOfProblem, internalProblemId);
	}

	/**
	 * Instantiates a new allergy problem.
	 *
	 * @param allergyIntolerance
	 *            the allergy intolerance
	 */
	public AllergyProblem(AllergyIntolerance allergyIntolerance) {
		super(allergyIntolerance);
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.ehealth_connector.cda.AbstractAllergyProblem#addId(org.ehealth_connector.common.Identificator)
	 */
	@Override
	public void addId(Identificator id) {
		final II ii = CdaChUtil.createUniqueIiFromIdentificator(id);
		getAllergyProblem().getIds().add(ii);
	}

}
