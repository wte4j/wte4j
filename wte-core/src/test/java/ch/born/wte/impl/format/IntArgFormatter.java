package ch.born.wte.impl.format;

import ch.born.wte.impl.format.ToStringFormatter;

public class IntArgFormatter extends ToStringFormatter {
	int value;

	public IntArgFormatter(int value) {
		super();
		this.value = value;
	}
}