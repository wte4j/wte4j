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

import java.io.File;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.wte4j.examples.showcase.server.hsql.HsqlServerBean;
import org.wte4j.examples.showcase.server.hsql.ShowCaseDbInitializer;

@Configuration
public class DatabaseConfig {

	public static final String RESET_DATABSE_ENV_PROPERTY = "org.wte4j.examples.showcase.RESET_DATABSE";
	public static final String DATABASE_DIRECTORY_ENV_PROPERTY = "org.wte4j.examples.showcase.DATABASE_DIRECTORY";
	private static final File DEFAULT_DATABASE_DIRECTORY = Paths.get(System.getProperty("java.io.tmpdir"), "hsql").toFile();

	@Autowired
	private ResourcePatternResolver resourceloader;

	@Autowired
	private Environment env;

	@Bean
	@Qualifier("wte4j")
	public DataSource dataSource() {
		return hsqlServer().createDataSource();
	}

	@Bean
	public HsqlServerBean hsqlServer() {
		boolean overide = env.getProperty(RESET_DATABSE_ENV_PROPERTY, Boolean.class, Boolean.FALSE);
		File directory = env.getProperty(DATABASE_DIRECTORY_ENV_PROPERTY, File.class, DEFAULT_DATABASE_DIRECTORY);
		ShowCaseDbInitializer dbInitializer = new ShowCaseDbInitializer(resourceloader);
		return dbInitializer.createDatabase(directory.toPath(), overide);
	}
}
