package ch.born.wte.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.Template;
import ch.born.wte.TemplateBuildException;
import ch.born.wte.UnknownFormatterException;
import ch.born.wte.User;
import ch.born.wte.WteDataModel;
import ch.born.wte.WteModelService;
import ch.born.wte.impl.format.ToStringFormatter;

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
		WordTemplateBuilder<String> wtb = new WordTemplateBuilder<String>(
				new SimpleFormatterFactory(), new SimpleValueModelService(),
				String.class);
		wtb.setAuthor(new User("test1", "test2"));
		Template<String> wt = wtb.build();
		assertEquals(wt.getClass(), WordTemplate.class);
		assertNotNull(wt);
	}

	@Test(expected = TemplateBuildException.class)
	public void testParametersMissing() {
		new WordTemplateBuilder<String>(null, null, null);
		fail("Exception expected");
	}

	@Test(expected = TemplateBuildException.class)
	public void testRequiredPropertiesMissing() {
		requiredProperties.add("test");
		WordTemplateBuilder<String> wtb = new WordTemplateBuilder<String>(
				new SimpleFormatterFactory(), new SimpleValueModelService(),
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

		FormatterFactory factory = mock(FormatterFactory.class);
		WordTemplateBuilder<String> builder = new WordTemplateBuilder<String>(
				factory, service, String.class);

		byte[] templateContent = builder.createBasicTemplate();
		File templateFile = File.createTempFile("generated", "docx");
		try {
			FileUtils.writeByteArrayToFile(templateFile, templateContent);
			ZipFile zipFile = new ZipFile(templateFile);
			ZipEntry entry = zipFile.getEntry("word/document.xml");
			InputStream documentIn = zipFile.getInputStream(entry);
			String doucmentXml = IOUtils.toString(documentIn);
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
