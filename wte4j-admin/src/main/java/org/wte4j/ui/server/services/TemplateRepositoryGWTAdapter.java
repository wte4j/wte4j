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
package org.wte4j.ui.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.wte4j.LockingException;
import org.wte4j.Template;
import org.wte4j.TemplateRepository;
import org.wte4j.User;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateService;
import org.wte4j.ui.shared.TemplateServiceException;
import org.wte4j.ui.shared.UserDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TemplateRepositoryGWTAdapter extends RemoteServiceServlet implements TemplateService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServiceContext serviceContext;

	@Autowired
	@Qualifier("wte4j-admin")
	private MessageFactory messageFactory;

	@Autowired
	private TemplateRepository templateRepository;

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public List<TemplateDto> getTemplates() {
		List<Template<Object>> templates = templateRepository.queryTemplates()
				.list();
		List<TemplateDto> templateDtos = new ArrayList<TemplateDto>();
		for (Template<Object> template : templates) {
			TemplateDto templateDto = createTemplateDto(template);
			templateDtos.add(templateDto);
		}
		return templateDtos;
	}

	@Override
	public TemplateDto lockTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		try {
			template = templateRepository.lockForEdit(template, serviceContext.getUser());
			return createTemplateDto(template);
		} catch (LockingException e) {
			logger.debug("template {}_{} is locked by {}",
					template.getDocumentName(),
					template.getLanguage(),
					template.getLockingUser().getDisplayName(),
					e);
			throw createServiceException(MessageKey.LOCKED_TEMPLATE);
		}
	}

	@Override
	public TemplateDto unlockTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		template = templateRepository.unlock(template);
		return createTemplateDto(template);
	}

	@Override
	public void deleteTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		try {
			templateRepository.delete(template);

		} catch (LockingException e) {
			logger.debug("template {}_{} is locked by {}",
					template.getDocumentName(),
					template.getLanguage(),
					template.getLockingUser().getDisplayName(),
					e);
			throw createServiceException(MessageKey.LOCKED_TEMPLATE);
		}

	}

	Template<?> lookup(TemplateDto dto) {
		Template<?> template = templateRepository.getTemplate(dto.getDocumentName(), dto.getLanguage());
		if (template == null) {
			throw createServiceException(MessageKey.TEMPLATE_NOT_FOUND);
		}
		return template;
	}

	TemplateDto createTemplateDto(Template<?> template) {
		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());

		if (template.getEditedAt() != null) {
			dto.setUpdateAt(new Date(template.getEditedAt().getTime()));
		}

		dto.setEditor(createUserDto(template.getEditor()));
		if (template.getLockingUser() != null) {
			dto.setLockingUser(createUserDto(template.getLockingUser()));
		}

		return dto;
	}

	UserDto createUserDto(User user) {
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		dto.setDisplayName(user.getDisplayName());
		return dto;
	}

	TemplateServiceException createServiceException(MessageKey key) {
		String message = messageFactory.createMessage(key.getValue());
		return new TemplateServiceException(message);
	}

}