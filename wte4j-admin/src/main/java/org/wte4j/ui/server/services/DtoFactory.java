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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.User;
import org.wte4j.ui.shared.MappingDto;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.UserDto;

public final class DtoFactory {

	public static TemplateDto createTemplateDto(Template<?> template) {
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

		dto.setInputType(template.getInputType().getName());
		dto.setProperties(new HashMap<String, String>(template.getProperties()));
		dto.setMapping(crateMappingDtos(template.getContentMapping()));
		return dto;
	}

	public static UserDto createUserDto(User user) {
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		dto.setDisplayName(user.getDisplayName());
		return dto;
	}

	public static List<MappingDto> crateMappingDtos(Map<String, MappingDetail> mapping) {
		List<MappingDto> mappingDtos = new ArrayList<>();
		for (Map.Entry<String, MappingDetail> entry : mapping.entrySet()) {
			MappingDto mappingDto = new MappingDto();
			mappingDto.setgetConentControlId(entry.getKey());
			mappingDto.setModelKey(entry.getValue().getModelKey());
			mappingDto.setFormatterDefinition(entry.getValue().getFormatterDefinition());
			mappingDtos.add(mappingDto);
		}
		return mappingDtos;
	}

}
