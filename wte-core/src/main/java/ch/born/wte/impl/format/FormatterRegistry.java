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
package ch.born.wte.impl.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.FormatterName;
import ch.born.wte.UnknownFormatterException;

@Component
public class FormatterRegistry implements FormatterFactory {

	private static final Class<?>[] INTERNAL_FORMATTER = {
			NumberFormatter.class, DateFormatter.class, TimeFormatter.class,
			DateTimeFormater.class, CustomDateTimeFormatter.class };
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Class<? extends Formatter>> namedFormatters;
	private Map<Class<?>, Class<? extends Formatter>> defaultFormatters;

	public FormatterRegistry() {
		namedFormatters = new HashMap<String, Class<? extends Formatter>>();
		defaultFormatters = new HashMap<Class<?>, Class<? extends Formatter>>();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initBuildInFormatter() {
		for (Class<?> formatter : INTERNAL_FORMATTER) {
			registerClass((Class<? extends Formatter>) formatter);
		}
	}

	public void registerClass(Class<? extends Formatter> someClass) {
		registerNamedFormatter(someClass);
		registerDefaultFormatter(someClass);
	}

	void registerNamedFormatter(Class<? extends Formatter> someClass) {
		FormatterName annotatedName = someClass
				.getAnnotation(FormatterName.class);
		String name = someClass.getSimpleName();
		if (annotatedName != null) {
			name = annotatedName.value();
		}
		if (namedFormatters.containsKey(name)) {
			logger.warn("replace formatter {} with class {}", name,
					someClass.getName());
		}
		namedFormatters.put(name, (Class<? extends Formatter>) someClass);
		logger.info("registered formatter {} with name {}",
				someClass.getName(), name);
	}

	private void registerDefaultFormatter(Class<? extends Formatter> someClass) {
		DefaultFormatter defaultAnnnotation = someClass
				.getAnnotation(DefaultFormatter.class);
		if (defaultAnnnotation != null) {
			checkDefaultFormatterClass(someClass);
			for (Class<?> type : defaultAnnnotation.value()) {
				registerDefaultFormatter(type, someClass);
			}
		}
	}

	private void checkDefaultFormatterClass(Class<? extends Formatter> someClass) {
		try {
			someClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Can not create Intsance with default Constructor ", e);
		}
	}

	void registerDefaultFormatter(Class<?> type,
			Class<? extends Formatter> someClass)
			throws IllegalArgumentException {
		if (defaultFormatters.containsKey(type)) {
			logger.warn("replace formatter for {} with class {}",
					type.getName(), someClass.getName());
		}
		defaultFormatters.put(type, someClass);
		logger.info("registered formatter {} for type {}", someClass.getName(),
				type.getName());

	}

	public boolean hasFormatter(String formatterName) {
		return namedFormatters.containsKey(formatterName);
	}

	@Override
	public Formatter createFormatter(String name, List<String> args)
			throws UnknownFormatterException, FormatterInstantiationException {

		final Class<? extends Formatter> formatterClass = namedFormatters
				.get(name);
		if (formatterClass == null) {
			throw new UnknownFormatterException(name);
		}
		return createFormatter(formatterClass, args);
	}

	static Formatter createFormatter(Class<? extends Formatter> formatterClass,
			List<String> args) {
		try {
			String springExpression = createSpringExpression(formatterClass,
					args);
			ExpressionParser parser = new SpelExpressionParser();
			Expression exp = parser.parseExpression(springExpression);
			return exp.getValue(Formatter.class);
		} catch (Exception e) {
			throw new FormatterInstantiationException(
					"Can not create new Instance of "
							+ formatterClass.getName() + "with args "
							+ args.toString(), e);
		}
	}

	@Override
	public Formatter createDefaultFormatter(Class<?> type)
			throws FormatterInstantiationException {
		Formatter formatter = new ToStringFormatter();
		Class<? extends Formatter> formatterClass = defaultFormatters.get(type);
		if (formatterClass != null) {
			try {
				formatter = formatterClass.newInstance();
			} catch (Exception e) {
				throw new FormatterInstantiationException(
						"Error on creating instance of "
								+ formatterClass.getName(), e);
			}
		}
		return formatter;

	}

	private static String createSpringExpression(
			Class<? extends Formatter> formatterClass, List<String> args) {
		StringBuilder springExpression = new StringBuilder("new ");
		springExpression.append(formatterClass.getCanonicalName());
		springExpression.append("(");
		appendArgs(springExpression, args);
		springExpression.append(")");
		return springExpression.toString();
	}

	private static void appendArgs(StringBuilder springExpression,
			List<String> args) {
		boolean addSeparator = false;
		for (String arg : args) {
			if (addSeparator) {
				springExpression.append(", ");
			}
			springExpression.append("'");
			springExpression.append(arg);
			springExpression.append("'");
			addSeparator = true;
		}
	}
}
