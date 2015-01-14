package ch.born.wte.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import ch.born.wte.FormatterFactory;
import ch.born.wte.InvalidTemplateException;
import ch.born.wte.Template;
import ch.born.wte.TemplateBuilder;
import ch.born.wte.TemplateEngine;
import ch.born.wte.TemplateRepository;
import ch.born.wte.WteException;
import ch.born.wte.WteModelService;

@Service
public class SpringTemplateEngine implements TemplateEngine {

	@Autowired
	protected TemplateRepository templateRepository;

	@Autowired(required=false)
	@Qualifier("wteModelService")
	protected WteModelService modelService;

	@Autowired
	protected FormatterFactory formatterFactory;

	@Override
	public <E> TemplateBuilder<E> getTemplateBuilder(Class<E> inputType) {
		if(modelService ==null){
			throw new WteException("no bean with qualifier wteModelService of type " + WteModelService.class.getName() + " is defined");
		}
		return new WordTemplateBuilder<E>(formatterFactory, modelService,
				inputType);
	}

	@Override
	public File createDocument(String documentName, String language, Object data)
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

	File createFile(Template<Object> template, Object data) throws IOException {
		File tempFile = File.createTempFile(template.getDocumentName(), "docx");
		template.toDocument(data, tempFile);
		return tempFile;
	}

	@Override
	public TemplateRepository getTemplateRepository() {
		return templateRepository;
	}

}
