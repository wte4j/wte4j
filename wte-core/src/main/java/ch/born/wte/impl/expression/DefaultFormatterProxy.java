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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;

public class DefaultFormatterProxy implements Formatter {

	private FormatterFactory registry;
	private Map<Class<?>, Formatter> cache;
	private Locale locale;

	public DefaultFormatterProxy(FormatterFactory registry) {
		this.registry = registry;
		cache = new HashMap<Class<?>, Formatter>();
		locale = Locale.getDefault();
	}

	@Override
	public void setLocale(Locale aLocale) {
		locale = aLocale;
		for (Formatter formatter : cache.values()) {
			formatter.setLocale(aLocale);
		}
	}

	@Override
	public String format(Object object) throws ClassCastException {
		Formatter formatter = getFormatter(object.getClass());
		return formatter.format(object);
	}

	Formatter getFormatter(Class<?> type) {
		Formatter formatter = cache.get(type);
		if (formatter == null) {
			formatter = registry.createDefaultFormatter(type);
			formatter.setLocale(locale);
			cache.put(type, formatter);
		}
		return formatter;
	}
}
