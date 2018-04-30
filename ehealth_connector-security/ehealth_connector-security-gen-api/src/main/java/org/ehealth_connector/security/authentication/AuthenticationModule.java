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
package org.ehealth_connector.security.authentication;

import org.ehealth_connector.security.communication.config.IdpClientConfig;
import org.ehealth_connector.security.exceptions.ClientSendException;
import org.ehealth_connector.security.saml2.Response;

/**
 * <!-- @formatter:off -->
 * <div class="en">Describing the methods of the Authentication Module Interface</div>
 * <div class="de">Beschreibung der Methoden des Authentication Module Interfaces</div>
 * <div class="fr">VOICIFRANCAIS</div>
 * <div class="it">ITALIANO</div>
 * <!-- @formatter:on -->
 */
public interface AuthenticationModule {

	/**
	 * 
	 * <!-- @formatter:off -->
	 * <div class="en">Method to get the authentication assertion from an idp.</div>
	 * <div class="de">Methode um die authentifizierungs assertion eines IdP zu erhalten.</div>
	 * <div class="fr">VOICIFRANCAIS</div>
	 * <div class="it">ITALIANO</div>
	 *
	 * @param aAuthnRequest
	 * <div class="en">The AuthnRequest to be sent to the IdP.</div>
	 * <div class="de">Der AuthnRequest der zum IdP geschickt werden soll.</div>
	 * <div class="fr">VOICIFRANCAIS</div>
	 * <div class="it">ITALIANO</div>
	 * 
	 * @param clientConfiguration
	 * <div class="en">The client configuration to be used to communicate with the IdP service.</div>
	 * <div class="de">Die Clientkonfiguartion di gebraucht wird um mit dem Service des IdP zu kommunizieren.</div>
	 * <div class="fr">VOICIFRANCAIS</div>
	 * <div class="it">ITALIANO</div>
	 * 
	 * @return
	 * <div class="en">The response to the AuthnRequest from IdP service.</div>
	 * <div class="de">Die Antwort auf die Authn Anfrage des  Service des IdP.</div>
	 * <div class="fr">VOICIFRANCAIS</div>
	 * <div class="it">ITALIANO</div>
	 * <!-- @formatter:on -->
	 * @throws ClientSendException 
	 */
	Response invokeUserAuthentication(AuthnRequest aAuthnRequest, IdpClientConfig clientConfiguration)
			throws ClientSendException;

}