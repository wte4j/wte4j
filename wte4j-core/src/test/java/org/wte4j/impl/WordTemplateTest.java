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
import org.wte4j.Formatter;
import org.wte4j.FormatterFactory;
import org.wte4j.FormatterInstantiationException;
import org.wte4j.InvalidTemplateException;
import org.wte4j.LockingException;
import org.wte4j.Template;
import org.wte4j.UnknownFormatterException;
import org.wte4j.User;
import org.wte4j.WteDataModel;
import org.wte4j.WteException;
import org.wte4j.WteModelService;
import org.wte4j.impl.format.ToStringFormatter;

public class WordTemplateTest {

	private static final String EMPTY_WORD = "org/wte4j/impl/empty.docx";
	private static final String TEST_TEXTFILE = "org/wte4j/impl/test.txt";

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
					"org/wte4j/impl/dateValueTemplate.docx");
			fail("Exception expected");
		} catch (InvalidTemplateException e) {
			checkUpdateContent(wordTemplate, editor, EMPTY_WORD);
		}
	}

	@Test
	public void toDocumentTest() throws IOException, Docx4JException {

		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
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

		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/dateValueTemplate.docx");
		OutputStream out = new ByteArrayOutputStream();
		wordTemplate.toDocument("test", out);
		fail(InvalidTemplateException.class + " expected");
	}

	@Test
	public void toTestDocument() throws IOException, Docx4JException {
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
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
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/dateValueTemplate.docx");
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
