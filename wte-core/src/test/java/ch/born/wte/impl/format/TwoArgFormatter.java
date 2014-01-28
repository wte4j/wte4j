package ch.born.wte.impl.format;

import ch.born.wte.impl.format.ToStringFormatter;

public class TwoArgFormatter extends ToStringFormatter {
	String arg1;
	String arg2;

	public TwoArgFormatter(String arg1, String arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
}