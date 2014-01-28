package ch.born.wte.impl.expression;

import ch.born.wte.ExpressionError;
import ch.born.wte.WteException;

public class InvalidExpressionException extends WteException {

	private final ExpressionError error;

	public InvalidExpressionException(ExpressionError expressionError) {
		super(expressionError.toString());

		error = expressionError;
	}

	public InvalidExpressionException(ExpressionError expressionError,
			Throwable e) {
		super(expressionError.toString(), e);
		error = expressionError;
	}

	public ExpressionError getError() {
		return error;
	}

}
