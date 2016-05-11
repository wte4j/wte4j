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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.wte4j.Formatter;

abstract class AbstractDateFormatter implements Formatter {

	private final FormatStyle style;
	private Locale locale = Locale.getDefault();
	private DateFormat format;

	public AbstractDateFormatter(FormatStyle style) {
		this.style = style;
		locale = Locale.getDefault();
	}

	protected abstract String getPattern();

	public FormatStyle getFormatStyle() {
		return style;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public void setLocale(Locale aLocale) {
		locale = aLocale;
		format = null;
	}

	private DateFormat getDateFormat() {
		if (format == null) {
			format = new SimpleDateFormat(getPattern(), locale);
		}
		return format;
	}

	@Override
	public String format(Object object) throws IllegalArgumentException {
		return getDateFormat().format(object);
	}

}
