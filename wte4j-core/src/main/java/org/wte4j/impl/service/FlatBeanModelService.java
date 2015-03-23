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
package org.wte4j.impl.service;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.wte4j.Template;
import org.wte4j.WteDataModel;
import org.wte4j.WteModelService;

public class FlatBeanModelService implements WteModelService {

	@Override
	public Map<String, Class<?>> listModelElements(Class<?> inputClass,
			Map<String, String> properties) {
		Map<String, Class<?>> elements = new HashMap<String, Class<?>>();

		for (PropertyDescriptor descriptor : PropertyUtils
				.getPropertyDescriptors(inputClass)) {
			elements.put(descriptor.getName(), descriptor.getPropertyType());
		}

		return elements;
	}

	@Override
	public List<String> listRequiredModelProperties() {
		return Collections.emptyList();
	}

	@Override
	public WteDataModel createModel(Template<?> template, Object input) {
		return new BeanModel(input);
	}

	private static class BeanModel implements WteDataModel {

		private Object input;

		public BeanModel(Object input) {
			this.input = input;
		}

		@Override
		public Object getValue(String key) throws IllegalArgumentException {
			try {
				return PropertyUtils.getProperty(input, key);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}

	}

}
