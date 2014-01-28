package ch.born.wte.impl.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterName;

@FormatterName("customDateTime")
public class CustomDateTimeFormatter implements Formatter {

	private final String pattern;
	private DateFormat format;

	public CustomDateTimeFormatter(String aPattern) {
		pattern = aPattern;
		format = new SimpleDateFormat(pattern, Locale.getDefault());
	}

	@Override
	public void setLocale(Locale locale) {
		format = new SimpleDateFormat(pattern, locale);
	}

	@Override
	public String format(Object object) throws ClassCastException {
		if (object != null) {
			return format.format(object);
		} else {
			return null;
		}
	}
}
