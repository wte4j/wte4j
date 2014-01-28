package ch.born.wte.impl.format;

import java.text.NumberFormat;
import java.util.Locale;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterName;

@FormatterName("number")
public class NumberFormatter implements Formatter {

	private NumberFormat format;

	public NumberFormatter() {
		format = NumberFormat.getNumberInstance(new Locale("de", "CH"));
	}

	public NumberFormatter(int fractionDigits) {
		this();
		format.setGroupingUsed(false);
		format.setMinimumFractionDigits(fractionDigits);
		format.setMaximumFractionDigits(fractionDigits);
	}

	public NumberFormatter(int fractionDigits, boolean groupThousends) {
		this();
		format.setGroupingUsed(groupThousends);
		format.setMinimumFractionDigits(fractionDigits);
		format.setMaximumFractionDigits(fractionDigits);
	}

	@Override
	public void setLocale(Locale locale) {
		// we do not suport different locales when formatatting nummbers
	}

	@Override
	public String format(Object object) throws IllegalArgumentException {
		if (!(object instanceof Number)) {
			throw new IllegalArgumentException("type " + object.getClass()
					+ " is not suportet");
		}
		return format.format(object);
	}

}
