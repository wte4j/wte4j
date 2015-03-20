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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.model.structure.HeaderFooterPolicy;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtElement;

import ch.born.wte.WteException;

/**
 * Ein {@linkplain Docx4JWordTemplate} ist ein Word im OpenXml Format mit
 * Content Control elements.
 * 
 */
public class Docx4JWordTemplate {

	private final WordprocessingMLPackage wordMLPackage;
	private List<PlainTextContent> plainTextContentControls;

	public Docx4JWordTemplate() {
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
			plainTextContentControls = new ArrayList<PlainTextContent>();
		} catch (Docx4JException e) {
			throw new WteException("Can not create a new empty document:"
					+ e.getMessage(), e);
		}
	}

	public Docx4JWordTemplate(InputStream in) throws IOException {
		try {
			wordMLPackage = WordprocessingMLPackage.load(in);
			initPlainTextControlList();
		} catch (Docx4JException e) {
			unwrapIOException(e);
			throw new WteException("Error while reading document", e);
		}
	}

	private static void unwrapIOException(Exception e) throws IOException {
		if (e.getCause() instanceof IOException) {
			throw (IOException) e.getCause();
		}
	}

	protected WordprocessingMLPackage getWordMLPackage() {
		return wordMLPackage;
	}

	private void initPlainTextControlList() throws Docx4JException {
		try {
			plainTextContentControls = new ArrayList<PlainTextContent>();
			addControlsInMainPart();
			addControlsInHeaders();
			addPlainTextControlsInFooter();
		} catch (JAXBException e) {
			throw new WteException("Error on searching content controls", e);
		}

	}

	private void addControlsInMainPart() throws Docx4JException, JAXBException {
		List<SdtElement> elements = findContentControls(wordMLPackage
				.getMainDocumentPart());
		wrappAndAddPlainTextContentControls(elements);
	}

	private void addControlsInHeaders() throws Docx4JException, JAXBException {
		List<HeaderPart> headers = listHeaderParts();
		List<SdtElement> elements = findContentControls(headers);
		wrappAndAddPlainTextContentControls(elements);
	}

	private void addPlainTextControlsInFooter() throws Docx4JException,
			JAXBException {
		List<FooterPart> footers = listFooterParts();
		List<SdtElement> elements = findContentControls(footers);
		wrappAndAddPlainTextContentControls(elements);
	}

	private void wrappAndAddPlainTextContentControls(List<SdtElement> elements) {
		for (SdtElement element : elements) {
			if (PlainTextContent.isPlainTextContent(element)) {
				plainTextContentControls.add(new PlainTextContent(element));
			}
		}
	}

	private List<HeaderPart> listHeaderParts() {
		List<SectionWrapper> sectionWrappers = wordMLPackage.getDocumentModel()
				.getSections();
		List<HeaderPart> headerParts = new LinkedList<HeaderPart>();
		for (SectionWrapper sectionWrapper : sectionWrappers) {
			HeaderFooterPolicy policy = sectionWrapper.getHeaderFooterPolicy();
			CollectionUtils.addIgnoreNull(headerParts,
					policy.getDefaultHeader());
			CollectionUtils.addIgnoreNull(headerParts, policy.getEvenHeader());
			CollectionUtils.addIgnoreNull(headerParts, policy.getFirstHeader());
		}
		return headerParts;
	}

	private List<FooterPart> listFooterParts() {
		List<SectionWrapper> sectionWrappers = wordMLPackage.getDocumentModel()
				.getSections();
		List<FooterPart> footerParts = new LinkedList<FooterPart>();
		for (SectionWrapper sectionWrapper : sectionWrappers) {
			HeaderFooterPolicy policy = sectionWrapper.getHeaderFooterPolicy();
			CollectionUtils.addIgnoreNull(footerParts,
					policy.getDefaultFooter());
			CollectionUtils.addIgnoreNull(footerParts, policy.getEvenFooter());
			CollectionUtils.addIgnoreNull(footerParts, policy.getFirstFooter());
		}
		return footerParts;
	}

	/**
	 * Sucht alle content controls die nicht in einer reapeating section
	 * enthalten sind
	 * 
	 * @throws JAXBException
	 * @throws XPathBinderAssociationIsPartialException
	 */
	static List<SdtElement> findContentControls(ContentAccessor container)
			throws Docx4JException, JAXBException {
		List<SdtElement> sdtElements = new LinkedList<SdtElement>();
		for (Object o : container.getContent()) {
			Object unwrapped = XmlUtils.unwrap(o);
			if (unwrapped instanceof SdtElement) {
				sdtElements.add((SdtElement) unwrapped);
			}
			if (unwrapped instanceof ContentAccessor) {
				List<SdtElement> list = findContentControls((ContentAccessor) unwrapped);
				sdtElements.addAll(list);
			}
		}
		return sdtElements;
	}

	/**
	 * Sucht alle content controls die nicht in einer reapeating section
	 * enthalten sind.
	 * 
	 * @throws JAXBException
	 * @throws XPathBinderAssociationIsPartialException
	 */
	static List<SdtElement> findContentControls(
			List<? extends ContentAccessor> documentParts)
			throws Docx4JException, JAXBException {
		List<SdtElement> elements = new LinkedList<SdtElement>();
		for (ContentAccessor part : documentParts) {
			elements.addAll(findContentControls(part));
		}
		return elements;

	}

	public PlainTextContent addPlainTextContent() {
		SdtBlock block = PlainTextContent.createSdtBlockElement();
		wordMLPackage.getMainDocumentPart().getContent().add(block);
		PlainTextContent plainTextContent = new PlainTextContent(block);
		plainTextContentControls.add(plainTextContent);
		return plainTextContent;
	}

	public List<PlainTextContent> getPlainTextContent() {
		return plainTextContentControls;
	}

	/**
	 * Writes this document as word xml to a OutputStream and closed the stream
	 * in any case.
	 * 
	 * @param out
	 *            - the Outputstream to write the document.
	 * @throws IOException
	 */
	public void writeAsOPCXml(OutputStream out) throws IOException {
		try {
			SaveToZipFile saveToZipFile = new SaveToZipFile(wordMLPackage);
			saveToZipFile.saveFlatOPC(out);
		} catch (Docx4JException e) {
			unwrapIOException(e);
			throw new WteException("Can not marshall document", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * Writes this document as docx to a OutputStream and closed the stream in
	 * any case.
	 * 
	 * @param out
	 *            - the Outputstream to write the document.
	 * @throws IOException
	 */
	public void writeAsOpenXML(OutputStream out) throws IOException {
		try {
			SaveToZipFile saveToZipFile = new SaveToZipFile(wordMLPackage);
			saveToZipFile.save(out);
		} catch (Docx4JException e) {
			unwrapIOException(e);
			throw new WteException("Can not marshall document", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

}
