package ch.born.wte.impl.format;

public enum FormatStyle {

	SHORT("dd.MM.yyyy", "HH:mm"), //
	MEDIUM("dd. MMM. yyyy", "HH:mm:ss"), //
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
