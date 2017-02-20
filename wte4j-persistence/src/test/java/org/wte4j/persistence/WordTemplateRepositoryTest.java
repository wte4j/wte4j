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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.wte4j.EmbeddedDataBaseConfig;
import org.wte4j.FileStore;
import org.wte4j.LockingException;
import org.wte4j.MappingDetail;
import org.wte4j.Template;
import org.wte4j.TemplateQuery;
import org.wte4j.User;
import org.wte4j.WteModelService;
import org.wte4j.impl.TemplateContextFactory;
import org.wte4j.impl.WordTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EmbeddedDataBaseConfig.class })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class WordTemplateRepositoryTest {
	private static final long LOCKED_TEMPLATE = 2;
	private static final long UNLOCKED_TEMPLATE = 1;
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
	public void testQueryTemplates() {
		TemplateQuery query = repository.queryTemplates();
		List<Template<Object>> templates = query.list();
		assertEquals(4, templates.size());
	}

	@Test
	@Transactional
	public void testQueryWithEmptyResults() {
		TemplateQuery query = repository.queryTemplates();
		List<Template<Object>> templates = query.language("xxx").list();
		assertEquals(0, templates.size());
	}

	@Test
	@Transactional
	public void getTemplate() {
		Template<?> template = repository.getTemplate("test1", "en");
		assertNotNull(template);
	}

	@Test
	@Transactional
	public void getNonExistingTemplate() {
		Template<?> template = repository.getTemplate("XXXX", "XX");
		assertNull(template);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void getTemplateWithWrongInputType() {
		repository.getTemplate("test1", "en", Date.class);
		fail("Exception expected");
	}

	@Test
	@Transactional
	public void locking() {
		WordTemplate<?> template = unlockedTemplate();
		User lockingUser = new User("locking", "Locking User");
		Template<?> locked = repository.lockForEdit(template, lockingUser);
		assertEquals(lockingUser, locked.getLockingUser());
		assertTrue(isVersionIncontext(locked));
	}

	@Test
	@Transactional
	public void lockingLockedTemplate() {
		WordTemplate<?> toLock = lockedTemplate();
		User second = new User("second", "Second User");
		try {
			repository.lockForEdit(toLock, second);
			fail(LockingException.class + " expected");
		} catch (LockingException e) {
			assertTrue(isVersionIncontext(toLock));
		}
	}

	@Test(expected = LockingException.class)
	@Transactional
	public void lockingTemplateParallel() {
		Template<?> template1 = unlockedTemplate();
		Template<?> template2 = unlockedTemplate();
		template1 = repository.lockForEdit(template1, new User("first", "First"));
		User second = new User("second", "Second User");
		repository.lockForEdit(template2, second);
		fail(LockingException.class + " expected");
	}

	@Test
	@Transactional
	public void lockingTwice() {
		Template<?> template = unlockedTemplate();
		User user = new User("user", "User");
		template = repository.lockForEdit(template, user);
		template = repository.lockForEdit(template, user);
		assertEquals(user, template.getLockingUser());
	}

	@Test
	@Transactional
	public void unlock() {
		WordTemplate<?> template = lockedTemplate();
		Template<?> unlocked = repository.unlock(template);
		assertNull(unlocked.getLockingUser());
		assertTrue(isVersionIncontext(unlocked));
	}

	@Test
	@Transactional
	public void persistNewTemplate() {
		WordTemplate<?> template = newTemplate();
		repository.persist(template);
		Template<?> persisted = repository.getTemplate("test3", "de");
		assertTrue(isVersionIncontext(persisted));
	}

	@Test
	@Transactional
	public void persistTemplateWithFileStore() throws Exception {
		WordTemplate<?> template = newTemplate();
		FileStore fileStore = mock(FileStore.class);
		File file = File.createTempFile("content", "docx");
		OutputStream out = FileUtils.openOutputStream(file);
		when(fileStore.getOutStream(anyString())).thenReturn(out);
		repository.setFileStore(fileStore);
		try {
			repository.persist(template);
			File epected = FileUtils.toFile(getClass().getResource("empty.docx"));
			assertTrue(FileUtils.contentEquals(epected, file));
		} finally {
			file.deleteOnExit();
		}
	}

	@Test
	@Transactional
	public void persistOfLocked() throws Exception {
		Template<?> template1 = unlockedTemplate();
		WordTemplate<?> template2 = unlockedTemplate();
		template1 = repository.lockForEdit(template1, new User("user1", "user1"));
		byte[] content = getContent("empty.docx");
		template2.getTemplateData().setContent(content, new User());
		try {
			repository.persist(template2);
			fail("Exception expected");
		} catch (LockingException e) {
			assertTrue(isVersionIncontext(template1));
		}
	}

	@Test
	@Transactional
	public void changeContent() throws Exception {
		WordTemplate<?> template = unlockedTemplate();
		byte[] content = getContent("empty.docx");
		User user = new User("new_editor", "New Editor");
		template.update(new ByteArrayInputStream(content), user);
		Template<?> peristed = repository.persist(template);
		assertTrue(isVersionIncontext(peristed));
	}

	@Test
	@Transactional
	public void updateTemplateWithFileStore() throws Exception {
		FileStore fileStore = mock(FileStore.class);
		File file = File.createTempFile("content", "docx");
		OutputStream out = FileUtils.openOutputStream(file);
		when(fileStore.getOutStream(anyString())).thenReturn(out);
		repository.setFileStore(fileStore);
		WordTemplate<?> template = unlockedTemplate();
		template.getTemplateData().setContent(getContent("empty.docx"), new User());
		try {
			repository.persist(template);
			File expected = FileUtils.toFile(getClass().getResource("empty.docx"));
			assertTrue(FileUtils.contentEquals(expected, file));
		} finally {
			file.deleteOnExit();
		}
	}

	@Test
	@Transactional
	public void deleteTemplate() {
		WordTemplate<?> template = unlockedTemplate();
		repository.delete(template);
		assertFalse(isVersionIncontext(template));
	}

	@Test
	@Transactional
	public void deleteLockedTemplate() {
		WordTemplate<?> template = lockedTemplate();
		try {
			repository.delete(template);
			fail("Exception expected. Locked templates must not be deleted");
		} catch (LockingException e) {
			assertTrue(isVersionIncontext(template));
		}
	}

	@Test
	@Transactional
	public void delteTemplateWithFileStore() throws Exception {
		FileStore fileStore = mock(FileStore.class);
		repository.setFileStore(fileStore);
		WordTemplate<?> template = unlockedTemplate();
		PersistentTemplate templateData = (PersistentTemplate) template.getTemplateData();
		repository.delete(template);
		verify(fileStore, times(1)).deleteFile(anyString());
		verify(fileStore, times(1)).deleteFile(templateData.getTemplateFileName());
		assertFalse("template must not be in persistent context", isVersionIncontext(template));
	}

	@Test
	@Transactional
	public void errorsWithFileStore() throws Exception {
		WordTemplate<?> template = unlockedTemplate();
		FileStore fileStore = mock(FileStore.class);
		repository.setFileStore(fileStore);
		EntityManager entityManager = mock(EntityManager.class);
		when(entityManager.merge(any())).thenThrow(new PersistenceException());
		repository.em = entityManager;
		try {
			repository.persist(template);
			fail("Exception expected");
		} catch (Exception e) {
			verifyZeroInteractions(fileStore);
		}
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

	/**
	 * Returns a detached locked template
	 */
	private WordTemplate<?> lockedTemplate() {
		PersistentTemplate template = getTemplateInContext(LOCKED_TEMPLATE);
		entityManager.detach(template);
		return new WordTemplate<Object>(template, contextFactory);
	}

	/**
	 * Returns a detached unlocked template
	 */
	private WordTemplate<?> unlockedTemplate() {
		PersistentTemplate template = getTemplateInContext(UNLOCKED_TEMPLATE);
		entityManager.detach(template);
		return new WordTemplate<Object>(template, contextFactory);
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
