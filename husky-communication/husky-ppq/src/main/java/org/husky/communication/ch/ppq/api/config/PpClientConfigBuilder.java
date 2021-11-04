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
package org.husky.communication.ch.ppq.api.config;

import org.husky.xua.communication.config.SoapClientConfigBuilder;

/**
 * <!-- @formatter:off -->
 * <div class="en">Interface describing the PpClientConfigBuilder methods.</div>
 * <div class="de">Interface beschreibt die Methoden des PpClientConfigBuilder.</div>
 * <div class="fr"></div>
 * <div class="it"></div>
 * <!-- @formatter:on -->
 */
public interface PpClientConfigBuilder extends SoapClientConfigBuilder {

	@Override
	PpClientConfigBuilder clientKeyStore(String clientKeyStoreFile);

	@Override
	PpClientConfigBuilder clientKeyStorePassword(String clientKeyStorePassword);

	@Override
	PpClientConfigBuilder clientKeyStoreType(String clientKeyStoreType);

	/**
	  * <!-- @formatter:off -->
	 * <div class="en">Creates a concrete instance of PpClientConfig with setted params.</div>
	 * <div class="de">Erstellt die konkrete Instanz des PpClientConfig mit den gesetzten Parametern.</div>
	 * <div class="fr"></div>
	 * <div class="it"></div>
	 *
	 * @return
	 *  <div class="en">a new PpClientConfig instance with the params set</div>
	 *  <div class="de">eine neue PpClientConfig Instanz mit den gesetzten Parametern</div>
	 *  <div class="fr"></div>
	 *  <div class="it"></div>
	 *  <!-- @formatter:on -->
	 */
	PpClientConfig create();

	/**
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.communication.config.SoapClientConfigBuilder#portName(java.lang.String)
	 */
	@Override
	PpClientConfigBuilder portName(String portName);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.communication.config.SoapClientConfigBuilder#portNamespace(java.lang.String)
	 */
	@Override
	PpClientConfigBuilder portNamespace(String portNamespace);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.communication.config.SoapClientConfigBuilder#serviceName(java.lang.String)
	 */
	@Override
	PpClientConfigBuilder serviceName(String serviceName);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.communication.config.SoapClientConfigBuilder#serviceNamespace(java.lang.String)
	 */
	@Override
	PpClientConfigBuilder serviceNamespace(String serviceNamespace);

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.husky.xua.communication.config.ClientConfigBuilder#url(java.lang.String)
	 */
	@Override
	PpClientConfigBuilder url(String aEndpointUrl);

}