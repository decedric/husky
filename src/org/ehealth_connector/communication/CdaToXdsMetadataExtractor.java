/********************************************************************************
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
 ********************************************************************************/

package org.ehealth_connector.communication;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ehealth_connector.cda.ch.CdaChVacd;
import org.ehealth_connector.common.Author;
import org.openhealthtools.ihe.xds.metadata.DocumentEntryType;
import org.openhealthtools.ihe.xds.metadata.extract.MetadataExtractionException;
import org.openhealthtools.ihe.xds.metadata.extract.cdar2.CDAR2Extractor;

public class CdaToXdsMetadataExtractor {

	org.openhealthtools.mdht.uml.cda.ClinicalDocument iDoc;
	DocumentEntryType docEntry;

	@SuppressWarnings("deprecation")
	public CdaToXdsMetadataExtractor(CdaChVacd doc) {
		Category.shutdown();
		Logger.getRootLogger().setLevel(Level.OFF);

		iDoc = doc.getDoc();
		docEntry = extract();
	}

	public String cGetLanguageCode() {
		return docEntry.getLanguageCode();
	}

	private DocumentEntryType extract() {
		CDAR2Extractor extractor = new CDAR2Extractor(iDoc);
		try {
			docEntry = extractor.extract();
		} catch (MetadataExtractionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("success\n"+docEntry.toString());
		return docEntry;
	}

	public Author extractAuthor() {
		DocumentEntryType docEntry = extract();
		org.openhealthtools.ihe.xds.metadata.AuthorType iAuthor = (org.openhealthtools.ihe.xds.metadata.AuthorType) docEntry
				.getAuthors().get(0);
		org.ehealth_connector.common.Author author = new Author(iAuthor);
		return author;
	}
}
