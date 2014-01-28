package ch.born.wte.impl.expression;

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
