package ch.born.wte.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.InvalidTemplateException;
import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.UnknownFormatterException;
import ch.born.wte.User;
import ch.born.wte.WteDataModel;
import ch.born.wte.WteException;
import ch.born.wte.WteModelService;
import ch.born.wte.impl.format.ToStringFormatter;

public class WordTemplateTest {

	private static final String EMPTY_WORD = "ch/born/wte/impl/empty.docx";
	private static final String TEST_TEXTFILE = "ch/born/wte/impl/test.txt";

	@Test
	public void writeTest() throws IOException {
		PersistentTemplate template = new PersistentTemplate();
		byte[] content = new byte[1];
		Arrays.fill(content, Byte.MIN_VALUE);
		template.setContent(content);

		WordTemplate<?> wordTemplate = new WordTemplate<Object>(template, null,
				null);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wordTemplate.write(out);
		assertTrue("Content is not equals",
				Arrays.equals(content, out.toByteArray()));
	}

	@Test
	public void writeFileTest() throws IOException {
		WordTemplate<String> template = createWordTemplate(EMPTY_WORD);
		File templateFile = File.createTempFile("template", "docx");
		long length = templateFile.length();
		try {
			template.write(templateFile);
			assertTrue("File has not been writen",
					templateFile.length() > length);
		} finally {
			templateFile.delete();
		}
	}

	@Test
	public void updateUnlockedTemplate() throws IOException {
		WordTemplate<?> wordTemplate = createWordTemplate(TEST_TEXTFILE);

		User user = new User("user", "user");
		updateContent(wordTemplate, user, EMPTY_WORD);
		checkUpdateContent(wordTemplate, user, EMPTY_WORD);
	}

	@Test()
	public void updateWithLockingUser() throws IOException {
		User user = new User("user1", "user1");
		WordTemplate<?> wordTemplate = createWordTemplate(TEST_TEXTFILE);
		wordTemplate.getPersistentData().lock(user);
		updateContent(wordTemplate, user, EMPTY_WORD);
		checkUpdateContent(wordTemplate, user, EMPTY_WORD);
	}

	@Test(expected = LockingException.class)
	public void updateLockedWithDifferentEditor() throws IOException {
		WordTemplate<?> wordTemplate = createWordTemplate(TEST_TEXTFILE);
		wordTemplate.getPersistentData().lock(new User("user1", "user1"));

		User user2 = new User("user2", "user 2");
		updateContent(wordTemplate, user2, EMPTY_WORD);
		fail("Exception expected");
	}

	@Test
	public void updateWithNonWordDocument() throws IOException {
		WordTemplate<String> template = createWordTemplate(EMPTY_WORD);
		User editor = template.getEditor();
		try {
			updateContent(template, new User("user", "user"), TEST_TEXTFILE);
			fail("Exception expected");
		} catch (WteException e) {
			checkUpdateContent(template, editor, EMPTY_WORD);
		}
	}

	@Test
	public void updateWithInvalidWordDocument() throws IOException {
		WordTemplate<String> wordTemplate = createWordTemplate(EMPTY_WORD);
		User editor = wordTemplate.getEditor();

		try {
			updateContent(wordTemplate, new User("user", "user"),
					"ch/born/wte/impl/dateValueTemplate.docx");
			fail("Exception expected");
		} catch (InvalidTemplateException e) {
			checkUpdateContent(wordTemplate, editor, EMPTY_WORD);
		}
	}

	@Test
	public void toDocumentTest() throws IOException, Docx4JException {

		WordTemplate<String> wordTemplate = createWordTemplate("ch/born/wte/impl/simpleTemplate.docx");
		File generatedDocument = File.createTempFile("generated", "docx");
		try {
			wordTemplate.toDocument("test123", generatedDocument);

			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.load(generatedDocument);
			String content = XmlUtils.marshaltoString(wordMLPackage
					.getMainDocumentPart().getContents(), true);
			assertTrue(content.contains("test123"));
		} finally {
			generatedDocument.deleteOnExit();
		}
	}

	@Test(expected = InvalidTemplateException.class)
	public void toDocumentWithInvalidDocument() throws IOException,
			Docx4JException {

		WordTemplate<String> wordTemplate = createWordTemplate("ch/born/wte/impl/dateValueTemplate.docx");
		OutputStream out = new ByteArrayOutputStream();
		wordTemplate.toDocument("test", out);
		fail(InvalidTemplateException.class + " expected");
	}

	@Test
	public void toTestDocument() throws IOException, Docx4JException {
		WordTemplate<String> wordTemplate = createWordTemplate("ch/born/wte/impl/simpleTemplate.docx");
		File generatedDocument = File.createTempFile("generated", "docx");
		try {
			wordTemplate.toTestDocument(generatedDocument);

			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.load(generatedDocument);
			String content = XmlUtils.marshaltoString(wordMLPackage
					.getMainDocumentPart().getContents(), true);
			assertTrue(content.contains(TestDataModel.STRING_TEXT));
		} finally {
			generatedDocument.delete();
		}
	}

	@Test(expected = InvalidTemplateException.class)
	public void toTestDocumentWithInvalidTemplate() throws IOException,
			Docx4JException {
		WordTemplate<String> wordTemplate = createWordTemplate("ch/born/wte/impl/dateValueTemplate.docx");
		OutputStream out = new ByteArrayOutputStream();
		wordTemplate.toTestDocument(out);
		fail(InvalidTemplateException.class + "expected");
	}

	private static void updateContent(WordTemplate<?> template, User user,
			String fileName) throws IOException {
		InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
		try {
			template.update(in, user);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private void checkUpdateContent(WordTemplate<?> wordTemplate, User user,
			String fileName) throws IOException {
		File file = FileUtils.toFile(ClassLoader.getSystemResource(fileName));

		byte[] expectedContent = FileUtils.readFileToByteArray(file);
		byte[] currentContent = wordTemplate.getPersistentData().getContent();

		assertTrue(Arrays.equals(expectedContent, currentContent));
		assertEquals(user, wordTemplate.getEditor());
		assertNotNull(wordTemplate.getEditedAt());
	}

	private WordTemplate<String> createWordTemplate(String pathToTemplateFile)
			throws IOException {
		File templateDocument = FileUtils.toFile(ClassLoader
				.getSystemResource(pathToTemplateFile));
		byte[] templateContent = FileUtils
				.readFileToByteArray(templateDocument);

		PersistentTemplate persistentData = new PersistentTemplate();
		persistentData.setDocumentName("test");
		persistentData.setLanguage("de");
		persistentData.setInputType(String.class);
		persistentData.setEditedAt(new Date());
		persistentData.setCreatedAt(new Date());
		persistentData.setEditor(new User("default", "default"));
		persistentData.setContent(templateContent);

		WordTemplate<String> wordTemplate = new WordTemplate<String>(
				persistentData, new SimpleValueModelService(),
				new SimpleFormatterFactory());
		return wordTemplate;
	}

	private static class SimpleValueModelService implements WteModelService {

		@Override
		public Map<String, Class<?>> listModelElements(Class<?> inputClass,
				Map<String, String> properties) {
			return Collections.<String, Class<?>> singletonMap("value",
					String.class);
		}

		@Override
		public List<String> listRequiredModelProperties() {
			return Collections.emptyList();
		}

		@Override
		public WteDataModel createModel(Template<?> template, final Object input) {
			return new WteDataModel() {
				@Override
				public Object getValue(String key)
						throws IllegalArgumentException {
					if (key.equals("value")) {
						return input.toString();
					} else {
						throw new IllegalArgumentException();
					}
				}
			};
		}

	}

	private static class SimpleFormatterFactory implements FormatterFactory {

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
