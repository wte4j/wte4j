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
 * Interface for querying Templates in the repository.
 */
public interface TemplateQuery {

	TemplateQuery documentName(String name);

	TemplateQuery language(String language);

	TemplateQuery inputType(Class<?> clazz);

	TemplateQuery editor(String userId);

	TemplateQuery isLocked(boolean value);

	TemplateQuery isLockedBy(String userId);

	TemplateQuery hasProperties(Map<String, String> properties);

	List<Template<Object>> list();
}
