package ch.born.wte.impl.format;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.FormatterName;

@FormatterName("dateTime")
@DefaultFormatter({ java.sql.Timestamp.class })
public class DateTimeFormater extends AbstractDateFormatter {

	private FormatStyle timeStyle;

	public DateTimeFormater() {
		this(FormatStyle.SHORT, FormatStyle.SHORT);
	}

	public DateTimeFormater(FormatStyle dateStyle, FormatStyle timeStyle) {
		super(dateStyle);
		this.timeStyle = timeStyle;
	}

	public DateTimeFormater(String dateStyle, String timeStyle) {
		this(FormatStyle.fromString(dateStyle), FormatStyle
				.fromString(timeStyle));
	}

	public FormatStyle getDateStyle() {
		return getFormatStyle();
	}

	public FormatStyle getTimeStyle() {
		return timeStyle;
	}

	@Override
	protected String getPattern() {
		return getDateStyle().getDatePattern() + " "
				+ getTimeStyle().getTimePattern();
	}

}
