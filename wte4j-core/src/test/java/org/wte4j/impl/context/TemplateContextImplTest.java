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
package org.wte4j.impl.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.wte4j.ExpressionError;
import org.wte4j.Formatter;
import org.wte4j.FormatterFactory;
import org.wte4j.FormatterInstantiationException;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.UnknownFormatterException;
import org.wte4j.WteDataModel;
import org.wte4j.WteModelService;
import org.wte4j.impl.InvalidExpressionException;

@RunWith(MockitoJUnitRunner.class)
public class TemplateContextImplTest {
	private static final String KEY = "key";
	private static final Integer VALUE = 1;
	private static final String FORMATED_VALUE = "1.0";
	private static final Class INPUT_TYPE = String.class;
	private static final Map<String, Class<?>> ELEMENTS = Collections
			.<String, Class<?>> singletonMap(KEY, VALUE.getClass());

	@Mock
	FormatterFactory formatterFactory;

	@Mock
	WteModelService modelService;

	@Mock
	Template<String> template;

	@Before
	public void initMocks() {
		Formatter formatter = mock(Formatter.class);
		when(formatter.format(VALUE)).thenReturn(FORMATED_VALUE);
		when(formatterFactory.createFormatter("formatter",
				Collections.<String> singletonList("arg"))).thenReturn(formatter);

		when(template.getLanguage()).thenReturn("de");
		when(template.getInputType()).thenReturn(INPUT_TYPE);

		when(modelService.listModelElements(template.getInputType(), template.getProperties())).thenReturn(ELEMENTS);
		WteDataModel model = mock(WteDataModel.class);
		when(modelService.createModel(template, "")).thenReturn(model);

		when(model.getValue(KEY)).thenReturn(VALUE);
	}

	@Test
	public void resolveValidExpression() {

		TemplateContextImpl<String> templateContextImpl = new TemplateContextImpl<>(formatterFactory, modelService, template);
		templateContextImpl.bind("");
		String expression = "format:formatter(arg) key";
		String resolvedValue = templateContextImpl.resolveValue(expression);

		assertEquals(FORMATED_VALUE, resolvedValue);
	}

	@Test
	public void resolveValidExpressionWithMapping() {
		MappingDetail mappingDetail = new MappingDetail();
		mappingDetail.setModelKey("key");
		mappingDetail.setFormatterDefinition("formatter(arg)");
		Map<String, MappingDetail> contentMapping = new HashMap<String, MappingDetail>();
		contentMapping.put("contentKey", mappingDetail);

		when(template.getContentMapping()).thenReturn(contentMapping);

		TemplateContextImpl<String> templateContextImpl = new TemplateContextImpl<>(formatterFactory, modelService, template);
		templateContextImpl.bind("");
		String expression = "contentKey";
		String resolvedValue = templateContextImpl.resolveValue(expression);

		assertEquals(FORMATED_VALUE, resolvedValue);
	}

	@Test
	public void resolveUnknownFormatterExpression() {
		when(formatterFactory.createFormatter(anyString(), anyList())).thenThrow(new UnknownFormatterException(""));

		TemplateContextImpl<String> templateContextImpl = new TemplateContextImpl<>(formatterFactory, modelService, template);
		templateContextImpl.bind("");

		String expression = "format:formatter1 key";
		try {
			templateContextImpl.resolveValue(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {
			assertEquals(ExpressionError.UNKNOWN_FORMATTER, e.getError());
		}
	}

	@Test
	public void resolveIlegalFormatterExpression() {
		when(formatterFactory.createFormatter(anyString(), anyList()))
				.thenThrow(new FormatterInstantiationException(""));

		TemplateContextImpl<String> templateContextImpl = new TemplateContextImpl<>(formatterFactory, modelService, template);
		templateContextImpl.bind("");

		String expression = "format:formatter1 key";
		try {
			templateContextImpl.resolveValue(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {

			assertEquals(ExpressionError.ILLEGALFORMATTER_DEFINITION,
					e.getError());
		}
	}

	@Test
	public void resolveUnknownContentKey() {
		TemplateContextImpl<String> templateContextImpl = new TemplateContextImpl<>(formatterFactory, modelService, template);
		templateContextImpl.bind("");

		String expression = "format:formatter(arg) unkown_key";
		try {
			templateContextImpl.resolveValue(expression);
			fail("Exception expected");
		} catch (InvalidExpressionException e) {

			assertEquals(ExpressionError.ILLEGAL_CONTENT_KEY, e.getError());
		}
	}

}
