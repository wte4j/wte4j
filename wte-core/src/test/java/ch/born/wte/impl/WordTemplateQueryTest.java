package ch.born.wte.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ch.born.wte.EmbeddedDataBaseConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EmbeddedDataBaseConfig.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class WordTemplateQueryTest  {

	@PersistenceContext
	EntityManager entityManager;

	@Test	
	public void queryAll() {
		List<PersistentTemplate> result = query().list(entityManager);
		assertEquals(4, result.size());
	}

	@Test	
	public void queryDocumentName() {
		List<PersistentTemplate> result = query().documentName("test1").list(
				entityManager);
		assertEquals(1, result.size());
		assertEquals("test1", result.get(0).getDocumentName());
	}

	@Test	
	public void queryLanguage() {
		WordTemplateQuery query = query().language("en");
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		assertEquals("en", result.get(0).getLanguage());
	}

	@Test	
	public void queryInputType() {

		WordTemplateQuery query = query().inputType(Integer.class);
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		assertEquals(Integer.class, result.get(0).getInputType());
	}

	@Test	
	public void queryEditor() {
		WordTemplateQuery query = query().editor("test_user");
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		assertEquals("test_user", result.get(0).getEditor().getUserId());
	}

	@Test	
	public void queryLocked() {
		WordTemplateQuery query = query().isLocked(true);
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		assertEquals(true, result.get(0).isLocked());
	}

	@Test	
	public void queryNotLocked() {
		WordTemplateQuery query = query().isLocked(false);
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(3, result.size());
		assertEquals(false, result.get(0).isLocked());
	}

	@Test	
	public void queryLockedBy() {
		WordTemplateQuery query = query().isLockedBy("test_user");
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		assertEquals("test_user", result.get(0).getLockingUser().getUserId());
	}

	@Test	
	public void queryProperty() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		WordTemplateQuery query = query().hasProperties(properties);
		List<PersistentTemplate> result = query.list(entityManager);
		assertEquals(1, result.size());
		PersistentTemplate template = result.get(0);
		assertTrue(template.getProperties().entrySet()
				.containsAll(properties.entrySet()));
	}

	private WordTemplateQuery query() {
		return new WordTemplateQuery(null, entityManager.getCriteriaBuilder());
	}

}
