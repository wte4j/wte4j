package ch.born.wte.impl.format;

import ch.born.wte.WteDataModel;

class SingleValueDataModel implements WteDataModel {
	private Object value;

	public SingleValueDataModel(Object value) {
		super();
		this.value = value;
	}

	@Override
	public Object getValue(String key) throws IllegalArgumentException {
		return value;
	}

}
