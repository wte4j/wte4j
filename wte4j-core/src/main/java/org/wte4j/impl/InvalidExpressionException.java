/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j.impl;

import org.wte4j.ExpressionError;
import org.wte4j.WteException;

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
