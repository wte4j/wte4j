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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.fonts.PhysicalFonts;
import org.wte4j.InvalidTemplateException;
import org.wte4j.LockingException;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.User;
import org.wte4j.WteException;
import org.wte4j.impl.word.WordTemplateFile;

/**
 * {@link Template} implementation for word documents (docx)
 * 
 * @param <E>
 *            see {@link Template}
 */
public class WordTemplate<E> implements Template<E> {
	private PersistentTemplate persistentData;
	private TemplateContextFactory contextFactory;
	private WordTemplateFile document;

	public WordTemplate(PersistentTemplate template, TemplateContextFactory contextFactory) {
		this.persistentData = template;
		this.contextFactory = contextFactory;
	}

	@Override
	public void toDocument(E data, OutputStream out) throws IOException,
			InvalidTemplateException {
		prepareDocument();
		TemplateContext<E> context = contextFactory.createTemplateContext(this);
		context.bind(data);
		document.updateDynamicContent(context);
		document.writeAsOpenXML(out);
	}

	private void prepareDocument() {
		if (document == null) {
			try {
				document = createDocument(persistentData.getContent());
			} catch (IOException e) {
				throw new WteException(e);
			}
		}
	}

	private WordTemplateFile createDocument(byte[] content)
			throws IOException {
		return new WordTemplateFile(new ByteArrayInputStream(content));
	}

	@Override
	public void toTestDocument(OutputStream out)
			throws InvalidTemplateException, IOException {
		TemplateContext<E> context = contextFactory.createTemplateContext(this);
		prepareDocument();
		document.updateDynamicContent(context);
		document.writeAsOpenXML(out);
	}
	
	@Override
	public void toPDFDocument(E data, OutputStream out) throws IOException {
        PhysicalFonts.setRegex(null);
        
        prepareDocument();
        TemplateContext<E> context = contextFactory.createTemplateContext(this);
        context.bind(data);
        document.updateDynamicContent(context);
        document.writeAsPDF(out);
}


	@Override
	public String getDocumentName() {
		return persistentData.getDocumentName();
	}

	@Override
	public Class<?> getInputType() {
		return persistentData.getInputType();
	}

	@Override
	public Map<String, String> getProperties() {
		return persistentData.getProperties();
	}

	public String getLanguage() {
		return persistentData.getLanguage();
	}

	public Locale getLocale() {
		if (persistentData.getLanguage() == null) {
			return null;
		}
		return new Locale(persistentData.getLanguage());
	}

	public User getLockingUser() {
		return persistentData.getLockingUser();
	}

	public Date getCreatedAt() {
		return persistentData.getCreatedAt();
	}

	public User getEditor() {
		return persistentData.getEditor();
	}

	public Date getEditedAt() {
		return persistentData.getEditedAt();
	}

	@Override
	public void update(InputStream in, User editor) throws IOException,
			InvalidTemplateException, LockingException {
		byte[] content = IOUtils.toByteArray(in);
		document = createDocument(content);
		persistentData.setContent(content, editor);
	}

	@Override
	public Map<String, MappingDetail> getContentMapping() {
		return persistentData.getContentMapping();
	}

	@Override
	public List<String> listContentIds() {
		prepareDocument();
		return document.listContentIds();
	}

	@Override
	public void validate() throws InvalidTemplateException {
		prepareDocument();
		document.validate(contextFactory.createTemplateContext(this));
	}

	@Override
	public void write(File file) throws IOException {
		OutputStream out = FileUtils.openOutputStream(file);
		write(out);
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		persistentData.writeContent(out);
	}

	protected PersistentTemplate getPersistentData() {
		return persistentData;
	}
}
