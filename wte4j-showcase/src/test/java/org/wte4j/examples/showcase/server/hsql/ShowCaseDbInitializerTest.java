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
