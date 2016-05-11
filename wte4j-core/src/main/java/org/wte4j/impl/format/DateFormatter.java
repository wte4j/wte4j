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

import org.wte4j.DefaultFormatter;
import org.wte4j.FormatterName;

@FormatterName("date")
@DefaultFormatter({ java.util.Date.class, java.sql.Date.class })
public class DateFormatter extends AbstractDateFormatter {

	public DateFormatter() {
		this(FormatStyle.SHORT);
	}

	public DateFormatter(FormatStyle style) {
		super(style);
	}

	public DateFormatter(String style) {
		this(FormatStyle.fromString(style));
	}

	@Override
	protected String getPattern() {
		return getFormatStyle().getDatePattern();
	}

}
