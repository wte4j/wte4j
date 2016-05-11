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
package org.wte4j.impl.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.wte4j.ExpressionError;
import org.wte4j.Formatter;
import org.wte4j.FormatterFactory;
import org.wte4j.FormatterInstantiationException;
import org.wte4j.UnknownFormatterException;
import org.wte4j.impl.InvalidExpressionException;

public class ValueFormatter {

	private final FormatterFactory formatterFactory;
	private final Map<Class<?>, Formatter> defaultFormaters;

	private String formatterName;
	private List<String> formatterArgs;

	public ValueFormatter(FormatterFactory formatterFactory, Locale locale) {
		super();
		this.formatterFactory = formatterFactory;
		defaultFormaters = new HashMap<>();
	}

	public String format(Object value, Locale locale) throws InvalidExpressionException {
		Formatter formatter = getFormatter(value.getClass());
		try {
			formatter.setLocale(locale);
			return formatter.format(value);
		} catch (ClassCastException e) {
			throw new InvalidExpressionException(ExpressionError.FORMATTER_TYPE_MISSMATCH);
		}
	}

	public void validate(Class<?> inputType) throws InvalidExpressionException {
		getFormatter(inputType);
	}

	private Formatter getFormatter(Class<?> inputType) {
		Formatter formatter = getDefaultFormatter(inputType);
		if (formatterName != null) {
			formatter = createFormatter(formatterName, formatterArgs);
		}
		return formatter;
	}

	public void clear() {
		formatterName = null;
		formatterArgs = Collections.emptyList();
	}

	public void setFormatterName(String formatterName) {
		this.formatterName = formatterName;
	}

	public void setFormatterArgs(List<String> formatterArgs) {
		this.formatterArgs = formatterArgs;
	}

	private Formatter getDefaultFormatter(Class<?> type) {
		Formatter formatter = defaultFormaters.get(type);
		if (formatter == null) {
			formatter = formatterFactory.createDefaultFormatter(type);
		}
		return formatter;
	}

	Formatter createFormatter(String name, List<String> args) {
		try {
			return formatterFactory.createFormatter(name, args);
		} catch (UnknownFormatterException e) {
			throw new InvalidExpressionException(
					ExpressionError.UNKNOWN_FORMATTER, e);
		} catch (FormatterInstantiationException e) {
			throw new InvalidExpressionException(
					ExpressionError.ILLEGALFORMATTER_DEFINITION, e);
		}
	}

}
