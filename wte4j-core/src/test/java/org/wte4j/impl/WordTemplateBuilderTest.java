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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.wte4j.Formatter;
import org.wte4j.FormatterFactory;
import org.wte4j.FormatterInstantiationException;
import org.wte4j.Template;
import org.wte4j.TemplateBuildException;
import org.wte4j.UnknownFormatterException;
import org.wte4j.User;
import org.wte4j.WteDataModel;
import org.wte4j.WteModelService;
import org.wte4j.impl.format.ToStringFormatter;

public class WordTemplateBuilderTest {
	private Map<String, Class<?>> modelMap = null;
	private List<String> requiredProperties = null;

	@Before
	public void initVars() {
		modelMap = new HashMap<String, Class<?>>();
		modelMap.put("value", String.class);

		requiredProperties = new ArrayList<String>();
	}

	@Test
	public void testNormal() {
		TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
		WordTemplateBuilder<String> wtb = new WordTemplateBuilder<String>(contextFactory, new SimpleValueModelService(),
				String.class);
		wtb.setAuthor(new User("test1", "test2"));
		Template<String> wt = wtb.build();
		assertEquals(wt.getClass(), WordTemplate.class);
		assertNotNull(wt);
	}

	@Test
	public void buildWithFile() throws URISyntaxException, IOException {
		URL fileUrl = ClassLoader.getSystemResource("org/wte4j/basic-values-template.docx");
		Path file = Paths.get(fileUrl.toURI());

		TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
		WordTemplateBuilder<String> wtb = new WordTemplateBuilder<String>(contextFactory, new SimpleValueModelService(),
				String.class);
		Template<String> template = wtb.setAuthor(new User("test1", "test2"))
				.setTemplateFile(file)
				.build();

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		template.write(bytesOut);
		assertTrue(Arrays.equals(Files.readAllBytes(file), bytesOut.toByteArray()));

	}

	@Test(expected = TemplateBuildException.class)
	public void testParametersMissing() {
		new WordTemplateBuilder<String>(null, null, null);
		fail("Exception expected");
	}

	@Test(expected = TemplateBuildException.class)
	public void testRequiredPropertiesMissing() {
		requiredProperties.add("test");
		TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
		WordTemplateBuilder<String> wtb = new WordTemplateBuilder<String>(contextFactory, new SimpleValueModelService(),
				String.class);
		wtb.build();
		fail("Exception expected");
	}

	@Test
	public void createBasicTemplate() throws Exception {

		Map<String, Class<?>> elements = new HashMap<String, Class<?>>();
		elements.put("string", String.class);

		WteModelService service = mock(WteModelService.class);
		Map<String, String> properties = anyMapOf(String.class, String.class);
		Class<?> inputType = any(Class.class);
		when(service.listModelElements(inputType, properties)).thenReturn(
				elements);

		TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
		WordTemplateBuilder<String> builder = new WordTemplateBuilder<String>(
				contextFactory, service, String.class);

		byte[] templateContent = builder.createBasicTemplate();
		File templateFile = File.createTempFile("generated", "docx");
		try {
			FileUtils.writeByteArrayToFile(templateFile, templateContent);
			ZipFile zipFile = new ZipFile(templateFile);
			ZipEntry entry = zipFile.getEntry("word/document.xml");
			InputStream documentIn = zipFile.getInputStream(entry);
			String doucmentXml = IOUtils.toString(documentIn);
			doucmentXml = doucmentXml.replaceAll("\\n\\s+", "");
			String expected = "<w:sdt>"
					+ "<w:sdtPr>"//
					+ /*  */"<w:tag w:val=\"string\"/>"//
					+ /*  */"<w:text/>"//
					+ /*  */"<w:showingPlcHdr/>"//
					+ /*  */"<w15:appearance w15:val=\"tags\"/>"//
					+ /*  */"<w:alias w:val=\"string\"/>"//
					+ "</w:sdtPr>"//
					+ "<w:sdtContent>"//
					+ /*  */"<w:p><w:r>"
					+ /*       */"<w:rPr><w:rStyle w:val=\"PlaceholderText\"/></w:rPr>"
					+ /*       */"<w:t>string</w:t>"//
					+ /*  */"</w:r></w:p>" //
					+ "</w:sdtContent>"//
					+ "</w:sdt>";
			assertTrue(doucmentXml.contains(expected));
		} finally {
			templateFile.delete();
		}

	}

	private class SimpleValueModelService implements WteModelService {
		@Override
		public Map<String, Class<?>> listModelElements(Class<?> inputClass,
				Map<String, String> properties) {
			return modelMap;
		}

		@Override
		public List<String> listRequiredModelProperties() {
			return requiredProperties;
		}

		@Override
		public WteDataModel createModel(Template<?> template, final Object input) {
			return new WteDataModel() {
				@Override
				public Object getValue(String key)
						throws IllegalArgumentException {
					if (modelMap.containsKey(key)) {
						return modelMap.get(key);
					} else {
						throw new IllegalArgumentException();
					}
				}
			};
		}
	}

	private class SimpleFormatterFactory implements FormatterFactory {
		private Formatter formatter = new ToStringFormatter();

		@Override
		public Formatter createFormatter(String name, List<String> args)
				throws UnknownFormatterException,
				FormatterInstantiationException {
			if (name.equals("string")) {
				return formatter;
			}
			throw new UnknownFormatterException(name);
		}

		@Override
		public Formatter createDefaultFormatter(Class<?> type)
				throws FormatterInstantiationException {
			return formatter;
		}
	}
}
