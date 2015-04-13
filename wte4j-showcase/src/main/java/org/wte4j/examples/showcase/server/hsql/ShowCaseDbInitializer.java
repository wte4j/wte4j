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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ShowCaseDbInitializer {

	private ResourcePatternResolver resourceLoader;

	public ShowCaseDbInitializer(ResourcePatternResolver resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Initialize Hslq Database with name wte4-showcase
	 * 
	 * @param dataBaseLocation
	 *            - path in the file system where the hsql database file shall
	 *            be stored
	 * @param overide
	 *            - if true and the database file exists the files will be
	 *            replaced
	 * @return
	 */
	public HsqlServerBean createDatabase(Path dataBaseLocation, boolean overide) {
		Path hsqlScript = dataBaseLocation.resolve("wte4j-showcase.script");
		if (overide || !Files.exists(hsqlScript)) {
			try {
				if (!Files.exists(dataBaseLocation)) {
					Files.createDirectory(dataBaseLocation);
				}
				Resource[] resources = resourceLoader.getResources("classpath:/db/*");
				for (Resource resource : resources) {
					Path filePath = dataBaseLocation.resolve(resource.getFilename());
					File source = resource.getFile();
					Files.copy(source.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("can not copy database files", e);
			}
		}
		return new HsqlServerBean(dataBaseLocation, "wte4j-showcase");
	}

	public static void main(String... args) {
		ShowCaseDbInitializer initializer = new ShowCaseDbInitializer(new StaticApplicationContext());
		boolean overide = false;
		if (args.length == 2) {
			overide = Boolean.parseBoolean(args[1]);
		}
		HsqlServerBean hsqlServerBean = initializer.createDatabase(Paths.get(args[0]), overide);
		hsqlServerBean.startDatabase();
	}

}
