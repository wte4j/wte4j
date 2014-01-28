package ch.born.wte;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch.born.wte.impl.service.SimpleDbViewModelService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TemplateEngineTest.TemplateEngineConfiguration.class })
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
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("viewName", "WTE_TEST_DATA");
		properties.put("pkColumnName", "ID");

		TemplateBuilder<Integer> builder = wte
				.getTemplateBuilder(Integer.class);

		Template<Integer> template = builder.setDocumentName(documentName)
				.setLanguage(language)
				.setAuthor(new User("userId", "Hans Wurst"))
				.setProperties(properties).build();
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
	@ComponentScan(basePackages = { "ch.born.wte" })
	@ImportResource("classpath:test-persistence-context.xml")
	@EnableTransactionManagement
	public static class TemplateEngineConfiguration {

		@Autowired
		DataSource ds;

		@Bean(autowire = Autowire.NO)
		WteModelService wteModelService() {
			SimpleDbViewModelService modelService = new SimpleDbViewModelService(
					ds);
			return modelService;
		}
	}

}
