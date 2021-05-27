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
package org.wte4j;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wte4j.impl.TemplateContext;
import org.wte4j.impl.TemplateContextFactory;
import org.wte4j.impl.context.TemplateContextFactoryImpl;
import org.wte4j.impl.format.FormatterRegistry;

/**
 * Ueberprueft, dass die Formatierungen von Zahlen via Formatierungsanweisung in
 * den Tags funktioniert.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FormatNumberExpressionTest.Config.class })
public class FormatNumberExpressionTest {

	@Autowired
	private TemplateContextFactory contextFactory;

	@Test()
	public void formateWithMoreDigitsTest() {
		testFormat(1000.5555, "format:number(3) value", "1000.556");
	}

	@Test()
	public void formateWithLessDigitsTest() {
		testFormat(1000.55, "format:number(3) value", "1000.550");
	}

	@Test()
	public void formateNoDigitsTest() {
		testFormat(1000.5555, "format:number(0) value", "1001");
	}

	@Test()
	public void formateGroupingTest() {
		testFormat(10000, "format:number(0, true) value", "10’000");
	}

	@Test()
	public void formateNoGroupingTest() {
		testFormat(10000, "format:number(0, false) value", "10000");
	}

	void testFormat(Number input, String expression, String expected) {
		Template<Number> template = mock(Template.class);
		when(template.getLanguage()).thenReturn("de");
		when(template.getInputType()).thenReturn((Class) Number.class);

		TemplateContext<Number> context = contextFactory.createTemplateContext(template);
		context.bind(input);

		String formated = context.resolveValue(expression);
		assertEquals(expected, formated);
	}

	@Configuration
	public static class Config {
		@Bean
		public FormatterFactory formatterRegistry() {
			return new FormatterRegistry();
		}

		@Bean
		@Qualifier("wteModelService")
		public WteModelService wteModelService() {
			return new SimpleModelService();
		}

		@Bean
		public TemplateContextFactory templateContextFactory() {
			return new TemplateContextFactoryImpl();
		}
	}

	public static class SimpleModelService implements WteModelService {

		@Override
		public Map<String, Class<?>> listModelElements(Class<?> inputClass, Map<String, String> properties) {
			return Collections.<String, Class<?>> singletonMap("value", Number.class);
		}

		@Override
		public List<String> listRequiredModelProperties() {
			return Collections.emptyList();
		}

		@Override
		public WteDataModel createModel(Template<?> template, final Object input) {
			return new WteDataModel() {

				@Override
				public Object getValue(String key) {
					if (key.equals("value")) {
						return input;
					}
					return null;
				}
			};
		}
	}

}
