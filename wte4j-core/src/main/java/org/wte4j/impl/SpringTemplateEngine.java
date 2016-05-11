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
package org.wte4j.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.wte4j.InvalidTemplateException;
import org.wte4j.Template;
import org.wte4j.TemplateBuilder;
import org.wte4j.TemplateEngine;
import org.wte4j.TemplateFile;
import org.wte4j.TemplateRepository;
import org.wte4j.WteException;
import org.wte4j.WteModelService;
import org.wte4j.impl.word.WordTemplateFile;

@Service("wordTemplateEngine")
public class SpringTemplateEngine implements TemplateEngine {

	@Autowired
	protected TemplateRepository templateRepository;

	@Autowired(required = false)
	@Qualifier("wteModelService")
	protected WteModelService modelService;

	@Autowired
	protected TemplateContextFactory contextFactory;

	@Override
	public <E> TemplateBuilder<E> getTemplateBuilder(Class<E> inputType) {
		if (modelService == null) {
			throw new WteException("no bean with qualifier wteModelService of type " + WteModelService.class.getName() + " is defined");
		}
		return new WordTemplateBuilder<E>(contextFactory, modelService,
				inputType);
	}

	@Override
	public Path createDocument(String documentName, String language, Object data)
			throws IllegalArgumentException, InvalidTemplateException,
			IOException {
		Template<Object> template = templateRepository.getTemplate(
				documentName, language);
		if (template == null) {
			throw new IllegalArgumentException(
					"Template does not exists for document \"" + documentName
							+ "\" with the given language " + language);
		}
		return createFile(template, data);
	}

	Path createFile(Template<Object> template, Object data) throws IOException {
		File tempFile = File.createTempFile(template.getDocumentName(), ".docx");
		try (OutputStream out = Files.newOutputStream(tempFile.toPath())) {
			template.toDocument(data, out);
			return tempFile.toPath();
		}
	}

	@Override
	public TemplateRepository getTemplateRepository() {
		return templateRepository;
	}

	@Override
	public TemplateFile asTemplateFile(Path aFile) throws IOException {
		try (InputStream in = Files.newInputStream(aFile)) {

			WordTemplateFile templateFile = new WordTemplateFile(in);
			return templateFile;
		}
	}
}
