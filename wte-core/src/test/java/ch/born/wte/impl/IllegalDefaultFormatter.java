package ch.born.wte.impl;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.FormatterName;
import ch.born.wte.impl.format.ToStringFormatter;

@FormatterName("illegal")
@DefaultFormatter({ Integer.class })
public class IllegalDefaultFormatter extends ToStringFormatter {
	public IllegalDefaultFormatter(String arg) {
	}
}