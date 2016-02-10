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

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MappingDetail {
	
	@Column(name = "model_key", nullable = true, length = 250)
	private String modelKey;
	
	@Column(name = "formatter_definition", nullable = true, length = 250)
	private String formatterDefinition;

	public String getModelKey() {
		return modelKey;
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}

	public String getFormatterDefinition() {
		return formatterDefinition;
	}

	public void setFormatterDefinition(String formatterDefinition) {
		this.formatterDefinition = formatterDefinition;
	}

	@Override
	@Generated("eclipse")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelKey == null) ? 0 : modelKey.hashCode());
		return result;
	}

	@Override
	@Generated("eclipse")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MappingDetail other = (MappingDetail) obj;
		if (modelKey == null) {
			if (other.modelKey != null)
				return false;
		} else if (!modelKey.equals(other.modelKey))
			return false;
		return true;
	}

}
