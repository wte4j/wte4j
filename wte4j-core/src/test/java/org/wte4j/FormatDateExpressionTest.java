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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
 * Ueberprueft, dass die Formatierungen von Datum und Zeit via
 * Formatierungsanweisung funktioniert.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FormatDateExpressionTest.Config.class })
public class FormatDateExpressionTest {
	private static final TimeZone STARTUP_TIMEZONE = TimeZone.getDefault();
	private static final TimeZone MEZ = TimeZone.getTimeZone("CET");
	private static final Locale LOCALE = Locale.GERMAN;

	@Autowired
	private TemplateContextFactory contextFactory;

	@BeforeClass
	public static void setMEZAsDefaultTimeZone() {
		TimeZone.setDefault(MEZ);
	}

	@AfterClass
	public static void resetDefaulTimeZone() {
		TimeZone.setDefault(STARTUP_TIMEZONE);
	}

	@Test()
	public void formateDateShortTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15);
		testFormat(calendar, "format:date(short) value", "15.02.1977");
	}

	@Test()
	public void formateDateMediumTest() {
		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15);
		testFormat(calendar, "format:date(medium) value", "15. Feb. 1977");
	}

	@Test()
	public void formateDateLongTest() {
		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15);
		testFormat(calendar, "format:date(long) value", "15. Februar 1977");
	}

	@Test()
	public void formateTimeShortTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.clear();
		calendar.set(1977, 01, 15, 22, 33, 44);

		testFormat(calendar, "format:time(short) value", "22:33");
	}

	@Test()
	public void formateTimeMediumTest() {
		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);

		testFormat(calendar, "format:time(medium) value", "22:33:44");
	}

	@Test()
	public void formateTimeLongTest() {
		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);
		calendar.set(Calendar.MILLISECOND, 123);

		testFormat(calendar, "format:time(long) value", "22:33:44.123 MEZ");
	}

	@Test()
	public void formateDateTimeShortTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.clear();
		calendar.set(1977, 01, 15, 22, 33, 44);

		testFormat(calendar, "format:dateTime(short, short) value", "15.02.1977 22:33");
	}

	@Test()
	public void formateDateTimeMediumTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);
		testFormat(calendar, "format:dateTime(medium, medium) value", "15. Feb. 1977 22:33:44");
	}

	@Test()
	public void formateDateTimeLongTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);
		calendar.set(Calendar.MILLISECOND, 123);

		testFormat(calendar, "format:dateTime(long, long) value", "15. Februar 1977 22:33:44.123 MEZ");
	}

	@Test()
	public void formateDateTimeDifferentFormatTest() {

		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);

		testFormat(calendar, "format:dateTime(short, medium) value", "15.02.1977 22:33:44");
	}

	@Test()
	public void formateDateTimeWithPattern() {
		Calendar calendar = Calendar.getInstance(LOCALE);
		calendar.set(1977, 01, 15, 22, 33, 44);

		testFormat(calendar, "format:customDateTime(yyyy) value", "1977");
	}

	void testFormat(Calendar input, String expression, String expected) {
		Template<Date> template = mock(Template.class);
		when(template.getLanguage()).thenReturn(LOCALE.getLanguage());
		when(template.getInputType()).thenReturn((Class) Date.class);

		TemplateContext<Date> context = contextFactory.createTemplateContext(template);
		context.bind(input.getTime());

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
			return Collections.<String, Class<?>> singletonMap("value", inputClass);
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
