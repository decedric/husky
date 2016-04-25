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

package org.ehealth_connector.validation.service.transform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.ehealth_connector.validation.service.util.Computable;
import org.ehealth_connector.validation.service.util.Exceptions;
import org.ehealth_connector.validation.service.util.JarUtils;
import org.ehealth_connector.validation.service.util.Memoizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

/**
 * A thread-safe, caching factory for compiled XSL stylesheets.
 *
 */
public class StylesheetFactory {
	/**
	 * A {@link Computable} which takes an XSL source document as argument and
	 * produces a compiled stylesheet.
	 */
	private final Computable<Source, XsltExecutable> computable = new Computable<Source, XsltExecutable>() {
		@Override
		public XsltExecutable compute(Source source) throws Exception {
			return compileStylesheet(source);
		}
	};

	/** Thread-safe cache for storing the compiled stylesheets. */
	private final Memoizer<Source, XsltExecutable> cache = new Memoizer<Source, XsltExecutable>(
			computable);

	/** The SLF4J logger instance. */
	private final Logger log = LoggerFactory.getLogger(getClass());

	/** The <cite>Saxon Processor</cite> instance. */
	private final Processor processor;

	private final URIResolver resolver;

	/**
	 * Constructs a new <tt>TransformationFactory</tt> instance.
	 *
	 * @param processor
	 *            the <cite>Saxon Processor</cite> instance.
	 * @throws NullPointerException
	 *             if the specified <tt>processor</tt> is <tt>null</tt>.
	 */
	public StylesheetFactory(Processor processor) {
		this(processor, null);
	}

	public StylesheetFactory(Processor processor, URIResolver resolver) {
		if (processor == null) {
			throw new NullPointerException("Processor is null.");
		}
		this.processor = processor;
		this.resolver = resolver;
	}

	/**
	 * Compiles a stylesheet from the specified source.
	 *
	 * @param source
	 *            the source XSL stylesheet to be compiled.
	 * @return a compiled stylesheet instance.
	 * @throws TransformationException
	 *             if the stylesheet contains static errors or if it cannot be
	 *             read.
	 * @throws NullPointerException
	 *             if the specified source is <tt>null</tt>.
	 */
	private XsltExecutable compileStylesheet(Source source) throws TransformationException {
		if (source == null)
			throw new NullPointerException("Source is null.");
		final String sourceName = Transformation.describeSource(source);
		final XsltCompiler compiler = getProcessor().newXsltCompiler();
		if (resolver != null)
			compiler.setURIResolver(resolver);
		try {
			log.info("Compiling stylesheet '{}'", sourceName);
			return compiler.compile(source);
		} catch (final SaxonApiException e) {
			throw new TransformationException("Failed to compile stylesheet '{}'" + sourceName, e);
		}
	}

	/**
	 * Returns this factory's <cite>Saxon Processor</cite> instance.
	 *
	 * @return this factory's <cite>Saxon Processor</cite> instance.
	 */
	public Processor getProcessor() {
		return processor;
	}

	/**
	 * Gets a compiled stylesheet from the specified XSL file.
	 *
	 * @param file
	 *            an XSL stylesheet source file.
	 * @param useCache
	 *            allows/disallows usage of the cache.
	 * @return a compiled stylesheet instance.
	 * @throws TransformationException
	 *             if the stylesheet contains static errors or if it cannot be
	 *             read.
	 * @throws NullPointerException
	 *             if the specified file is <tt>null</tt>.
	 */
	public XsltExecutable getStylesheet(File file, boolean useCache)
			throws TransformationException {
		if (file == null)
			throw new NullPointerException("File is null.");
		return getStylesheet(new StreamSource(file), useCache);
	}

	/**
	 * Gets a compiled stylesheet from the specified source.
	 *
	 * @param source
	 *            the source XSL stylesheet to be compiled.
	 * @param cacheable
	 *            allows/disallows usage of the cache.
	 * @return a compiled stylesheet instance.
	 * @throws TransformationException
	 *             if the stylesheet contains static errors or if it cannot be
	 *             read.
	 * @throws NullPointerException
	 *             if the specified source is <tt>null</tt>.
	 */
	public XsltExecutable getStylesheet(Source source, boolean useCache)
			throws TransformationException {
		try {
			return (useCache ? cache : computable).compute(source);
		} catch (final Exception e) {
			if (e.getCause() instanceof TransformationException) {
				throw (TransformationException) e.getCause();
			}
			throw Exceptions.launderThrowable(e);
		}
	}

	/**
	 * Gets a compiled stylesheet from the specified URI.
	 *
	 * @param uri
	 *            a string that conforms to the URI syntax.
	 * @param useCache
	 *            allows/disallows usage of the cache.
	 * @return a compiled stylesheet instance.
	 * @throws TransformationException
	 *             if the stylesheet contains static errors or if it cannot be
	 *             read.
	 * @throws NullPointerException
	 *             if the specified URI is <tt>null</tt>.
	 */
	public XsltExecutable getStylesheet(String uri, boolean useCache)
			throws TransformationException {
		if (uri == null) {
			throw new NullPointerException("URI is null.");
		}
		InputStream in = null;
		try {
			in = new URL(uri).openStream();
			final StreamSource streamSource = new StreamSource(in);
			streamSource.setPublicId(JarUtils.getRelativeUri(uri, getClass()));
			return getStylesheet(streamSource, useCache);
		} catch (final IOException e) {
			throw new TransformationException(
					"An I/O error occured when trying to access the stylesheet.", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (final IOException e) {
				}
		}
	}

}
