package ch.born.wte;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

import ch.born.wte.impl.service.WteMapModel;

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
		final File templateDocument = FileUtils.toFile(ClassLoader
				.getSystemResource("ch/born/wte/basic-values-template.docx"));

		Template<Integer> template = createTemplate(documentName, language,
				templateDocument);

		TemplateRepository repository = wte.getTemplateRepository();
		template = repository.persist(template);

		File document = wte.createDocument(documentName, language, 1);

		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
				.load(document);
		StringWriter writer = new StringWriter();
		TextUtils.extractText(
				wordMLPackage.getMainDocumentPart().getContents(), writer);
		String contentAsText = writer.toString();
		assertTrue(!contentAsText.contains("toReplace"));
	}

	private Template<Integer> createTemplate(String documentName,
			String language) {

		TemplateBuilder<Integer> builder = wte
				.getTemplateBuilder(Integer.class);

		Template<Integer> template = builder.setDocumentName(documentName)
				.setLanguage(language)
				.setAuthor(new User("userId", "Hans Wurst")).build();
		return template;
	}

	private Template<Integer> createTemplate(String documentName,
			String language, File templateDocument) throws IOException {
		Template<Integer> template = createTemplate(documentName, language);
		InputStream in = null;
		try {
			in = FileUtils.openInputStream(templateDocument);
			template.update(in, template.getEditor());
		} finally {
			IOUtils.closeQuietly(in);
		}
		return template;
	}

	@Configuration
	@ComponentScan(basePackages = { "ch.born.wte.impl" })
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
