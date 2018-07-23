/*
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
package org.ehealth_connector.cda.ch.lab.lrep;

import java.util.Date;

import org.ehealth_connector.cda.utils.CdaUtil;
import org.ehealth_connector.common.Identificator;
import org.openhealthtools.mdht.uml.cda.ihe.lab.SpecimenReceived;

/**
 * The derived SpecimenReceivedEntry. This is just to add the Swiss
 * templpateIds.
 */
public class SpecimenReceivedEntry extends org.ehealth_connector.cda.ihe.lab.SpecimenReceivedEntry {

	/**
	 * Instantiates a new specimen received entry.
	 */
	public SpecimenReceivedEntry() {
		super();
		CdaUtil.addTemplateIdOnce(getMdht(), new Identificator("2.16.756.5.30.1.1.10.4.12"));
	}

	/**
	 * Instantiates a new specimen received entry.
	 *
	 * @param effectiveTime
	 *            the effective time
	 * @param id
	 *            the id
	 */
	public SpecimenReceivedEntry(Date effectiveTime, Identificator id) {
		super(effectiveTime, id);
		CdaUtil.addTemplateIdOnce(getMdht(), new Identificator("2.16.756.5.30.1.1.10.4.12"));
	}

	/**
	 * Instantiates a new specimen received entry.
	 *
	 * @param mdht
	 *            the mdht
	 */
	public SpecimenReceivedEntry(SpecimenReceived mdht) {
		super(mdht);
		CdaUtil.addTemplateIdOnce(getMdht(), new Identificator("2.16.756.5.30.1.1.10.4.12"));
	}

}