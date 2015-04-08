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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class ShowCaseDbInitializerTest {

	ApplicationContext context;

	@Test
	public void initializeDatabase() throws IOException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");

		ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
		HsqlServerBean serverBean = showCaseDbInitializer.createDatabase(directory);

		try {
			serverBean.startDatabase();
			DataSource dataSource = serverBean.createDataSource();
			assertConent(dataSource);
		} finally {
			serverBean.stopDatabase();
			deleteDirectory(directory);
		}
	}

	private void assertConent(DataSource dataSource) {

		JdbcTemplate template = new JdbcTemplate(dataSource);
		long personCount = template.queryForObject("select count(id) from person", Long.class);
		assertEquals(2L, personCount);
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
