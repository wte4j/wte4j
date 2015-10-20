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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.wte4j.ExpressionError;
import org.wte4j.InvalidTemplateException;
import org.wte4j.LockingException;
import org.wte4j.Template;
import org.wte4j.User;
import org.wte4j.WteException;

@RunWith(MockitoJUnitRunner.class)
public class WordTemplateTest {
	private static final String EMPTY_WORD = "org/wte4j/impl/empty.docx";
	private static final String TEST_TEXTFILE = "org/wte4j/impl/test.txt";

	@Mock
	private TemplateContextFactory contextFactory;

	@Mock
	private TemplateContext<String> templateContext;

	@Before
	public void initMocks() {
		when(contextFactory.createTemplateContext((Template<String>) any())).thenReturn(templateContext);
	}

	@Test
	public void writeTest() throws IOException {
		PersistentTemplate template = new PersistentTemplate();
		byte[] content = new byte[1];
		Arrays.fill(content, Byte.MIN_VALUE);
		template.setContent(content);
		WordTemplate<?> wordTemplate = new WordTemplate<Object>(template, null);
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
	public void listConentIds() throws IOException {
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
		List<String> ids = wordTemplate.listContentIds();
		assertEquals(1, ids.size());
		assertEquals("value", ids.get(0));
	}

	@Test
	public void validate() throws IOException {
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
		when(templateContext.validate("value")).thenReturn(ExpressionError.ILLEGAL_CONTENT_KEY);
		try {
			wordTemplate.validate();
			fail("InvalidTemplateExcption expected");
		} catch (InvalidTemplateException e) {
			assertEquals(ExpressionError.ILLEGAL_CONTENT_KEY, e.getErrors().get("value"));
		}
	}

	@Test
	public void toDocumentTest() throws IOException, Docx4JException {
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
		when(templateContext.resolveValue("value")).thenReturn("test123");
		File generatedDocument = File.createTempFile("generated", "docx");
		try (OutputStream out = new FileOutputStream(generatedDocument)) {
			wordTemplate.toDocument("gugus", out);
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.load(generatedDocument);
			String content = XmlUtils.marshaltoString(wordMLPackage
					.getMainDocumentPart().getContents(), true);
			assertTrue(content.contains("test123"));
		} finally {
			generatedDocument.deleteOnExit();
		}
	}
	
	@Test
	public void toPDFDocumentTest() throws IOException, Docx4JException {
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
		when(templateContext.resolveValue("value")).thenReturn("test123");
		File generatedDocument = File.createTempFile("generated", ".pdf");
		try (OutputStream out = new FileOutputStream(generatedDocument)) {
			wordTemplate.toPDFDocument("gugus", out);
			assertTrue(generatedDocument.isFile());
			assertTrue(generatedDocument.length() > 0);
			List<String> lines = FileUtils.readLines(generatedDocument);
			assertTrue(lines.size() > 0);
			assertTrue(lines.get(0).contains("PDF"));
		} finally {
			generatedDocument.deleteOnExit();
		}
	}

	@Test(expected = InvalidTemplateException.class)
	public void toDocumentWithInvalidDocument() throws IOException,
			Docx4JException {
		InvalidExpressionException error = new InvalidExpressionException(ExpressionError.FORMATTER_TYPE_MISSMATCH);
		when(templateContext.resolveValue(anyString())).thenThrow(error);

		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/dateValueTemplate.docx");

		OutputStream out = new ByteArrayOutputStream();
		wordTemplate.toDocument("test", out);
	}

	@Test
	public void toTestDocument() throws IOException, Docx4JException {
		when(templateContext.resolveValue("value")).thenReturn(TestDataModel.STRING_TEXT);
		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/simpleTemplate.docx");
		File generatedDocument = File.createTempFile("generated", "docx");
		try (OutputStream out = new FileOutputStream(generatedDocument)) {
			wordTemplate.toTestDocument(out);
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
		InvalidExpressionException error = new InvalidExpressionException(ExpressionError.FORMATTER_TYPE_MISSMATCH);
		when(templateContext.resolveValue(anyString())).thenThrow(error);

		WordTemplate<String> wordTemplate = createWordTemplate("org/wte4j/impl/dateValueTemplate.docx");
		OutputStream out = new ByteArrayOutputStream();
		wordTemplate.toTestDocument(out);
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
				persistentData, contextFactory);
		return wordTemplate;
	}

}
