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

import java.text.NumberFormat;
import java.util.Locale;

import org.wte4j.Formatter;
import org.wte4j.FormatterName;

@FormatterName("number")
public class NumberFormatter implements Formatter {

	private NumberFormat format;

	public NumberFormatter() {
		format = NumberFormat.getNumberInstance(new Locale("de", "CH"));
	}

	public NumberFormatter(int fractionDigits) {
		this();
		format.setGroupingUsed(false);
		format.setMinimumFractionDigits(fractionDigits);
		format.setMaximumFractionDigits(fractionDigits);
	}

	public NumberFormatter(int fractionDigits, boolean groupThousends) {
		this();
		format.setGroupingUsed(groupThousends);
		format.setMinimumFractionDigits(fractionDigits);
		format.setMaximumFractionDigits(fractionDigits);
	}

	@Override
	public void setLocale(Locale locale) {
		// we do not suport different locales when formatatting nummbers
	}

	@Override
	public String format(Object object) throws IllegalArgumentException {
		if (!(object instanceof Number)) {
			throw new IllegalArgumentException("type " + object.getClass()
					+ " is not suportet");
		}
		return format.format(object);
	}

}
