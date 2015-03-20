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
package ch.born.wte.impl.word;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtElement;
import org.junit.Test;

public class Docx4JWordTemplateTest {

	@Test
	public void writeAsOPCXmlTest() throws IOException, Docx4JException,
			JAXBException {
		Docx4JWordTemplate doc = new Docx4JWordTemplate();

		File file = File.createTempFile("test", "docx");
		OutputStream out = FileUtils.openOutputStream(file);
		try {
			doc.writeAsOPCXml(out);
		} finally {
			IOUtils.closeQuietly(out);
		}
		String content = FileUtils.readFileToString(file);
		assertTrue(content.startsWith("<?xml"));
	}

	@Test
	public void writeAsOpenXMLTest() throws IOException, Docx4JException,
			JAXBException {
		Docx4JWordTemplate doc = new Docx4JWordTemplate();
		File file = File.createTempFile("test", "docx");
		OutputStream out = FileUtils.openOutputStream(file);
		try {
			doc.writeAsOpenXML(out);
		} finally {
			IOUtils.closeQuietly(out);
		}
		ZipFile zipFile = new ZipFile(file);
		ZipEntry entry = zipFile.getEntry("word/document.xml");
		assertNotNull(entry);
	}

	@Test
	public void findContentControlsTest() throws Exception {
		InputStream in = ClassLoader
				.getSystemResourceAsStream("ch/born/wte/impl/word/ContentControls.docx");
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
				.load(in);
		in.close();
		List<SdtElement> elements = Docx4JWordTemplate
				.findContentControls(wordMLPackage.getMainDocumentPart());
		assertEquals(9, elements.size());
	}

	@Test
	public void addPlainTextContentTest() throws Docx4JException, JAXBException {
		Docx4JWordTemplate doc = new Docx4JWordTemplate();
		doc.addPlainTextContent();
		assertEquals(1, doc.getPlainTextContent().size());
		List<Object> content = doc.getWordMLPackage().getMainDocumentPart()
				.getContent();
		assertEquals(1, content.size());
		assertTrue(content.get(0) instanceof SdtBlock);
		assertEquals(1, doc.getPlainTextContent().size());
	}

	/**
	 * Testet ob plain text content controls im header, footer und hauptteil
	 * (als Absatzt, im Absatz und in Tabelle) gefunden werden.
	 * 
	 * @throws Docx4JException
	 * @throws JAXBException
	 */
	@Test
	public void loadTemplateWithPlainTextContentTest() throws IOException {
		InputStream in = getClass().getResourceAsStream(
				"PlainTextContentControls.docx");
		try {
			Docx4JWordTemplate doc = new Docx4JWordTemplate(in);
			assertEquals(5, doc.getPlainTextContent().size());
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
