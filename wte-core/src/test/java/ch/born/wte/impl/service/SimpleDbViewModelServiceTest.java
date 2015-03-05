package ch.born.wte.impl.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ch.born.wte.Template;
import ch.born.wte.WteDataModel;

public class SimpleDbViewModelServiceTest {

	EmbeddedDatabase ds;

	@Before
	public void init() {
		ds = new EmbeddedDatabaseBuilder()
				.addScript("classpath:/sql/wte_test_dbView.sql")
				.setType(EmbeddedDatabaseType.HSQL).build();
	}

	@After
	public void destroy() {
		ds.shutdown();
	}

	@Test
	public void testRequiredProperties() throws SQLException {
		SimpleDbViewModelService ms = new SimpleDbViewModelService(ds);
		List<String> list = ms.listRequiredModelProperties();
		assertTrue(list.size() == 2);
	}

	@Test
	public void testModelElements() throws SQLException {
		SimpleDbViewModelService ms = new SimpleDbViewModelService(ds);
		Map<String, String> modelProperties = new HashMap<String, String>();
		modelProperties.put(SimpleDbViewModelService.VIEW_NAME, "TESTDBVIEW");
		modelProperties.put(SimpleDbViewModelService.PRIMARY_KEY_COLUMN_NAME,
				"ID");
		Map<String, Class<?>> list = ms
				.listModelElements(null, modelProperties);
		assertTrue(list.size() > 0);
	}

	@Test
	public void testModel() throws SQLException {
		SimpleDbViewModelService ms = new SimpleDbViewModelService(ds);

		@SuppressWarnings("unchecked")
		Template<Integer> template = mock(Template.class);

		Map<String, String> modelProperties = new HashMap<String, String>();
		modelProperties.put(SimpleDbViewModelService.VIEW_NAME, "TESTDBVIEW");
		modelProperties.put(SimpleDbViewModelService.PRIMARY_KEY_COLUMN_NAME,
				"ID");
		when(template.getProperties()).thenReturn(modelProperties);

		when(template.getInputType()).then(new Answer<Class<?>>() {

			@Override
			public Class<?> answer(InvocationOnMock invocation)
					throws Throwable {
				return Integer.class;
			}
		});

		WteDataModel model = ms.createModel(template, new Integer(1));
		assertNotNull(model.getValue("id"));
		assertNotNull(model.getValue("testboolean"));
		assertNotNull(model.getValue("testvarchar255"));
		assertNotNull(model.getValue("testtimestamp"));
		assertNotNull(model.getValue("testtime"));
		assertNotNull(model.getValue("testdate"));
		assertNotNull(model.getValue("testchar255"));
		assertNotNull(model.getValue("testlongvarchar255"));
		assertNotNull(model.getValue("testtinyint"));
		assertNotNull(model.getValue("testsmallint"));
		assertNotNull(model.getValue("testint"));
		assertNotNull(model.getValue("testreal"));
		assertNotNull(model.getValue("testfloat"));
		assertNotNull(model.getValue("testnumeric"));
		assertNotNull(model.getValue("testdecimal"));
	}

	
}
