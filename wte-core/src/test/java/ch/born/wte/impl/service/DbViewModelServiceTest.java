package ch.born.wte.impl.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.Template;
import ch.born.wte.TestWithEmbeddedDataSource;
import ch.born.wte.UnknownFormatterException;
import ch.born.wte.User;
import ch.born.wte.WteDataModel;
import ch.born.wte.impl.WordTemplateBuilder;
import ch.born.wte.impl.format.ToStringFormatter;
import static org.junit.Assert.*;

@TransactionConfiguration(transactionManager = "transactionManagerWte")
public class DbViewModelServiceTest extends TestWithEmbeddedDataSource {

	@Autowired
	DataSource ds;

	@Test
	public void testRequiredProperties() throws SQLException {
		SimpleDbViewModelService ms = new SimpleDbViewModelService();
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
		WordTemplateBuilder<Integer> b = new WordTemplateBuilder<Integer>(new SimpleFormatterFactory(),
				ms, Integer.class);

		Map<String, String> modelProperties = new HashMap<String, String>();
		modelProperties.put(SimpleDbViewModelService.VIEW_NAME, "TESTDBVIEW");
		modelProperties.put(SimpleDbViewModelService.PRIMARY_KEY_COLUMN_NAME,
				"ID");
		b.setProperties(modelProperties);
		b.setAuthor(new User("test1", "test2"));

		Template<?> template = b.build();
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
	

	private static class SimpleFormatterFactory implements FormatterFactory {
		@Override
		public Formatter createFormatter(String name, List<String> args)
				throws UnknownFormatterException,
				FormatterInstantiationException {
			return null;
		}

		@Override
		public Formatter createDefaultFormatter(Class<?> type)
				throws FormatterInstantiationException {
			return null;
		}
	}
}
