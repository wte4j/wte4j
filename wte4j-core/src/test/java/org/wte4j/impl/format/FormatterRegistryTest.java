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
package org.wte4j.impl.format;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.wte4j.Formatter;
import org.wte4j.impl.AnnotatedCustomFormatter;
import org.wte4j.impl.IllegalDefaultFormatter;
import org.wte4j.impl.Simple;

public class FormatterRegistryTest {

	@Test
	public void registerCostumFormatter() {
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(ToStringFormatter.class);
		assertEquals(
				ToStringFormatter.class,
				registry.createFormatter("ToStringFormatter",
						Collections.<String> emptyList()).getClass());
	}

	@Test
	public void registerAnnotatedFormatter() {
		final Class<AnnotatedCustomFormatter> formatterClass = AnnotatedCustomFormatter.class;
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(formatterClass);
		Formatter formatter = registry.createFormatter("custom",
				Collections.<String> emptyList());
		assertEquals(formatterClass, formatter.getClass());
		assertEquals(formatterClass,
				registry.createDefaultFormatter(Integer.class).getClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void registerIllegalDefaultFormatter() {
		final Class<IllegalDefaultFormatter> formatterClass = IllegalDefaultFormatter.class;
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(formatterClass);
	}

	@Test
	public void twoArgFormatter() {
		TwoArgFormatter formatter = (TwoArgFormatter) FormatterRegistry
				.createFormatter(TwoArgFormatter.class, Arrays.asList("a", "b"));
		assertEquals("a", formatter.arg1);
		assertEquals("b", formatter.arg2);
	}

	@Test
	public void enumArgFormatter() {
		EnumArgFormatter formatter = (EnumArgFormatter) FormatterRegistry
				.createFormatter(EnumArgFormatter.class,
						Arrays.asList("VALUE_1"));
		assertEquals(Simple.VALUE_1, formatter.value);
	}

	@Test
	public void intArgFormatter() {
		IntArgFormatter formatter = (IntArgFormatter) FormatterRegistry
				.createFormatter(IntArgFormatter.class, Arrays.asList("23"));
		assertEquals(23, formatter.value);
	}

}
