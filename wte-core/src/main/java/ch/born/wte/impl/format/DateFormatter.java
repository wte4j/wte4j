package ch.born.wte.impl.format;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.FormatterName;

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
