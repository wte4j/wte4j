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
package org.wte4j;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.wte4j.impl.service.WteMapModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TemplateEngineTest.TemplateEngineConfiguration.class })
@DirtiesContext
public class TemplateEngineTest {

	@Autowired
	TemplateEngine wte;

	@Test
	public void createDocumentWithBasicDynamicContent() throws Exception {
		final String documentName = "basic_values";
		final String language = "de";
		final Path templateDocument = Paths.get(ClassLoader.getSystemResource("org/wte4j/basic-values-template.docx").toURI());

		TemplateBuilder<Integer> templateBuilder = wte.getTemplateBuilder(Integer.class);
		Template<Integer> template = templateBuilder
				.setDocumentName(documentName)
				.setLanguage(language)
				.setAuthor(new User("hw", "Hans Wurst"))
				.setTemplateFile(templateDocument)
				.build();

		TemplateRepository repository = wte.getTemplateRepository();
		template = repository.persist(template);

		Path file = wte.createDocument(documentName, language, 1);
		try (InputStream in = Files.newInputStream(file)) {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.load(in);
			StringWriter writer = new StringWriter();
			TextUtils.extractText(
					wordMLPackage.getMainDocumentPart().getContents(), writer);
			String contentAsText = writer.toString();
			assertTrue(!contentAsText.contains("toReplace"));
		} finally {
			Files.deleteIfExists(file);
		}
	}

	@Configuration
	@ComponentScan(basePackages = { "org.wte4j.impl" })
	@Import(EmbeddedDataBaseConfig.class)
	@EnableTransactionManagement
	public static class TemplateEngineConfiguration {

		@Bean()
		WteModelService wteModelService() {
			return new TestModelService();
		}
	}

	public static class TestModelService implements WteModelService {

		@Override
		public Map<String, Class<?>> listModelElements(Class<?> inputClass,
				Map<String, String> properties) {
			Map<String, Class<?>> map = new HashMap<String, Class<?>>();
			map.put("int_value", Integer.class);
			map.put("dec_value", Double.class);
			map.put("timestamp_value", Timestamp.class);
			map.put("date_value", java.sql.Date.class);
			map.put("time_value", Time.class);
			map.put("string_value", String.class);
			return map;
		}

		@Override
		public List<String> listRequiredModelProperties() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public WteDataModel createModel(Template<?> template, Object input) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("int_value", 4567);
			map.put("dec_value", 123.5678);
			map.put("timestamp_value", new Date());
			map.put("date_value", new Date());
			map.put("time_value", new Date());
			map.put("string_value",
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt");
			return new WteMapModel(map);
		}
	}
}
