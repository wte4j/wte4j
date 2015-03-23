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
package org.wte4j.impl;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class TestDataModelTest {

	@Test
	public void getValueTest() {
		Map<String, Class<?>> elements = createElements();
		TestDataModel model = new TestDataModel(elements);
		Iterator<Map.Entry<String, Class<?>>> iterator = elements.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Class<?>> entry = iterator.next();
			Object value = model.getValue(entry.getKey());
			if (value.getClass().equals(entry.getValue())) {
				iterator.remove();
			}
		}
		assertTrue(
				"Fehler beim erstellen folgender Testdaten:"
						+ elements.toString(), elements.isEmpty());

	}

	private static Map<String, Class<?>> createElements() {
		Map<String, Class<?>> elements = new HashMap<String, Class<?>>();

		elements.put("string", String.class);

		elements.put("boolean", Boolean.class);

		elements.put("int", Integer.class);
		elements.put("long", Long.class);
		elements.put("bigInt", BigInteger.class);
		elements.put("float", Float.class);
		elements.put("double", Double.class);
		elements.put("bigDez", BigDecimal.class);

		elements.put("sqlDate", java.sql.Date.class);
		elements.put("sqlTime", java.sql.Time.class);
		elements.put("sqlTimestamp", java.sql.Timestamp.class);
		elements.put("date", Date.class);

		return elements;

	}

}
