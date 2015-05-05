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
package org.wte4j.impl;

import org.wte4j.ExpressionError;
import org.wte4j.WteDataModel;

public interface TemplateContext<E> {
	/**
	 * Binds the context to a {@link WteDataModel}
	 * 
	 * @param data
	 *            -the model to be used
	 */
	void bind(E data);

	/**
	 * Evaluates Expression and returns the value
	 * 
	 * @param expression
	 * @return the evaluated value to be placed as content in a template
	 * @throws InvalidExpressionException
	 *             if the expression is not valid
	 * @throws IllegalStateException
	 *             if the context is not boud to a {@link WteDataModel} (see
	 *             {@link # bind(WteDataModel)}
	 */
	String resolveValue(String expression) throws InvalidExpressionException, IllegalStateException;

	/**
	 * validates a expression against the model definition of this template
	 * 
	 * @param expression
	 * @return
	 */
	ExpressionError validate(String expression);
}
