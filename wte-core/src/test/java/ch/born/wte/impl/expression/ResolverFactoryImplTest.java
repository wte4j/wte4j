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
package ch.born.wte.impl.expression;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import ch.born.wte.ExpressionError;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.UnknownFormatterException;
import ch.born.wte.WteDataModel;
import ch.born.wte.impl.format.ToStringFormatter;
import ch.born.wte.impl.word.Docx4JWordTemplate;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class ResolverFactoryImplTest {

	private static final Map<String, Class<?>> ELEMENTS = Collections
			.<String, Class<?>> singletonMap("key", String.class);

	@Test
	public void parseValidExpression() {
		FormatterFactory formatterFactory = mock(FormatterFactory.class);
		when(
				formatterFactory.createFormatter("formatter",
						Collections.<String> singletonList("arg"))).thenReturn(
				new ToStringFormatter());

		ResolverFactoryImpl templateContextImpl = new ResolverFactoryImpl(
				Locale.getDefault(), formatterFactory, ELEMENTS);

		String expression = "format:formatter(arg) key";
		ValueResolver<String> resolver = templateContextImpl
				.createStringResolver(expression);

		WteDataModel model = mock(WteDataModel.class);
		String expectedValue = "testValue";
		when(model.getValue("key")).thenReturn(expectedValue);
		assertEquals(expectedValue, resolver.resolve(model));
	}

	@Test
	public void parseUnknownFormatterExpression() {

		FormatterFactory formatterFactory = mock(FormatterFactory.class);
		when(formatterFactory.createFormatter(anyString(), anyList()))
				.thenThrow(new UnknownFormatterException(""));

		ResolverFactoryImpl templateContextImpl = new ResolverFactoryImpl(
				Locale.getDefault(), formatterFactory, ELEMENTS);

		String expression = "format:formatter1 key";

		try {
			templateContextImpl.createStringResolver(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {
			assertEquals(ExpressionError.UNKNOWN_FORMATTER, e.getError());
		}
	}

	@Test
	public void parseIllegalFormatterExpression() {
		Docx4JWordTemplate template = new Docx4JWordTemplate();

		FormatterFactory formatterFactory = mock(FormatterFactory.class);
		when(formatterFactory.createFormatter(anyString(), anyList()))
				.thenThrow(new FormatterInstantiationException(""));

		ResolverFactoryImpl templateContextImpl = new ResolverFactoryImpl(
				Locale.getDefault(), formatterFactory, ELEMENTS);

		String expression = "format:formatter1 key";
		try {
			templateContextImpl.createStringResolver(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {

			assertEquals(ExpressionError.ILLEGALFORMATTER_DEFINITION,
					e.getError());
		}
	}

	@Test
	public void unknownContentKey() {
		FormatterFactory formatterFactory = mock(FormatterFactory.class);
		ResolverFactoryImpl templateContextImpl = new ResolverFactoryImpl(
				Locale.getDefault(), formatterFactory, ELEMENTS);
		String expression = "format:formatter1 gugus";
		try {
			templateContextImpl.createStringResolver(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {

			assertEquals(ExpressionError.ILLEGAL_CONTENT_KEY, e.getError());
		}
	}
}
