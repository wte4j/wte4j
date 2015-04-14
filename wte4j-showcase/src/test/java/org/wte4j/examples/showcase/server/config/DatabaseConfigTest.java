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
package org.wte4j.examples.showcase.server.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wte4j.examples.showcase.IntegrationTestApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IntegrationTestApplicationConfig.class })
public class DatabaseConfigTest {

	@Autowired
	DataSource ds;

	@Test
	public void databaseIsIntializedTest() {
		JdbcTemplate template = new JdbcTemplate(ds);
		Set<String> wte4jTables = template.execute(new ConnectionCallback<Set<String>>() {

			@Override
			public Set<String> doInConnection(Connection con) throws SQLException, DataAccessException {
				Set<String> tableNames = new HashSet<String>();
				ResultSet tableRs = con.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
				try {
					while (tableRs.next()) {
						tableNames.add(tableRs.getString("TABLE_NAME").toLowerCase());
					}
				}
				finally {
					tableRs.close();
				}
				return tableNames;
			}
		});
		assertEquals(5, wte4jTables.size());
		assertTrue(wte4jTables.contains("person"));
		assertTrue(wte4jTables.contains("purchase_order"));
		assertTrue(wte4jTables.contains("wte4j_template"));
		assertTrue(wte4jTables.contains("wte4j_template_properties"));
		assertTrue(wte4jTables.contains("wte4j_gen"));
	}

}
