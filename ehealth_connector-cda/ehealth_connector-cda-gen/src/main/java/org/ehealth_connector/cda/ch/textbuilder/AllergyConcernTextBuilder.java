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

package org.ehealth_connector.cda.ch.textbuilder;

import java.util.List;

import org.ehealth_connector.cda.AllergyConcern;
import org.ehealth_connector.cda.AllergyProblem;
import org.ehealth_connector.cda.ch.enums.SectionsVACD;
import org.ehealth_connector.common.utils.Util;
import org.openhealthtools.mdht.uml.cda.EntryRelationship;

/**
 * Builds the &lt;text&gt; part of the Immunization recommendations.
 * 
 * Always builds the whole part (not only adds one immunization recommendation).
 * 
 */
public class AllergyConcernTextBuilder extends TextBuilder {

	private List<org.ehealth_connector.cda.AllergyConcern> problemConcerns;
	private String contentIdPrefix;

	/**
	 * Constructor.
	 * 
	 * @param problemConcerns
	 *            a list of problem concerns
	 * @param section
	 *            the section
	 */
	public AllergyConcernTextBuilder(List<AllergyConcern> problemConcerns, SectionsVACD section) {
		this.problemConcerns = problemConcerns;
		contentIdPrefix = section.getContentIdPrefix();
	}

	/**
	 * Returns HTML formatted string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		append("<table border='1' width='100%'>");
		addHeader();
		addBody();
		append("</table>");
		return super.toString();
	}

	private void addBody() {
		append("<tbody>");
		int i = 1;
		for (final org.ehealth_connector.cda.AllergyConcern problemConcern : problemConcerns) {
			addRow(problemConcern, i++);
		}
		append("</tbody>");
	}

	private void addHeader() {
		append("<thead>");
		append("<tr>");
		append("<th>Risikokategorie</th>");
		append("<th>Risikofaktor</th>");
		append("<th>Kommentar</th>");
		append("</tr>");
		append("</thead>");
	}

	private void addRow(AllergyConcern allergyConcern, int i) {
		append("<tr>");
		addCell("Komplikationsrisiko");
		if (allergyConcern.getConcern() != null) {
			addCellWithContent(allergyConcern.getConcern(), contentIdPrefix, i);
		} else {
			addCell("");
		}
		if (allergyConcern.getAllergyProblems() == null) {
			addCell("");
		} else {
			// Create a string with more than one reference
			String cellStr = "<td>";
			int j = 0;
			boolean minOneComment = false;
			for (final AllergyProblem ap : allergyConcern.getAllergyProblems()) {
				if (ap.getMdhtAllergyProblem().getEntryRelationships() != null) {
					for (final EntryRelationship er : ap.getMdhtAllergyProblem()
							.getEntryRelationships()) {
						if (Util.isComment(er)) {
							j++;
							minOneComment = true;
							cellStr = cellStr + ("<content ID='"
									+ SectionsVACD.ALLERGIES_REACTIONS.getContentIdPrefix()
									+ "-comment" + i + j + "'>");
							cellStr = cellStr + (ap.getCommentText());
							cellStr = cellStr + ("</content>");
						}
					}
				}
			}
			if (minOneComment) {
				cellStr = cellStr + "</td>";
				append(cellStr);
			}
		}
		append("</tr>");
	}
}