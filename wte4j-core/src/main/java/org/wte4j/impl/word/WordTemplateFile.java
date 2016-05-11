/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j.impl.word;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wte4j.ExpressionError;
import org.wte4j.InvalidTemplateException;
import org.wte4j.TemplateFile;
import org.wte4j.impl.InvalidExpressionException;
import org.wte4j.impl.TemplateContext;
import org.wte4j.impl.expression.WteExpression;

/**
 * {@linkplain WordTemplateFile} is a {@link Docx4JWordTemplate} witch supports
 * filling in content and validating the content control elements from a word
 * template.
 */
public class WordTemplateFile extends Docx4JWordTemplate implements TemplateFile {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public WordTemplateFile() {
		super();
	}

	public WordTemplateFile(InputStream in) throws IOException {
		super(in);
	}

	public void validate(TemplateContext<?> context) throws InvalidTemplateException {
		Map<String, ExpressionError> errors = new HashMap<String, ExpressionError>();
		for (PlainTextContent content : getPlainTextContent()) {
			ExpressionError error = context.validate(content.getExpression());
			if (error != null) {
				errors.put(content.getExpression(), error);
			}
		}
		checkErrors(errors);
	}

	public void updateDynamicContent(TemplateContext<?> context)
			throws InvalidTemplateException {
		Map<String, ExpressionError> errors = new HashMap<String, ExpressionError>();
		for (PlainTextContent content : getPlainTextContent()) {
			ExpressionError error = setContent(content, context);
			if (error != null) {
				errors.put(content.getExpression(), error);
			}
		}
		checkErrors(errors);
	}

	private ExpressionError setContent(PlainTextContent content, TemplateContext<?> context) {
		final String expression = content.getExpression();
		try {
			String value = context.resolveValue(expression);
			content.setContent(value);
			content.hideMarkers();

		} catch (InvalidExpressionException e) {
			return e.getError();
		}
		return null;
	}

	private void checkErrors(Map<String, ExpressionError> errors) {
		if (!errors.isEmpty()) {
			throw new InvalidTemplateException(errors);
		}
	}

	@Override
	public List<String> listContentIds() {
		WteExpression expression = new WteExpression();
		List<String> contendIds = new ArrayList<String>();
		for (PlainTextContent content : getPlainTextContent()) {
			expression.setExpressionString(content.getExpression());
			contendIds.add(expression.getContentKey());
		}
		return contendIds;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		try {
			writeAsOpenXML(out);
		} finally {
			out.close();
		}
	}

	@Override
	public void write(File file) throws IOException {
		try (OutputStream out = Files.newOutputStream(file.toPath())) {
			write(out);
		}
	}
}
