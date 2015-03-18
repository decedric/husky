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
* Attribution-ShareAlike 4.0 Switzerland License.
*
 * Year of publication: 2015
*
 *******************************************************************************/
package org.ehealth_connector.cda.ch.textbuilder;

import java.util.ArrayList;
import java.util.List;

import org.ehealth_connector.cda.ActiveProblemConcernEntry;
import org.ehealth_connector.cda.ch.SectionsVACD;

/**
 * Builds the <text> part of the Treatment plan section.
 * 
 * Always builds the whole part (not only adds one recommendation).
 * 
 * @author ahelmer
 */
public class ActiveProblemConcernTextBuilder extends TextBuilder {

  protected final static String tableStub = "<table border=\"1\" width=\"100%\"><thead><tr><th>Risikokategorie</th><th>Risikofaktor</th></tr></thead><tbody>";
  private List<ActiveProblemConcernEntry> problemConcernEntries;
  private ActiveProblemConcernEntry newProblemConcernEntry;
  private String sectionText;
  private int newId;

  public ActiveProblemConcernTextBuilder(
      ArrayList<ActiveProblemConcernEntry> problemConcernEntries,
      ActiveProblemConcernEntry newProblemConcernEntry, String sectionText) {
    this.problemConcernEntries = problemConcernEntries;
    this.newProblemConcernEntry = newProblemConcernEntry;
    this.sectionText = sectionText;
    init();
  }

  private String buildRow(ActiveProblemConcernEntry newProblemConcernEntry2, int newId) {
    StringBuilder rowBuilder = new StringBuilder();
    rowBuilder.append("<tr>");
    rowBuilder.append(buildCell("Komplikationsrisiko"));
    rowBuilder
    .append(buildCellWithContent(
        newProblemConcernEntry2.getConcern(), newId,
        SectionsVACD.ACTIVE_PROBLEMS.getContentIdPrefix()));
    rowBuilder.append("</tr>");
    return rowBuilder.toString();
  }

  public ActiveProblemConcernEntry getProblemConcernEntry() {
    return newProblemConcernEntry;
  }

  public String getSectionText() {
    return sectionText;
  }

  public void init() {
    // ID
    if (problemConcernEntries.size() != 0) {
      newId = problemConcernEntries.size() + 1;
      if (sectionText.equals("") || sectionText == null)
        try {
          throw new Exception(
              "If there is more than zero elements, the sectionText can´t be empty.");
        } catch (Exception e) {
          e.printStackTrace();
        }
    } else {
      newId = 1;
      sectionText = tableStub + tableFooter;
    }

    sectionText = insertRow(newProblemConcernEntry, newId,
        sectionText);
  }

  public String insertRow(ActiveProblemConcernEntry newProblemConcernEntry2, int newId,
      String sectionText) {
    String rowStr = buildRow(newProblemConcernEntry2, newId);
    // TODO If there is no element found that could be replaced, then an
    // error occured (e.g. in a scenario, where an external document is
    // loaded where the table footer does not match this table footer. If
    // the convenience API is used to add a ProblemConcern then this method
    // would not find the specific text.
    // - In this case: Generate a new (convennience API conformant) set of
    // ids, update the text and the objects. For this purpose the other
    // methods of this and the super class could be useful.
    String tableStr = sectionText
        .replace(tableFooter, rowStr + tableFooter);
    return tableStr;
  }

  //
  // /**
  // * Returns HTML formatted string.
  // *
  // * @see java.lang.Object#toString()
  // */
  // public String toString() {
  // return super.toString();
  // }
  //
  // public String buildCompleteText() {
  // // tableHeader = new String[]
  // //
  // {"Impfstoff Handelsname","Hersteller","Lot-Nr","Datum","Impfung gegen","Impfung erfolgt durch"};
  // // Header
  // tableHeader = new String[] { "Risikokategorie", "Risikofaktor" };
  // // Body
  // tableBody = new String[problemConcernEntries.size()][2];
  // for (int i = 0; i < problemConcernEntries.size(); i++) {
  // tableBody[i][0] = "Komplikationsrisiko";
  // tableBody[i][1] = problemConcernEntries.get(i).getProblemConcern();
  // }
  // // References
  // referenceBodyCell = new Boolean[] { false, true };
  //
  // // Build the Complete Text for all ProblemConcernEntries
  // super.build(tableHeader, tableBody, referenceBodyCell);
  // return super.toString();
  // }

}
