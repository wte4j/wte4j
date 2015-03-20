/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.born.wte.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.born.wte.ExpressionError;
import ch.born.wte.InvalidTemplateException;
import ch.born.wte.WteDataModel;
import ch.born.wte.impl.expression.InvalidExpressionException;
import ch.born.wte.impl.expression.ValueResolver;
import ch.born.wte.impl.expression.ValueResolverFactory;
import ch.born.wte.impl.word.Docx4JWordTemplate;
import ch.born.wte.impl.word.PlainTextContent;

/**
 * {@linkplain DocumentGenerator} is a helper class for creating word documents
 * from a word template.
 */
class DocumentGenerator {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private ValueResolverFactory resolverFactory;
	private Docx4JWordTemplate wordDocument;
	private Map<String, ValueResolver<String>> expressions;

	public DocumentGenerator(Docx4JWordTemplate wordDocument,
			ValueResolverFactory aResolverFactory) {
		super();
		this.resolverFactory = aResolverFactory;
		this.wordDocument = wordDocument;
		this.expressions = new HashMap<String, ValueResolver<String>>();
	}

	/**
	 * Writes this document as word xml to a OutputStream and closed the stream
	 * in any case.
	 * 
	 * @param out
	 *            - the Outputstream to write the document.
	 * @throws IOException
	 */
	public void writeAsOPCXml(OutputStream out) throws IOException {
		wordDocument.writeAsOPCXml(out);

	}

	/**
	 * Writes this document as docx to a OutputStream and closed the stream in
	 * any case.
	 * 
	 * @param out
	 *            - the Outputstream to write the document.
	 * @throws IOException
	 */
	public void writeAsOpenXML(OutputStream out) throws IOException {
		wordDocument.writeAsOpenXML(out);

	}

	public void parseExpressions() throws InvalidTemplateException {
		Map<String, ExpressionError> errors = new HashMap<String, ExpressionError>();
		for (PlainTextContent content : wordDocument.getPlainTextContent()) {
			String expression = content.getExpression();
			ExpressionError error = parseExpression(expression);
			if (error != null) {
				errors.put(expression, error);
			}
		}
		checkErrors(errors);
	}

	private ExpressionError parseExpression(String expressionString) {
		try {
			ValueResolver<String> resolver = resolverFactory
					.createStringResolver(expressionString);
			expressions.put(expressionString, resolver);
		} catch (InvalidExpressionException e) {
			logger.debug("Error on on parsing expression '{}' ",
					expressionString, e);
			return e.getError();
		}
		return null;
	}

	public void setContent(WteDataModel dataModel)
			throws InvalidTemplateException {
		Map<String, ExpressionError> errors = new HashMap<String, ExpressionError>();
		for (PlainTextContent content : wordDocument.getPlainTextContent()) {
			ExpressionError error = setContent(content, dataModel);
			if (error != null) {
				errors.put(content.getExpression(), error);
			}
		}
		checkErrors(errors);
	}

	private ExpressionError setContent(PlainTextContent content,
			WteDataModel dataModel) {
		final String expression = content.getExpression();
		try {
			ValueResolver<String> resolver = expressions.get(expression);
			if (resolver != null) {
				String value = resolver.resolve(dataModel);
				content.setContent(value);
				content.hideMarkers();
			} else {
				logger.warn("expression '{}' was not parsed", expression);
			}
		} catch (ClassCastException e) {
			return ExpressionError.TYPE_MISSMATCH;
		}
		return null;
	}

	private void checkErrors(Map<String, ExpressionError> errors) {
		if (!errors.isEmpty()) {
			throw new InvalidTemplateException(errors);
		}
	}

	protected Map<String, ValueResolver<String>> getExpressions() {
		return expressions;
	}

}
