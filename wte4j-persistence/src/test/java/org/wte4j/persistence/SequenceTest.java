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
package org.wte4j.persistence;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.wte4j.EmbeddedDataBaseWithSequenceConfig;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.User;
import org.wte4j.WteModelService;
import org.wte4j.impl.TemplateContextFactory;
import org.wte4j.impl.WordTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EmbeddedDataBaseWithSequenceConfig.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class SequenceTest  {
	@PersistenceContext
	EntityManager entityManager;
	TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
	WteModelService modelService = mock(WteModelService.class);
	WordTemplateRepository repository;

	@Before
	public void initTest() throws Exception {
		repository = new WordTemplateRepository(entityManager, contextFactory);
	}

	@Test
	@Transactional
	public void persistNewTemplate() {
		WordTemplate<?> template = newTemplate();
		repository.persist(template);
		Template<?> persisted = repository.getTemplate("test3", "de");
		assertTrue(isVersionIncontext(persisted));
	}
	
	private WordTemplate<?> newTemplate() {
		try {
			PersistentTemplate templateData = new PersistentTemplate();
			templateData.setDocumentName("test3");
			templateData.setLanguage("de");
			templateData.setContent(getContent("empty.docx"));
			templateData.setCreatedAt(new Date());
			templateData.setEditedAt(new Date());
			templateData.setEditor(new User("user", "user"));

			Map<String, String> properties = new HashMap<>();
			properties.put("key", "value");
			templateData.setProperties(properties);

			MappingDetail value = new MappingDetail();
			value.setModelKey("modelKey");

			Map<String, MappingDetail> contentMaping = new HashedMap<String, MappingDetail>();
			contentMaping.put("conentKey", value);

			templateData.setContentMapping(contentMaping);

			WordTemplate<?> template = new WordTemplate<Object>(templateData, contextFactory);
			return template;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] getContent(String file) throws IOException {
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream(file);
			return IOUtils.toByteArray(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	PersistentTemplate getTemplateInContext(long id) {
		return entityManager.find(PersistentTemplate.class, id);
	}
	
	private boolean isVersionIncontext(Template<?> template) {
		WordTemplate<?> wordTemplate = (WordTemplate<?>) template;
		PersistentTemplate templateData = (PersistentTemplate) wordTemplate.getTemplateData();
		PersistentTemplate inContext = getTemplateInContext(templateData.getId());
		if (inContext == null) {
			return false;
		}
		return inContext.getVersion() == templateData.getVersion();
	}

}

