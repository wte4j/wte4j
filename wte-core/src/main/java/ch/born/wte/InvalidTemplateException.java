/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
