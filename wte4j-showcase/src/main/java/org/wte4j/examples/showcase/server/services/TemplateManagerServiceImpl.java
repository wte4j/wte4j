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
package org.wte4j.examples.showcase.server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.wte4j.Template;
import org.wte4j.TemplateBuilder;
import org.wte4j.TemplateEngine;
import org.wte4j.TemplateExistException;
import org.wte4j.TemplateRepository;
import org.wte4j.examples.showcase.server.MessageKey;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.TemplateManagerServiceException;
import org.wte4j.examples.showcase.shared.service.TemplateManagerService;
import org.wte4j.ui.server.services.MessageFactory;
import org.wte4j.ui.server.services.ServiceContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TemplateManagerServiceImpl extends RemoteServiceServlet implements
		TemplateManagerService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ServiceContext serviceContext;
	
	@Autowired
	@Qualifier("wte4j-showcase")
	private MessageFactory messageFactory;

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public List<String> listDataModel() {
		List<String> dataModels = new ArrayList<String>();
		dataModels.add(OrderDataDto.class.getCanonicalName());
		return dataModels;
	}

	@Override
	public void createTemplate(String className, String templateName) throws TemplateManagerServiceException {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);

			TemplateBuilder<?> templateBuilder = templateEngine
					.getTemplateBuilder(clazz);
			TemplateRepository templateRepository = templateEngine
					.getTemplateRepository();

			templateBuilder.setAuthor(serviceContext.getUser());
			templateBuilder.setDocumentName(templateName);
			templateBuilder.setLanguage("en");
			Template<?> template = templateBuilder.build();

			templateRepository.persist(template);
		} catch (ClassNotFoundException e) {
			throw new TemplateManagerServiceException(getMessage(MessageKey.DATA_MODEL_NOT_FOUND), e);
		} catch (TemplateExistException e) {
			throw new TemplateManagerServiceException(getMessage(MessageKey.TEMPLATE_EXISTS_ALREADY), e);
		}
	}
	
	private String getMessage(MessageKey messageKey) {
		return messageFactory.createMessage(messageKey.getValue());
	}

}
