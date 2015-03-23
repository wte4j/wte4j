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
package org.wte4j.impl.expression;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.wte4j.ExpressionError;
import org.wte4j.Formatter;
import org.wte4j.FormatterFactory;
import org.wte4j.FormatterInstantiationException;
import org.wte4j.UnknownFormatterException;

public class ResolverFactoryImpl implements ValueResolverFactory {

	private Locale locale;
	private FormatterFactory formatterFactory;
	private Map<String, Class<?>> contentElements;

	public ResolverFactoryImpl(Locale locale,
			FormatterFactory formatterFactory,
			Map<String, Class<?>> contentElements) {
		super();
		this.locale = locale;
		this.formatterFactory = formatterFactory;
		this.contentElements = contentElements;
	}

	@Override
	public ValueResolver<String> createStringResolver(String expressionString)
			throws InvalidExpressionException {
		WteExpression expression = new WteExpression();
		expression.setExpressionString(expressionString);
		if (!contentElements.containsKey(expression.getContentKey())) {
			throw new InvalidExpressionException(
					ExpressionError.ILLEGAL_CONTENT_KEY);
		}

		Formatter formatter = createFormatter(expression);
		return new StringResolver(expression.getContentKey(), formatter);
	}

	Formatter createFormatter(WteExpression expression) {
		try {
			Formatter formatter = new DefaultFormatterProxy(formatterFactory);
			if (StringUtils.isNotEmpty(expression.getFormattername())) {
				formatter = formatterFactory.createFormatter(
						expression.getFormattername(),
						expression.getFormatterArgs());
			}
			formatter.setLocale(locale);
			return formatter;
		} catch (UnknownFormatterException e) {
			throw new InvalidExpressionException(
					ExpressionError.UNKNOWN_FORMATTER, e);
		} catch (FormatterInstantiationException e) {
			throw new InvalidExpressionException(
					ExpressionError.ILLEGALFORMATTER_DEFINITION, e);
		}
	}

}
