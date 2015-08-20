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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.wte4j.Template;
import org.wte4j.TemplateEngine;
import org.wte4j.User;
import org.wte4j.ui.shared.InvalidTemplateServiceException;
import org.wte4j.ui.shared.MappingDto;
import org.wte4j.ui.shared.ModelElementDto;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateService;
import org.wte4j.ui.shared.UserDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IntegrationTestConfiguration.class })
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TemplateServiceIntegrationTest {
	@Autowired
	ServiceContext ServiceContext;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateEngine engine;

	@Test
	public void getTemplates() throws ClassNotFoundException {
		Template<TemplateDto> persisted = createAndPersistTestTemplate();
		List<TemplateDto> templates = templateService.getTemplates();
		TemplateDto template = templates.get(0);
		assertEquals(persisted.getDocumentName(), template.getDocumentName());
		assertEquals(persisted.getLanguage(), template.getLanguage());
		assertEquals(persisted.getInputType(), Class.forName(template.getInputType()));
		assertEquals(persisted.getEditor().getDisplayName(), template.getEditor().getDisplayName());
		assertEquals(persisted.getEditor().getUserId(), template.getEditor().getUserId());
		assertEquals(persisted.getEditedAt(), template.getUpdatedAt());

	}

	@Test
	public void lockTemplate() {
		Template<TemplateDto> template = createAndPersistTestTemplate();

		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());

		final User lockingUser = new User("ofr", "oliver");
		ServiceContext.setUser(lockingUser);

		templateService.lockTemplate(dto);

		Template<?> locked = engine.getTemplateRepository().getTemplate("test", "en");
		assertEquals(lockingUser, locked.getLockingUser());
	}

	@Test
	public void unlockTemplate() {
		Template<TemplateDto> template = createAndPersistTestTemplate();
		engine.getTemplateRepository().lockForEdit(template, new User("ofr", "oliver"));

		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());
		dto.setLockingUser(new UserDto());
		dto.getLockingUser().setUserId("ofr");
		dto.getLockingUser().setDisplayName("oliver");

		templateService.unlockTemplate(dto);

		Template<?> unlocked = engine.getTemplateRepository().getTemplate("test", "en");
		assertNull(unlocked.getLockingUser());
	}

	@Test
	public void deleteTemplate() {
		Template<TemplateDto> template = createAndPersistTestTemplate();

		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());
		templateService.deleteTemplate(dto);

		List<Template<Object>> templateList = engine.getTemplateRepository().queryTemplates()
				.documentName(template.getDocumentName())
				.language(template.getLanguage())
				.list();
		assertTrue(templateList.isEmpty());
	}

	@Test
	public void listModelElements() {

		List<ModelElementDto> modelElements = templateService.listModelElements(TemplateDto.class.getName(),
				Collections.<String, String> emptyMap());
		// ): all properties + class "property (getClass()) of Object
		assertEquals(10, modelElements.size());
	}

	@Test
	public void listUniqueContendIds() throws URISyntaxException {
		Path filePath = Paths.get(getClass().getResource("template.docx").toURI());
		List<String> contentIds = templateService.listUniqueContentIds(filePath.toString());
		assertEquals(1, contentIds.size());
		assertEquals("template", contentIds.get(0));
	}

	@Test
	public void saveTemplateData() throws URISyntaxException, IOException {
		Template<TemplateDto> template = createAndPersistTestTemplate();

		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());

		MappingDto mappingDto = new MappingDto();
		mappingDto.setModelKey("documentName");
		mappingDto.setContentControlKey("template");
		dto.getMapping().add(mappingDto);

		Path filePath = Paths.get(getClass().getResource("template.docx").toURI());
		templateService.saveTemplateData(dto, filePath.toString());

		Template<TemplateDto> updatedTemplate = engine.getTemplateRepository().getTemplate("test", "en", TemplateDto.class);
		File updatedTemplateFile = File.createTempFile("updated-template", "docx");
		updatedTemplateFile.deleteOnExit();

		updatedTemplate.write(updatedTemplateFile);
		assertTrue(Arrays.equals(
				Files.readAllBytes(filePath),
				Files.readAllBytes(updatedTemplateFile.toPath())));
	}

	@Test
	public void saveInvalidTemplateData() throws URISyntaxException, IOException {
		Template<TemplateDto> template = createAndPersistTestTemplate();

		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());

		Path filePath = Paths.get(getClass().getResource("template.docx").toURI());
		try {
			templateService.saveTemplateData(dto, filePath.toString());
			fail("Exeption expected");
		} catch (InvalidTemplateServiceException e) {
			assertEquals(1, e.getDetails().size());
			String detail = e.getDetails().get(0);
			assertTrue(detail.startsWith("template"));
		}
	}

	@Test
	public void createTemplateWithoutFile() {
		TemplateDto template = new TemplateDto();
		template.setDocumentName("test");
		template.setLanguage("en");
		template.setInputType(TemplateDto.class.getName());

		TemplateDto created = templateService.createTemplate(template, null);

		assertNotNull(created);
		Template<TemplateDto> templateCreated = engine.getTemplateRepository().getTemplate("test", "en", TemplateDto.class);
		assertNotNull(templateCreated);
	}

	@Test
	public void createTemplateWithFile() throws URISyntaxException {
		TemplateDto dto = new TemplateDto();
		dto.setDocumentName("test");
		dto.setLanguage("en");
		dto.setInputType(TemplateDto.class.getName());

		MappingDto mappingDto = new MappingDto();
		mappingDto.setContentControlKey("template");
		mappingDto.setModelKey("documentName");

		dto.getMapping().add(mappingDto);

		Path filePath = Paths.get(getClass().getResource("template.docx").toURI());

		TemplateDto created = templateService.createTemplate(dto, filePath.toString());

		assertNotNull(created);
		Template<TemplateDto> templateCreated = engine.getTemplateRepository().getTemplate("test", "en", TemplateDto.class);
		assertNotNull(templateCreated);
	}

	@Test
	public void createInvalidTemplateFromFile() throws URISyntaxException {
		TemplateDto template = new TemplateDto();
		template.setDocumentName("test");
		template.setLanguage("en");
		template.setInputType(TemplateDto.class.getName());

		Path filePath = Paths.get(getClass().getResource("template.docx").toURI());
		try {
			templateService.createTemplate(template, filePath.toString());
			fail();
		} catch (InvalidTemplateServiceException e) {
			assertEquals(1, e.getDetails().size());
			String detail = e.getDetails().get(0);
			assertTrue(detail.startsWith("template"));
		}
	}

	private Template<TemplateDto> createAndPersistTestTemplate() {
		Template<TemplateDto> template = engine.getTemplateBuilder(TemplateDto.class)
				.setDocumentName("test")
				.setLanguage("en")
				.setAuthor(new User("userName", "displayName"))
				.build();
		template = engine.getTemplateRepository().persist(template);

		return template;
	}
}
