package ch.born.wte;

import java.util.Map;

/**
 * Exception to be thrown if the WTE can't handle any of the expressions of a template. 
 * A list of all the erroneous expressions is maintained. 
 */
public class InvalidTemplateException extends WteException {

	private final Map<String, ExpressionError> expressionErrors;

	public InvalidTemplateException(Map<String, ExpressionError> errors) {
		super(createMessage(errors));
		this.expressionErrors = errors;
	}

	private static String createMessage(Map<String, ExpressionError> errors) {
		StringBuilder errorText = new StringBuilder("Invalid template: ");
		int count = 0;
		for (Map.Entry<String, ExpressionError> entry : errors.entrySet()) {
			errorText.append(entry.getKey());
			errorText.append(": ");
			errorText.append(entry.getValue());
			count++;
			if (count < errors.size()) {
				errorText.append(", ");
			}
		}
		return errorText.toString();
	}

	public Map<String, ExpressionError> getErrors() {
		return expressionErrors;
	}
}
