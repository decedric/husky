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

package org.ehealth_connector.validation.service.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ehealth_connector.validation.service.config.bind.ApplicationType;
import org.ehealth_connector.validation.service.config.bind.ConfigurationType;
import org.ehealth_connector.validation.service.config.bind.RuleSetType;
import org.ehealth_connector.validation.service.config.bind.SchematronType;
import org.ehealth_connector.validation.service.schematron.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the application's configuration.
 */
public class Configuration {

	/** The SLF4J logger instance. */
	private final Logger log = LoggerFactory.getLogger(getClass());

	/** The underlying unmarshalled configuration element. */
	private final ConfigurationType configuration;

	/** Map object with current RuleSets */
	private final Map<String, RuleSet> ruleSetMap = new LinkedHashMap<String, RuleSet>();

	/** Map object with current RuleSets */
	private final Map<String, RuleSet> ruleSetOidMap = new HashMap<String, RuleSet>();

	/**
	 * Creates a new configuration instance wrapping the specified
	 * configuration.
	 *
	 * @param configuration
	 *            the underlying <tt>ConfigurationType</tt> instance, this
	 *            configuration should be based on.
	 * @throws NullPointerException
	 *             if the specified configuration is <tt>null</tt>.
	 */
	public Configuration(ConfigurationType configuration) throws ConfigurationException {
		if (configuration == null) {
			throw new NullPointerException("Configuration is null.");
		}
		this.configuration = configuration;
		createRuleSetMaps();
	}

	/**
	 * Creates and returns the list of configured rule-sets.
	 *
	 * @return the list of configured rule-sets.
	 */
	protected List<RuleSet> createRuleSetList() {
		final SchematronType schematron = configuration.getSchematron();
		final List<RuleSet> ruleSetList = new ArrayList<RuleSet>();
		try {
			final File ruleSetDir = getRuleSetsDir();
			for (final RuleSetType ruleSetType : schematron.getRuleSets()) {
				ruleSetList.add(new RuleSetImpl(ruleSetType, ruleSetDir));
			}
		} catch (final ConfigurationException e) {
			log.error("RuleSet is invalid: '{}'", e);
		}
		return ruleSetList;
	}

	/**
	 * @return
	 */
	private void createRuleSetMaps() {
		ruleSetMap.clear();
		ruleSetOidMap.clear();
		for (final RuleSet ruleSet : createRuleSetList()) {
			ruleSetMap.put(ruleSet.getId(), ruleSet);
			if (ruleSet.getTemplateId() != null) {
				RuleSet first = null;
				if ((first = ruleSetOidMap.put(ruleSet.getTemplateId(), ruleSet)) != null) {
					final Object[] args = new Object[] { ruleSet.getTemplateId(),
							first.getPath().getName(), ruleSet.getPath().getName() };
					log.error("RuleSet OID {} is not unique." + " It was declared in '{}' and '{}'",
							args);
				}
			}
		}
	}

	/**
	 * @return
	 */
	public File getBaseDir() {
		return configuration.getBaseDir();
	}

	public File getConfigurationDir() {
		return configuration.getConfigurationDir();
	}

	/**
	 * @return
	 */
	public String getDocumentSchema() throws ConfigurationException {
		final ApplicationType application = configuration.getApplication();
		if (application != null) {
			File schema = new File(application.getDocumentSchema());
			if (!schema.isFile()) {
				schema = new File(getBaseDir() + "/" + application.getDocumentSchema());
			}
			if (!schema.isFile()) {
				schema = new File(getConfigurationDir() + "/" + application.getDocumentSchema());
			}
			if (!schema.isFile()) {
				throw new ConfigurationException(
						"Document schema not found (baseDir: " + getBaseDir() + ")");
			}
			if (!schema.canRead()) {
				throw new ConfigurationException("Document schema is not readable");
			}
			return schema.getAbsolutePath();
		}
		return null;
	}

	/**
	 * @return
	 */
	public String getDownloadsUrl() {
		final ApplicationType application = configuration.getApplication();
		return (application != null ? application.getDownloadsUrl() : null);
	}

	/**
	 * @return
	 */
	public String getLicenseKey() {
		final ApplicationType application = configuration.getApplication();
		return (application != null ? application.getLicenseKey() : null);
	}

	/**
	 * @return
	 */
	public String getPdfLevel() {
		final ApplicationType application = configuration.getApplication();
		return (application != null ? application.getPdfLevel() : null);
	}

	/**
	 * @return
	 */
	public String getPdfReportingLevel() {
		final ApplicationType application = configuration.getApplication();
		return (application != null ? application.getPdfReportingLevel() : null);
	}

	/**
	 * Returns the rule-set to which the specified <tt>id</tt> is mapped, or
	 * <tt>null</tt> if no such rule-set is found.
	 *
	 * @param id
	 *            the identifier of the rule-set.
	 * @return the rule-set to which the specified <tt>id</tt> is mapped, or
	 *         <tt>null</tt> if no such rule-set is found.
	 */
	public RuleSet getRuleSet(String id) {
		return ruleSetMap.get(id);
	}

	/**
	 * Returns an ordered list of all available rule-sets.
	 *
	 * @return the list of available rule-sets as an array.
	 */
	public RuleSet[] getRuleSetList() {
		return ruleSetMap.values().toArray(new RuleSet[0]);
	}

	/**
	 * @return
	 */
	public int getRuleSetsCount() {
		return getRuleSetList().length;
	}

	/**
	 * Returns the base directory of the <cite>Schematron</cite> rule-sets.
	 *
	 * @return the absolute path denoted by the <tt>dir</tt> attribute of the
	 *         <tt>schematron</tt> element.
	 */
	public File getRuleSetsDir() throws ConfigurationException {
		File schematron = new File(configuration.getSchematron().getDirectory());
		if (!schematron.getAbsoluteFile().isDirectory()) {
			schematron = new File(
					getBaseDir() + "/" + configuration.getSchematron().getDirectory());
		}
		if (!schematron.isDirectory()) {
			schematron = new File(
					getConfigurationDir() + "/" + configuration.getSchematron().getDirectory());
		}
		if (!schematron.isDirectory()) {
			throw new ConfigurationException("Schematron directory not found");
		}
		if (!schematron.canRead()) {
			throw new ConfigurationException("Schematron directory is not readable");
		}
		return schematron.getAbsoluteFile();
	}

	/**
	 * @return
	 */
	public String getTheme() {
		final ApplicationType application = configuration.getApplication();
		return (application != null ? application.getTheme() : null);
	}

	/**
	 * @return
	 */
	public File getUserDir() {
		return configuration.getUserDir();
	}

	/**
	 * @return
	 */
	public File getWorkDir() {
		try {
			return configuration.getWorkDir();
		} catch (final ConfigurationException e) {
			log.error("<<< Configuration failed: " + e.getCause());
			return configuration.getBaseDir();
		}
	}

}
