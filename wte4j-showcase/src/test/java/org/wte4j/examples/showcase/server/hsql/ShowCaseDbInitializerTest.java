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
package org.wte4j.examples.showcase.server.hsql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

public class ShowCaseDbInitializerTest {

	ApplicationContext context;

	@Test
	public void initializeDatabase() throws IOException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");

		ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
		HsqlServerBean serverBean = showCaseDbInitializer.createDatabase(directory, false);

		try {
			serverBean.startDatabase();
			DataSource dataSource = serverBean.createDataSource();
			assertTables(dataSource);
		} finally {
			serverBean.stopDatabase();
			deleteDirectory(directory);
		}
	}

	@Test
	public void initializeDatabaseWithOveride() throws IOException, SQLException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");

		ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
		HsqlServerBean serverBean = showCaseDbInitializer.createDatabase(directory, false);

		try {
			serverBean.startDatabase();
			DataSource dataSource = serverBean.createDataSource();
			JdbcTemplate template = new JdbcTemplate(dataSource);
			template.execute("drop table PURCHASE_ORDER");
			serverBean.stopDatabase();
			BasicDataSource basicDataSource = (BasicDataSource) dataSource;
			basicDataSource.close();

			serverBean = showCaseDbInitializer.createDatabase(directory, true);
			serverBean.startDatabase();
			dataSource = serverBean.createDataSource();
			assertTables(dataSource);
		} finally {
			serverBean.stopDatabase();
			deleteDirectory(directory);
		}
	}

	private void assertTables(DataSource dataSource) {

		JdbcTemplate template = new JdbcTemplate(dataSource);
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

	private void deleteDirectory(Path path) throws IOException {

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				Files.deleteIfExists(file);
				return super.visitFile(file, attrs);
			}
		});
		Files.deleteIfExists(path);

	}
}
