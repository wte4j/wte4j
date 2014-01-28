package ch.born.wte.impl.format;

import ch.born.wte.impl.Simple;
import ch.born.wte.impl.format.ToStringFormatter;

public class EnumArgFormatter extends ToStringFormatter {
	Simple value;

	public EnumArgFormatter(Simple value) {
		this.value = value;
	}
}