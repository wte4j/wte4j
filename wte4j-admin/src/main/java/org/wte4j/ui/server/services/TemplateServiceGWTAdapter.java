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
package org.wte4j.ui.server.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.wte4j.ui.shared.InvalidTemplateServiceException;
import org.wte4j.ui.shared.ModelElementDto;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateService;
import org.wte4j.ui.shared.TemplateServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TemplateServiceGWTAdapter extends RemoteServiceServlet implements TemplateService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TemplateService templateService;

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public List<TemplateDto> getTemplates() throws TemplateServiceException {
		return templateService.getTemplates();
	}

	public TemplateDto lockTemplate(TemplateDto template) throws TemplateServiceException {
		return templateService.lockTemplate(template);
	}

	public TemplateDto unlockTemplate(TemplateDto template) throws TemplateServiceException {
		return templateService.unlockTemplate(template);
	}

	public void deleteTemplate(TemplateDto template) throws TemplateServiceException {
		templateService.deleteTemplate(template);
	}

	public List<ModelElementDto> listModelElements(String inputType, Map<String, String> properties) throws TemplateServiceException {
		return templateService.listModelElements(inputType, properties);
	}

	public List<String> listUniqueContentIds(String templateFile) throws TemplateServiceException {
		return templateService.listUniqueContentIds(templateFile);
	}

	public TemplateDto saveTemplateData(TemplateDto templateDto, String uploadedTemplate) throws TemplateServiceException, InvalidTemplateServiceException {
		return templateService.saveTemplateData(templateDto, uploadedTemplate);
	}

	public TemplateDto createTemplate(TemplateDto newTemplate, String templateFile) {
		return templateService.createTemplate(newTemplate, templateFile);
	}

}