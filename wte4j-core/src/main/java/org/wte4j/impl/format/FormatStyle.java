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
package org.wte4j.impl.format;

public enum FormatStyle {

	SHORT("dd.MM.yyyy", "HH:mm"), //
	MEDIUM("dd. MMM yyyy", "HH:mm:ss"), //
	LONG("dd. MMMM yyyy", "HH:mm:ss.SSS z");

	private final String datePattern;
	private final String timePattern;

	FormatStyle(String aDatePattern, String aTimePattern) {
		datePattern = aDatePattern;
		timePattern = aTimePattern;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public String getTimePattern() {
		return timePattern;
	}

	public static FormatStyle fromString(String styleAsString) {
		String upperCase = styleAsString.toUpperCase();
		return FormatStyle.valueOf(upperCase);
	}

}
