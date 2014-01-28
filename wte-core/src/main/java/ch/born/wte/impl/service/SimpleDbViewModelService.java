package ch.born.wte.impl.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import ch.born.wte.Template;
import ch.born.wte.WteDataModel;
import ch.born.wte.WteException;
import ch.born.wte.WteModelService;

/**
 * Implementation of WteModelService for a model which is retrieving its data
 * from a database view.<br/>
 * A client using this class must provide the properties VIEW_NAME and
 * PRIMARY_KEY_COLUMN_NAME. The primary key column must be of integral type. To
 * retrieve a row from the view the method <code>createModel(..)</code> is used:
 * parameter <code>input</code> must contain the primary key of the row and must
 * be of type Integer.
 * 
 * @author trytz
 * 
 */
public class SimpleDbViewModelService implements WteModelService {

	public static final String VIEW_NAME = "viewName";
	public static final String PRIMARY_KEY_COLUMN_NAME = "pkColumnName";

	@Autowired
	protected DataSource ds;

	protected SimpleDbViewModelService() {
	}

	public SimpleDbViewModelService(DataSource aDs) {
		ds = aDs;
	}

	@Override
	public Map<String, Class<?>> listModelElements(Class<?> inputClass,
			Map<String, String> properties) {
		Map<String, Class<?>> elements = new HashMap<String, Class<?>>();
		String viewName = properties.get(VIEW_NAME);
		Connection connection = null;
		ResultSet rs = null;
		try {
			try {
				connection = ds.getConnection();
				DatabaseMetaData metaData = ds.getConnection().getMetaData();
				rs = metaData.getColumns(null, null, viewName, null);

				while (rs.next()) {
					String columnName = rs.getString("COLUMN_NAME")
							.toLowerCase();
					Class<?> type = MapperSqlType.map(rs.getInt("DATA_TYPE"));
					elements.put(columnName, type);
				}

			} finally {
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new WteException("error in view " + viewName, e);
		}
		return elements;
	}

	@Override
	public List<String> listRequiredModelProperties() {
		List<String> list = new ArrayList<String>();
		list.add(VIEW_NAME);
		list.add(PRIMARY_KEY_COLUMN_NAME);
		return list;
	}

	@Override
	public WteDataModel createModel(Template<?> template, Object input) {
		String viewName = template.getProperties().get(VIEW_NAME);
		String pkColumnName = template.getProperties().get(
				PRIMARY_KEY_COLUMN_NAME);
		Integer pk = (Integer) input;
		Map<String, Object> dataMap = new HashMap<String, Object>(); //TODO PreparedStatement!
		String query = "select * from " + viewName + " where " + pkColumnName
				+ "=" + pk;
		try {
			Connection connection = null;
			Statement statement = null;
			try {
				connection = ds.getConnection();
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query);
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnName(i)
								.toLowerCase();
						dataMap.put(columnName, rs.getObject(i));
					}
				}

			} finally {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new WteException("error in createModel (" + viewName + ", "
					+ pkColumnName + ", " + pk + ")", e);
		}
		return new WteMapModel(dataMap);
	}

}
