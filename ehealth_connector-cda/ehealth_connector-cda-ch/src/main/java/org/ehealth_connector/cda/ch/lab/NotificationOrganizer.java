package org.ehealth_connector.cda.ch.lab;

import org.ehealth_connector.common.enums.StatusCode;

public class NotificationOrganizer extends org.ehealth_connector.cda.ihe.lab.NotificationOrganizer {
	public NotificationOrganizer() {
		super();
		setStatusCode(StatusCode.COMPLETED);
	}

	public NotificationOrganizer(
			org.openhealthtools.mdht.uml.cda.ihe.lab.NotificationOrganizer mdht) {
		super(mdht);
	}

	public OutbreakIdentificationObservation getOutbreakIdentificationObservation() {
		return new OutbreakIdentificationObservation(getMdht().getOutbreakIdentifications().get(0));
	}

	public void setOutbreakIdentification(OutbreakIdentificationObservation outbreakIdentification) {
		getMdht().addObservation(outbreakIdentification.copy());
	}
}