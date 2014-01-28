package ch.born.wte.impl.format;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.FormatterName;

@FormatterName("time")
@DefaultFormatter({ java.sql.Time.class })
public class TimeFormatter extends AbstractDateFormatter {

	public TimeFormatter() {
		this(FormatStyle.SHORT);
	}

	public TimeFormatter(FormatStyle style) {
		super(style);
	}

	public TimeFormatter(String style) {
		this(FormatStyle.fromString(style));
	}

	@Override
	protected String getPattern() {
		return getFormatStyle().getTimePattern();
	}

}
