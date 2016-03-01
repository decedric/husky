package org.ehealth_connector.cda.ihe.lab;

import java.util.Date;

import org.ehealth_connector.cda.MdhtEntryActFacade;
import org.ehealth_connector.common.utils.DateUtil;
import org.openhealthtools.mdht.uml.cda.ihe.lab.LABFactory;
import org.openhealthtools.mdht.uml.cda.ihe.lab.SpecimenReceived;

public class SpecimenReceivedEntry
		extends MdhtEntryActFacade<org.openhealthtools.mdht.uml.cda.ihe.lab.SpecimenReceived> {

	public SpecimenReceivedEntry() {
		super(LABFactory.eINSTANCE.createSpecimenReceived().init());
	}

	// Required Elements
	public SpecimenReceivedEntry(Date effectiveTime) {
		this();
		setEffectiveTime(effectiveTime);
	}

	protected SpecimenReceivedEntry(SpecimenReceived mdht) {
		super(mdht);
	}

	protected Date getEffectiveTime() {
		return DateUtil.parseIVL_TSVDateTimeValue(getMdht().getEffectiveTime());
	}

	protected void setEffectiveTime(Date effectiveTime) {
		getMdht().setEffectiveTime(DateUtil.convertDateyyyyMMddHHmmssZZZZ(effectiveTime));
	}
}
