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
package org.wte4j.impl.context;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.wte4j.ExpressionError;
import org.wte4j.FormatterFactory;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.WteDataModel;
import org.wte4j.WteModelService;
import org.wte4j.impl.InvalidExpressionException;
import org.wte4j.impl.TemplateContext;
import org.wte4j.impl.expression.WteExpression;

public class TemplateContextImpl<E> implements TemplateContext<E> {

	private WteModelService modelService;
	private FormatterFactory formatterFactory;
	// Template specific fields
	private Template<E> template;
	private Locale locale;
	private Map<String, Class<?>> modelElements;
	private WteDataModel model;

	private WteExpression expression;
	private ValueFormatter valueFormatter;
	private String valueKey;

	public TemplateContextImpl(
			FormatterFactory formatterFactory, WteModelService modelService, Template<E> template) {
		super();
		this.modelService = modelService;
		this.formatterFactory = formatterFactory;
		this.template = template;
		expression = new WteExpression();
		initContext();
	}

	private void initContext() {
		locale = new Locale(template.getLanguage());
		valueFormatter = new ValueFormatter(formatterFactory, locale);
		modelElements = modelService.listModelElements(template.getInputType(), template.getProperties());
	}

	@Override
	public void bind(E data) {
		model = modelService.createModel(template, data);

	}

	@Override
	public ExpressionError validate(String expressionString) {
		try {
			parseAndValidate(expressionString);
		} catch (InvalidExpressionException e) {
			return e.getError();
		}
		return null;

	}

	@Override
	public String resolveValue(String expressionString) throws InvalidExpressionException, IllegalStateException {

		if (model == null) {
			throw new IllegalStateException("Context not bound to data");
		}
		parseAndValidate(expressionString);
		Object value = model.getValue(valueKey);
		if (value != null) {
			return valueFormatter.format(value, locale);
		}
		return StringUtils.EMPTY;
	}

	private void parseAndValidate(String expressionString) throws InvalidExpressionException {
		parseValueExpression(expressionString);
		if (!modelElements.containsKey(valueKey)) {
			throw new InvalidExpressionException(ExpressionError.ILLEGAL_CONTENT_KEY);
		}
		Class<?> valueType = modelElements.get(valueKey);
		valueFormatter.validate(valueType);

	}

	private void parseValueExpression(final String expressionString) {
		expression.setExpressionString(expressionString);
		valueKey = expression.getContentKey();
		valueFormatter.setFormatterName(expression.getFormattername());
		valueFormatter.setFormatterArgs(expression.getFormatterArgs());

		if (template.getContentMapping().containsKey(valueKey)) {
			MappingDetail mappingDetail = template.getContentMapping().get(valueKey);
			if (StringUtils.isNotEmpty(mappingDetail.getModelKey())) {
				valueKey = mappingDetail.getModelKey();
			}
			if (StringUtils.isNotEmpty(mappingDetail.getFormatterDefinition())) {
				expression.setExpressionString("format:" + mappingDetail.getFormatterDefinition().trim());
				valueFormatter.setFormatterName(expression.getFormattername());
				valueFormatter.setFormatterArgs(expression.getFormatterArgs());
				expression.setExpressionString(expressionString);
			}
		}

	}

}
