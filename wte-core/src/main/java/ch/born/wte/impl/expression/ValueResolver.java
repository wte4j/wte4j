package ch.born.wte.impl.expression;

import ch.born.wte.WteDataModel;

public interface ValueResolver<E> {
	E resolve(WteDataModel model) throws ClassCastException;
}
