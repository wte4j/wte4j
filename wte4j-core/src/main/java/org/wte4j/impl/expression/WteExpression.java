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
package org.wte4j.impl.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WteExpression {

	private static final Pattern FORMAT_PATTERN = Pattern.compile("format:"
			+ "(\\w+)" + "\\s*");
	private static final Pattern ARG_PATTERN = Pattern
			.compile("\\G\\((.*)\\)\\s*");

	private static final int FORMATTER_NAME = 1;
	private static final int FORMATTER_CUNSTRUCTOR_ARGS = 1;

	private String expressionString = "";

	private String contentKey;
	private String formattername;
	private List<String> formatterArgs;

	protected void parse(String aInput) {
		expressionString = aInput;
		int cursor = 0;
		cursor = parseFormatter(cursor);
		contentKey = expressionString.substring(cursor).trim();
	}

	protected int parseFormatter(int start) {
		formattername = null;
		formatterArgs = new ArrayList<String>();
		int cursor = parseFormatterName(start);
		if (cursor > start) {
			cursor = parseFormatterArgs(cursor);
		}
		return cursor;
	}

	protected int parseFormatterName(int start) {
		int cursor = start;
		Matcher matcher = FORMAT_PATTERN.matcher(expressionString);
		if (matcher.find(cursor)) {
			cursor = matcher.end();
			formattername = matcher.group(FORMATTER_NAME);
		}
		return cursor;
	}

	protected int parseFormatterArgs(int start) {
		int cursor = start;
		Matcher matcher = ARG_PATTERN.matcher(expressionString);
		if (matcher.find(cursor)) {
			cursor = matcher.end();
			StringTokenizer tokenizer = new StringTokenizer(
					matcher.group(FORMATTER_CUNSTRUCTOR_ARGS), ",");
			while (tokenizer.hasMoreTokens()) {
				formatterArgs.add(tokenizer.nextToken().trim());
			}
		}
		return cursor;
	}

	public void setExpressionString(String value) {
		expressionString = value;
		parse(value);
	}

	public String getExpressionString() {
		return expressionString;
	}

	public String getContentKey() {
		return contentKey;
	}

	public String getFormattername() {
		return formattername;
	}

	public List<String> getFormatterArgs() {
		return formatterArgs;
	}

	@Override
	public String toString() {
		return "WteExpression [expressionString=" + expressionString + "]";
	}

}
