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
import java.util.Map;

/**
 * The engine requires a WteModelService to generate documents and create new
 * templates with the supported tags for a given model configuration.
 */
public interface WteModelService {

	/**
	 * Returns a list of dynamic content elements available for the given
	 * properties and input type.
	 */
	Map<String, Class<?>> listModelElements(Class<?> inputClass,
			Map<String, String> properties);

	/**
	 * Lists the property names required to create models (
	 * {@link #createModel(Template, Object)})and list the the model elements(
	 * {@link #listModelElements(Class, Map)}).
	 */
	List<String> listRequiredModelProperties();

	/**
	 * Wraps the given input in a {@link WteDataModel}. This model is used to
	 * get the content to be filled in the template.
	 */
	WteDataModel createModel(Template<?> template, Object input);
}
