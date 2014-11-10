/*******************************************************************************
*
* The authorship of this code and the accompanying materials is held by
* medshare GmbH, Switzerland. All rights reserved.
* http://medshare.net
*
* Project Team: https://sourceforge.net/p/ehealthconnector/wiki/Team/
*
* This code is are made available under the terms of the
* Eclipse Public License v1.0.
*
* Accompanying materials are made available under the terms of the
* Creative Commons Attribution-ShareAlike 3.0 Switzerland License.
*
* Year of publication: 2014
*
*******************************************************************************/

package ehealthconnector.cda.communication;

public class XdsResponse {
	/**
	 * Eine Liste mit evtl. aufgetretenden Fehlern
	 */
	XdsErrorList xdsErrorList;

	/**
	 * Gibt den Gesamtstatus der Transaktion wieder
	 */
	XdsStatus xdsStatus;

	/**
	 * true, wenn die Transaktion erfolgreich war. Sonst: false.
	 */
	boolean isComplete;

}
