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
package org.wte4j.impl.word;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.RPr;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtElement;
import org.docx4j.wml.SdtRun;
import org.docx4j.wml.Tag;
import org.junit.Test;

public class PlainTextContentTest {

	@Test
	public void testCreateSdtBlockElement() {
		SdtElement element = PlainTextContent.createSdtBlockElement();
		String text = XmlUtils.marshaltoString(element, true);
		String expectedContent = "<w:sdtPr>" //
				+ "<w:tag/>"//
				+ "<w:text/>"//
				+ "<w:showingPlcHdr/>"//
				+ "<w15:appearance w15:val=\"tags\"/>"//
				+ "</w:sdtPr>"//
				+ "<w:sdtContent/>";
		assertTrue(text.contains(expectedContent));
	}

	@Test
	public void testFormatTextOfBlock() throws Exception {
		SdtElement contentControl = createPlainTextContentControl(SdtBlock.class);
		RPr rPr = createRunProperties("<w:b/>");
		contentControl.getSdtPr().getRPrOrAliasOrLock().add(rPr);
		PlainTextContent plainTextContent = new PlainTextContent(contentControl);
		plainTextContent.setContent("content");

		String xmlString = XmlUtils.marshaltoString(
				plainTextContent.getXmlElement(), true);
		String content = getSdtContent(xmlString);
		String expectedContent = "<w:p>"//
				+ "<w:r>" //
				+ "<w:rPr><w:b/></w:rPr>" // formatierung
				+ "<w:t>content</w:t>" // text
				+ "</w:r>"//
				+ "</w:p>";
		assertEquals(expectedContent, content);

	}

	@Test
	public void testFormatTextOfRun() throws Exception {
		SdtElement contentControl = createPlainTextContentControl(SdtRun.class);
		RPr rPr = createRunProperties("<w:b/>");
		contentControl.getSdtPr().getRPrOrAliasOrLock().add(rPr);
		PlainTextContent plainTextContent = new PlainTextContent(contentControl);
		plainTextContent.setContent("content");
		String xmlString = XmlUtils.marshaltoString(
				plainTextContent.getXmlElement(), true);
		String content = getSdtContent(xmlString);
		String expectedContent = "<w:r>"//
				+ "<w:rPr><w:b/></w:rPr>" // formatierung
				+ "<w:t>content</w:t>" // text
				+ "</w:r>";
		assertEquals(expectedContent, content);
	}

	@Test
	public void testGetExpression() throws Exception {
		String expressionString = "expression";

		Tag tag = new Tag();
		tag.setVal(expressionString);

		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		element.getSdtPr().setTag(tag);

		PlainTextContent plainTextContent = new PlainTextContent(element);
		assertEquals(expressionString, plainTextContent.getExpression());
	}

	@Test
	public void testSetExpression() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		PlainTextContent plainTextContent = new PlainTextContent(element);
		plainTextContent.setExpression("expression");
		String xmlString = XmlUtils.marshaltoString(
				plainTextContent.getXmlElement(), true);
		assertTrue(xmlString.contains("<w:tag w:val=\"expression\"/>"));
	}

	@Test()
	public void testExpressionNotSet() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		element.getSdtPr().setTag(null);
		PlainTextContent plainTextContent = new PlainTextContent(element);
		assertTrue(StringUtils.isEmpty(plainTextContent.getExpression()));
	}

	@Test
	public void testhasExpression() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		Tag tag = new Tag();
		tag.setVal("tag");
		element.getSdtPr().setTag(tag);
		PlainTextContent plainTextContent = new PlainTextContent(element);
		assertTrue(plainTextContent.hasExpression());
	}

	@Test()
	public void testhasNoExpression() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		Tag tag = new Tag();
		element.getSdtPr().setTag(tag);

		PlainTextContent plainTextContent = new PlainTextContent(element);
		assertFalse(plainTextContent.hasExpression());
	}

	@Test
	public void testSetTitle() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		PlainTextContent plainTextContent = new PlainTextContent(element);
		plainTextContent.setTitle("testLabel");
		String xmlString = XmlUtils.marshaltoString(
				plainTextContent.getXmlElement(), true);
		assertTrue(xmlString + " has no alias set",
				xmlString.contains("<w:alias w:val=\"testLabel\"/>"));
	}

	@Test
	public void testSetPlaceHolderText() throws Exception {
		SdtElement element = createPlainTextContentControl(SdtBlock.class);
		PlainTextContent plainTextContent = new PlainTextContent(element);
		plainTextContent.setPlaceHolderContent("placeHolderText");
		String xmlString = XmlUtils.marshaltoString(
				plainTextContent.getXmlElement(), true);
		int startContent = xmlString.indexOf("<w:sdtContent>")
				+ "<w:sdtContent>".length();
		int endContent = xmlString.lastIndexOf("</w:sdtContent>");
		String content = xmlString.substring(startContent, endContent);
		String expectedContent = "<w:p>"//
				+ "<w:r>"//
				+ "<w:rPr><w:rStyle w:val=\"PlaceholderText\"/></w:rPr>"//
				+ "<w:t>placeHolderText</w:t>"//
				+ "</w:r>"//
				+ "</w:p>";
		assertEquals(expectedContent, content);
	}

	private static <E extends SdtElement> E createPlainTextContentControl(
			Class<E> type) throws JAXBException, IOException {
		InputStream in = ClassLoader
				.getSystemResourceAsStream("org/wte4j/impl/word/PlainTextContentControl.xml");
		try {
			String xml = IOUtils.toString(in);
			return (E) XmlUtils.unmarshalString(xml, Context.jc, type);

		} finally {
			in.close();
		}
	}

	private static RPr createRunProperties(String rPrContentAsXml)
			throws JAXBException {
		String rPrXml = "<w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
				+ rPrContentAsXml + "</w:rPr>";
		return (RPr) XmlUtils.unmarshalString(rPrXml, Context.jc, RPr.class);
	}

	private String getSdtContent(String xmlString) {
		int startContent = xmlString.indexOf("<w:sdtContent>")
				+ "<w:sdtContent>".length();
		int endContent = xmlString.indexOf("</w:sdtContent>", startContent);
		return xmlString.substring(startContent, endContent);
	}

}
