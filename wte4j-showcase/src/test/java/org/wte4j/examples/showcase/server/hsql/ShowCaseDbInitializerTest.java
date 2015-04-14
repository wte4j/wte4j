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
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class ShowCaseDbInitializerTest {

	ApplicationContext context;

	@Test
	public void createDatabaseFiles() throws IOException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");
		try {
			ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
			showCaseDbInitializer.createDateBaseFiles(directory, false);

			Set<String> fileNamesInDirectory = listFiles(directory);

			Set<String> expectedFileNames = new HashSet<String>();
			expectedFileNames.add("wte4j-showcase.lobs");
			expectedFileNames.add("wte4j-showcase.properties");
			expectedFileNames.add("wte4j-showcase.script");
			assertEquals(expectedFileNames, fileNamesInDirectory);
		} finally {
			deleteDirectory(directory);
		}
	}

	@Test
	public void createDatabaseFilesWithoutOveride() throws IOException, SQLException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");
		Path dummyFile = directory.resolve("wte4j-showcase.script");
		Files.createFile(dummyFile);
		try {
			ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
			showCaseDbInitializer.createDateBaseFiles(directory, false);

			Set<String> fileNamesInDirectory = listFiles(directory);
			Set<String> expectedFileNames = new HashSet<String>();
			expectedFileNames.add("wte4j-showcase.script");
			assertEquals(expectedFileNames, fileNamesInDirectory);
			assertEquals(0, Files.size(dummyFile));
		} finally {
			deleteDirectory(directory);
		}
	}

	@Test
	public void createDatabaseFilesWithOveride() throws IOException, SQLException {
		ApplicationContext context = new StaticApplicationContext();
		Path directory = Files.createTempDirectory("database");
		Path dummyFile = directory.resolve("wte4j-showcase.script");
		Files.createFile(dummyFile);

		try {

			ShowCaseDbInitializer showCaseDbInitializer = new ShowCaseDbInitializer(context);
			showCaseDbInitializer.createDateBaseFiles(directory, true);

			Set<String> fileNamesInDirectory = listFiles(directory);
			Set<String> expectedFileNames = new HashSet<String>();
			expectedFileNames.add("wte4j-showcase.lobs");
			expectedFileNames.add("wte4j-showcase.properties");
			expectedFileNames.add("wte4j-showcase.script");
			assertEquals(expectedFileNames, fileNamesInDirectory);
			assertTrue(Files.size(dummyFile) > 0);

		} finally {
			deleteDirectory(directory);
		}
	}

	private Set<String> listFiles(Path directory) throws IOException {
		Set<String> fileNamesInDirectory = new HashSet<String>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
			for (Path path : directoryStream) {
				fileNamesInDirectory.add(path.getFileName().toString());
			}
		}
		return fileNamesInDirectory;
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
