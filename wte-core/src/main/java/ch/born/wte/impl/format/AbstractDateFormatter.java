package ch.born.wte.impl.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ch.born.wte.Formatter;

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
