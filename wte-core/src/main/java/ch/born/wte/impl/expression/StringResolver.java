package ch.born.wte.impl.expression;

import ch.born.wte.Formatter;
import ch.born.wte.WteDataModel;

public class StringResolver implements ValueResolver<String> {
	private final String contentKey;
	private final Formatter formatter;

	public StringResolver(String contentKey, Formatter formatter) {
		this.contentKey = contentKey;
		this.formatter = formatter;
	}

	@Override
	public String resolve(WteDataModel model) throws ClassCastException {
		Object value = model.getValue(contentKey);
		if (value != null) {
			return formatter.format(value);
		} else {
			return null;
		}
	}
}
