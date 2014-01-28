package ch.born.wte;

import java.util.Locale;

public interface Formatter {
	public void setLocale(Locale locale);

	public String format(Object object) throws ClassCastException;

}
