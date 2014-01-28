package ch.born.wte.impl.format;

import java.util.Locale;

import ch.born.wte.Formatter;

public class ToStringFormatter implements Formatter {

	@Override
	public void setLocale(Locale locale) {

	}

	@Override
	public String format(Object object) throws IllegalArgumentException {
		return object.toString();
	}

}
