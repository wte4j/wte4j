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
package org.wte4j.ui.shared;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("templateService")
public interface TemplateService extends RemoteService {
	List<TemplateDto> getTemplates() throws TemplateServiceException;;

	TemplateDto lockTemplate(TemplateDto template) throws TemplateServiceException;

	TemplateDto unlockTemplate(TemplateDto template) throws TemplateServiceException;;

	void deleteTemplate(TemplateDto template) throws TemplateServiceException;

	List<ModelElementDto> listModelElements(String inputType, Map<String, String> properties) throws TemplateServiceException;

	/**
	 * List all unique content ids for dynamic content in a template file. The file
	 * must have been uploaded to the server before.
	 * 
	 * @param templateFile
	 *            name of the file on server
	 * @return
	 */
	List<String> listUniqueContentIds(String pathToFile) throws TemplateServiceException;

	TemplateDto saveTemplateData(TemplateDto templateDto, String uploadedTemplate) throws TemplateServiceException, InvalidTemplateServiceException;

	TemplateDto createTemplate(TemplateDto newTemplate, String templateFile) throws TemplateServiceException, InvalidTemplateServiceException;

}
