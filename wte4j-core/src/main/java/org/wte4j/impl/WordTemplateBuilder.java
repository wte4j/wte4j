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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.TemplateBuildException;
import org.wte4j.TemplateBuilder;
import org.wte4j.TemplateExistException;
import org.wte4j.User;
import org.wte4j.WteException;
import org.wte4j.WteModelService;
import org.wte4j.impl.word.Docx4JWordTemplate;
import org.wte4j.impl.word.PlainTextContent;

public class WordTemplateBuilder<E> implements TemplateBuilder<E> {

	private WteModelService modelService;
	private TemplateContextFactory contextFactory;
	private Class<E> inputType;
	private User user;
	private String documentName;
	private String language;
	private Map<String, String> modelProperties = new HashMap<>();
	private Map<String, MappingDetail> mappingData = new HashMap<>();
	private Path templateFile;

	/**
	 * @param formatterFactory
	 * @param service
	 * @param inputType
	 * @throws TemplateBuildException
	 *             thrown if any of the parameters formatterFactory, service or
	 *             inputType are missing
	 */
	public WordTemplateBuilder(TemplateContextFactory contextFactory,
			WteModelService service, Class<E> inputType)
			throws TemplateBuildException {
		if (contextFactory == null || service == null || inputType == null) {
			throw new TemplateBuildException(
					"formatterFactory, service and inputType must not be null");
		}
		this.modelService = service;
		this.contextFactory = contextFactory;
		this.inputType = inputType;
		this.user = new User("", "");
		this.documentName = "new_template";
		this.language = Locale.getDefault().getLanguage();
	}

	@Override
	public TemplateBuilder<E> setProperties(Map<String, String> modelProperties) {
		this.modelProperties = modelProperties;
		return this;
	}

	@Override
	public TemplateBuilder<E> setDocumentName(String documentName) {
		this.documentName = documentName;
		return this;
	}

	@Override
	public TemplateBuilder<E> setAuthor(User user) {
		this.user = user;
		return this;
	}

	@Override
	public TemplateBuilder<E> setLanguage(String language) {
		this.language = language;
		return this;
	}

	@Override
	public TemplateBuilder<E> setMappingData(Map<String, MappingDetail> mappingData) {
		this.mappingData = mappingData;
		return this;

	}

	@Override
	public TemplateBuilder<E> setTemplateFile(Path aTemplateFile) {
		templateFile = aTemplateFile;
		return this;
	}

	/**
	 * @throws TemplateBuildException
	 *             thrown if a required property is missing or if any of the
	 *             parameters documentName, language or author's userId are
	 *             missing
	 */
	public Template<E> build() throws TemplateBuildException {
		validateBeforeBuild();
		PersistentTemplate template = new PersistentTemplate();
		template.setDocumentName(documentName);
		template.setLanguage(language);
		template.setInputType(inputType);
		template.setProperties(modelProperties);
		template.setEditor(user);
		template.setContentMapping(mappingData);
		template.setContent(getContent());
		Date date = new Date();
		template.setEditedAt(date);
		template.setCreatedAt(date);

		return new WordTemplate<E>(template, contextFactory);
	}

	protected byte[] getContent() {
		if (templateFile != null) {
			return getContentFromFile();
		}
		else {
			return createBasicTemplate();
		}
	}

	protected byte[] getContentFromFile() {
		try {
			return Files.readAllBytes(templateFile);
		} catch (IOException e) {
			throw new TemplateExistException("error on reading templateFile", e);
		}
	}

	byte[] createBasicTemplate() {
		try {
			Docx4JWordTemplate basicTemplate = new Docx4JWordTemplate();
			Map<String, Class<?>> dynamicElements = modelService
					.listModelElements(inputType, modelProperties);
			for (String key : dynamicElements.keySet()) {
				PlainTextContent plainTextContent = basicTemplate
						.addPlainTextContent();
				plainTextContent.setExpression(key);
				plainTextContent.setTitle(key);
				plainTextContent.setPlaceHolderContent(key);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			basicTemplate.writeAsOpenXML(out);
			return out.toByteArray();
		} catch (Exception e) {
			throw new WteException("Can not create BasicTemplate: "
					+ e.getMessage(), e);
		}
	}

	private void validateBeforeBuild() throws TemplateBuildException {
		for (String propertyName : modelService.listRequiredModelProperties()) {
			if (!modelProperties.containsKey(propertyName)) {
				throw new TemplateBuildException("required property missing ("
						+ propertyName + ")");
			}
		}
		if (StringUtils.isBlank(documentName) || StringUtils.isBlank(language)
				|| StringUtils.isBlank(user.getUserId())) {
			throw new TemplateBuildException(
					"documentName, language and author's userId must not be null or empty");
		}

	}

}
