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

package org.ehealth_connector.validation.service.schematron;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.ehealth_connector.validation.service.transform.StylesheetFactory;
import org.ehealth_connector.validation.service.transform.StylesheetURIResolver;
import org.ehealth_connector.validation.service.transform.Transformation;
import org.ehealth_connector.validation.service.transform.TransformationException;
import org.ehealth_connector.validation.service.util.JarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltExecutable;

/**
 * Class used for generating <cite>Schematron</cite> validation reports in HTML.
 * <p>
 * This class encapsulates a {@link Validators} instance for efficiently build
 * and retrieve compiled <cite>Schematron</cite> validator stylesheets.
 * </p>
 * <h4>Thread Safety</h4>
 * <p>
 * This class is immutable and therefore thread-safe.
 * </p>
 *
 */
public class ReportBuilder {

	/** Directory were xls-Sheets are stored */
	private static final String xslDir = JarUtils.getResourceUri("xsl", ReportBuilder.class);

	/** Style sheet for generating Validation HTML output */
	public static final String SVRL_TO_HTML = xslDir + JarUtils.separator + "svrl_to_html.xsl";
	/** Style sheet for generating Validation XML output */
	public static final String SVRL_TO_XML = xslDir + JarUtils.separator + "ms-svrl.xsl";

	/**
	 * Tries to auto-detect the matching rule-set from the specified input
	 * source document.
	 *
	 * @param processor
	 *            the <cite>Saxon Processor</cite> instance.
	 * @param source
	 *            the source document.
	 * @param ruleSetList
	 *            the list of available rule-sets.
	 * @return the one and only one matching rule-set for the specified input
	 *         source document.
	 * @throws RuleSetDetectionException
	 *             if no rule-set or if more than one rule-set was found for the
	 *             specified document, or if an error occurs when building the
	 *             document, for example a parsing error or because the type of
	 *             document supplied does not match the required type.
	 * @throws NullPointerException
	 *             if either the specified processor or source document is
	 *             <tt>null</tt>.
	 */
	public static RuleSet detectRuleSet(Processor processor, Source source, RuleSet[] ruleSetList)
			throws RuleSetDetectionException {
		if (processor == null) {
			throw new NullPointerException("Processor is null.");
		} else if (source == null) {
			throw new NullPointerException("Source document is null.");
		} else if (ruleSetList == null) {
			throw new RuleSetDetectionException(new ArrayList<RuleSet>());
		}
		final Map<String, RuleSet> ruleSetOidMap = new HashMap<String, RuleSet>();
		for (final RuleSet ruleSet : ruleSetList) {
			if (ruleSet.getTemplateId() != null) {
				ruleSetOidMap.put(ruleSet.getTemplateId(), ruleSet);
			}
		}
		final List<RuleSet> matchingRuleSets = new ArrayList<RuleSet>();
		final String expression = "/cda:ClinicalDocument/cda:templateId/@root";

		final DocumentBuilder builder = processor.newDocumentBuilder();
		final XPathCompiler compiler = processor.newXPathCompiler();
		compiler.declareNamespace("cda", "urn:hl7-org:v3");

		XPathSelector selector;
		try {
			final XdmNode document = builder.build(source);
			final XPathExecutable executable = compiler.compile(expression);
			selector = executable.load();
			selector.setContextItem(document);
		} catch (final SaxonApiException cause) {
			throw new RuleSetDetectionException(cause);
		}
		RuleSet match;
		String value;
		for (final XdmItem item : selector) {
			value = ((XdmNode) item).getStringValue();
			if ((match = ruleSetOidMap.get(value)) != null) {
				matchingRuleSets.add(match);
			}
		}
		if (matchingRuleSets.size() != 1) {
			throw new RuleSetDetectionException(matchingRuleSets);
		}
		return matchingRuleSets.get(0);
	}

	/** The SLF4J logger instance. */
	private final Logger log = LoggerFactory.getLogger(getClass());

	/** Validator class used for validatio */
	private final Validators validators;

	/** URI Resolver */
	private final URIResolver resolver;

	/** StylesheetFactoty */
	private final StylesheetFactory factory;

	/**
	 * Creates a new report builder instance.
	 *
	 * @param validators
	 *            a valid reference to the {@link Validators} instance.
	 * @throws NullPointerException
	 *             if the specified <tt>Validators</tt> instance is
	 *             <tt>null</tt>.
	 */
	public ReportBuilder(Validators validators) {
		if (validators == null) {
			throw new NullPointerException("Validators instance is null.");
		}
		this.validators = validators;
		this.resolver = new StylesheetURIResolver(xslDir);
		this.factory = new StylesheetFactory(getProcessor(), this.resolver);
	}

	/**
	 * Not used
	 *
	 * @param source
	 * @param ruleSet
	 * @throws RuleSetDetectionException
	 */
	public void checkRuleSet(Source source, RuleSet ruleSet)
			throws SAXException, RuleSetDetectionException {
		if (ruleSet.getTemplateId() != null) {
			detectRuleSet(source, new RuleSet[] { ruleSet });
		}
	}

	/**
	 * Count the errors in Schematron validation report
	 *
	 * @param in
	 *            Schematron validation report
	 * @return Number of errors
	 * @throws TransformationException
	 */
	protected long countErrors(InputStream in) throws TransformationException {
		final DocumentBuilder builder = getProcessor().newDocumentBuilder();
		final XPathCompiler compiler = getProcessor().newXPathCompiler();
		compiler.declareNamespace("svrl", "http://purl.oclc.org/dsdl/svrl");
		final String expression = "count(//svrl:failed-assert[@role='error'])"
				+ " + count(//svrl:successful-report[@role='error'])";
		try {
			final XdmNode document = builder.build(new StreamSource(in));
			final XdmItem item = compiler.evaluateSingle(expression, document);
			return ((XdmAtomicValue) item).getLongValue();
		} catch (final SaxonApiException cause) {
			throw new TransformationException(cause);
		}
	}

	/**
	 * Creates an HTML validation report for the specified input document using
	 * the given rule-set.
	 *
	 * @param ruleSet
	 *            a valid <cite>Schematron Rule-Set</cite> instance to be used
	 *            for the validation process.
	 *
	 * @param workDir
	 *            the work directory where to put/read precompiled Schematron
	 *            stylesheets
	 *
	 * @param input
	 *            the XML input to be validated as an array of bytes.
	 *
	 * @param parameters
	 *            XSLT parameters
	 *
	 * @return an HTML validation report as an array of bytes.
	 *
	 * @throws TransformationException
	 *             if the construction of the validator stylesheet aborted by
	 *             throwing an exception, if any stylesheet contains static
	 *             errors or cannot be read, or during the transformation phase,
	 *             if an error occurs when setting the source for the
	 *             transformation, or if any dynamic error occurs during the
	 *             transformation.
	 * @throws InterruptedException
	 *             if the construction of the validator stylesheet was
	 *             interrupted.
	 * @throws NullPointerException
	 *             if either the specified rule-set or input stream is
	 *             <tt>null</tt>.
	 * @see #createHTMLReport(RuleSet[], byte[])
	 * @see #createHTMLReport(RuleSet, InputStream, OutputStream)
	 */
	public byte[] createHTMLReport(RuleSet ruleSet, File workDir, byte[] input,
			Properties parameters) throws TransformationException, InterruptedException {
		final ByteArrayInputStream in = new ByteArrayInputStream(input);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		createHTMLReport(ruleSet, workDir, in, out, parameters);
		return out.toByteArray();
	}

	/**
	 * Creates an HTML <cite>Schematron</cite> validation report from the
	 * specified input using the given rule-set and sending the results to the
	 * output stream.
	 *
	 * @param ruleSet
	 *            a valid <cite>Schematron Rule-Set</cite> instance to be used
	 *            for the validation process.
	 *
	 * @param workDir
	 *            the work directory where to put/read precompiled Schematron
	 *            stylesheets
	 *
	 * @param in
	 *            the XML input to be validated.
	 * @param out
	 *            the output stream, where the validation results are written.
	 *
	 * @param parameters
	 *            XSLT parameters
	 *
	 * @throws TransformationException
	 *             if the construction of the validator stylesheet aborted by
	 *             throwing an exception, if any stylesheet contains static
	 *             errors or cannot be read, or during the transformation phase,
	 *             if an error occurs when setting the source for the
	 *             transformation, or if any dynamic error occurs during the
	 *             transformation.
	 * @throws InterruptedException
	 *             if the construction of the validator stylesheet was
	 *             interrupted.
	 * @throws NullPointerException
	 *             if either the specified rule-set or input stream is
	 *             <tt>null</tt>.
	 */
	public long createHTMLReport(RuleSet ruleSet, File workDir, InputStream in, OutputStream out,
			Properties parameters) throws TransformationException, InterruptedException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final XsltExecutable styleSheet = getValidator(ruleSet, workDir);
		final Transformation t1 = new Transformation(styleSheet);
		t1.setURIResolver(new StylesheetURIResolver(ruleSet.getPath().getParentFile()));
		t1.transform(in, baos);
		final byte[] svrl = baos.toByteArray();
		// for debugging only - comment these lines for productive releases
		// log.debug("Validation-Result input cachable: {}",
		// new Object[] { ruleSet.isCacheable() });
		// log.debug("Validation-Result SVRL: {}", new Object[] {
		// baos.toString() });
		// end of debugging only

		final Transformation t2 = new Transformation(factory.getStylesheet(SVRL_TO_HTML, false));
		t2.setURIResolver(this.resolver);
		t2.setParameters(parameters);
		t2.transform(new ByteArrayInputStream(svrl), out);
		// for debugging only - comment these lines for productive releases
		// log.debug("Validation-Result SVRL_TO_HTML: {}", new Object[] {
		// SVRL_TO_HTML });
		// log.debug("Validation-Result HTML: {}", new Object[] { out.toString()
		// });
		// end of debugging only
		return countErrors(new ByteArrayInputStream(svrl));
	}

	/**
	 * Creates an HTML validation report for the specified input document.
	 * <p>
	 * This method tries to auto-detect the rule-set to use for the validation
	 * process from the specified input document and the list of available
	 * rule-sets.
	 * </p>
	 *
	 * @param ruleSetList
	 *            the list of available rule-sets.
	 *
	 * @param workDir
	 *            the work directory where to put/read precompiled Schematron
	 *            stylesheets
	 *
	 * @param input
	 *            the XML input to be validated as an array of bytes.
	 *
	 * @param parameters
	 *            XSLT parameters
	 *
	 * @return an HTML validation report as an array of bytes.
	 *
	 * @throws RuleSetDetectionException
	 *             if no rule-set or if more than one rule-set was found for the
	 *             specified document, or if an error occurs when building the
	 *             document, for example a parsing error or because the type of
	 *             document supplied does not match the required type.
	 *
	 * @throws TransformationException
	 *             if the construction of the validator stylesheet aborted by
	 *             throwing an exception, if any stylesheet contains static
	 *             errors or cannot be read, or during the transformation phase,
	 *             if an error occurs when setting the source for the
	 *             transformation, or if any dynamic error occurs during the
	 *             transformation.
	 * @throws InterruptedException
	 *             if the construction of the validator stylesheet was
	 *             interrupted.
	 * @throws NullPointerException
	 *             if the specified input source document is <tt>null</tt>.
	 * @see #detectRuleSet(Source, RuleSet[])
	 * @see #createHTMLReport(RuleSet, byte[])
	 * @see #createHTMLReport(RuleSet, InputStream, OutputStream)
	 */
	public byte[] createHTMLReport(RuleSet[] ruleSetList, File workDir, byte[] input,
			Properties parameters) throws SAXException, RuleSetDetectionException,
			TransformationException, InterruptedException {
		final ByteArrayInputStream in = new ByteArrayInputStream(input);
		final RuleSet ruleSet = detectRuleSet(new StreamSource(in), ruleSetList);
		return createHTMLReport(ruleSet, workDir, input, parameters);
	}

	/**
	 *
	 * Creates a Schematrron result in the SVRL format
	 *
	 * @param ruleSet
	 *            the list of available rule-sets.
	 *
	 * @param workDir
	 *            the work directory where to put/read precompiled Schematron
	 *            stylesheets
	 *
	 * @param in
	 *            the XML input to be validated as an stream
	 * @param out
	 *            the output stream, where the validation results are written.
	 *
	 * @param parameters
	 *            XSLT parameters
	 *
	 * @return
	 * @throws TransformationException
	 * @throws InterruptedException
	 */
	public byte[] createSvrlReport(RuleSet ruleSet, File workDir, InputStream in, OutputStream out,
			Properties parameters) throws TransformationException, InterruptedException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ByteArrayOutputStream baos_ms = new ByteArrayOutputStream();
		final XsltExecutable styleSheet = getValidator(ruleSet, workDir);
		final Transformation t1 = new Transformation(styleSheet);
		t1.setURIResolver(new StylesheetURIResolver(ruleSet.getPath().getParentFile()));
		t1.transform(in, baos);
		final byte[] svrl = baos.toByteArray();

		// for debugging only - comment these lines for productive releases
		// OutputStream outputStream1 = null;
		// try {
		// outputStream1 = new FileOutputStream("/temp/svrl_raw_out.xml");
		// baos.writeTo(outputStream1);
		// outputStream1.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// end of debugging only

		final Transformation t2 = new Transformation(factory.getStylesheet(SVRL_TO_XML, false));
		t2.setURIResolver(this.resolver);
		t2.setParameters(parameters);
		t2.transform(new ByteArrayInputStream(svrl), baos_ms);
		final byte[] svrl_ms = baos_ms.toByteArray();

		// for debugging only - comment these lines for productive releases
		// OutputStream outputStream2 = null;
		// try {
		// outputStream2 = new FileOutputStream("/temp/svrl2xml_out.xml");
		// baos_ms.writeTo(outputStream2);
		// outputStream2.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// end of debugging only

		return svrl_ms;
	}

	/**
	 * Tries to auto-detect the matching rule-set from the specified input
	 * source document.
	 *
	 * @param source
	 *            the source document.
	 * @param ruleSetList
	 *            the list of available rule-sets.
	 * @return the one and only one matching rule-set for the specified input
	 *         source document.
	 * @throws RuleSetDetectionException
	 *             if no rule-set or if more than one rule-set was found for the
	 *             specified document, or if an error occurs when building the
	 *             document, for example a parsing error or because the type of
	 *             document supplied does not match the required type.
	 * @throws NullPointerException
	 *             if the specified source document is <tt>null</tt>.
	 */
	public RuleSet detectRuleSet(Source source, RuleSet[] ruleSetList)
			throws SAXException, RuleSetDetectionException {
		return detectRuleSet(getProcessor(), source, ruleSetList);
	}

	/**
	 * Returns the <cite>Saxon Processor</cite> instance.
	 *
	 * @return the <cite>Saxon Processor</cite> instance.
	 */
	public Processor getProcessor() {
		return validators.getProcessor();
	}

	/**
	 * Returns a compiled <cite>Schematron Validator</cite> stylesheet for the
	 * specified <cite>Schematron Rule-Set</cite>, blocking until it is
	 * available.
	 *
	 * @param ruleSet
	 *            a valid <cite>Schematron Rule-Set</cite> instance.
	 *
	 * @param workDir
	 *            the work directory where to put/read precompiled Schematron
	 *            stylesheets
	 *
	 * @return the compiled validator stylesheet.
	 *
	 * @throws TransformationException
	 *             if the construction of the compiled stylesheet aborted by
	 *             throwing an exception.
	 *
	 * @throws InterruptedException
	 *             if the construction of the compiled stylesheet was
	 *             interrupted.
	 *
	 * @throws NullPointerException
	 *             if the specified rule-set is <tt>null</tt>.
	 */
	public XsltExecutable getValidator(RuleSet ruleSet, File workDir)
			throws TransformationException, InterruptedException {
		return validators.get(ruleSet, workDir);
	}

} // End of ReportBuilder
