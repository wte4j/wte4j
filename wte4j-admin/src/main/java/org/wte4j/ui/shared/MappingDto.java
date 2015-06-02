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
package org.wte4j.ui.shared;

import java.io.Serializable;

public class MappingDto implements Serializable {
	private String conentControlKey;
	private String modelKey;
	private String formatterDefinition;

	public String getConentControlKey() {
		return conentControlKey;
	}

	public void setContentControlKey(String conentControlId) {
		this.conentControlKey = conentControlId;
	}

	public String getModelKey() {
		return modelKey;
	}

	public void setModelKey(String modelId) {
		this.modelKey = modelId;
	}

	public String getFormatterDefinition() {
		return formatterDefinition;
	}

	public void setFormatterDefinition(String formatterExpression) {
		this.formatterDefinition = formatterExpression;
	}
}
