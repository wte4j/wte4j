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
package org.wte4j;

import java.util.List;

public interface FormatterFactory {

	/**
	 * Creates a new formatter instance
	 * 
	 * @param name
	 *            - the name of a formatter
	 * @param args
	 *            - a list of args to construct the formatter
	 * @return a new formatter instance
	 * @throws UnknownFormatterException
	 *             - if the formatter with the given name does not exists
	 * @throws InstantiationException
	 *             -if for the given arguments the formatter can not be build
	 */

	Formatter createFormatter(String name, List<String> args) throws UnknownFormatterException, FormatterInstantiationException;

	/**
	 * Creates a new formatter instance for a given type.
	 * 
	 * @throws FormatterInstantiationException
	 *             if a new Instance can not be created.
	 */
	Formatter createDefaultFormatter(Class<?> type) throws FormatterInstantiationException;

}