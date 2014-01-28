package ch.born.wte.impl.service;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import ch.born.wte.Template;
import ch.born.wte.WteDataModel;
import ch.born.wte.WteModelService;

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
