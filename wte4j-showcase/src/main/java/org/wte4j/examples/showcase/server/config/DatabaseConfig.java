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

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.wte4j.examples.showcase.server.hsql.HsqlServerBean;
import org.wte4j.examples.showcase.server.hsql.ShowCaseDbInitializer;

@Configuration
public class DatabaseConfig {

	public static final Path DATABASE_DIRECTORY = Paths.get(System.getProperty("java.io.tmpdir"), "hsql");
	public static final String DATABASE_NAME = "wte4j-showcase";

	@Autowired
	private ResourcePatternResolver resourceloader;

	@Bean
	@Qualifier("wte4j")
	public DataSource dataSource() {
		return hsqlServer().createDataSource();
	}

	@Bean
	public HsqlServerBean hsqlServer() {
		ShowCaseDbInitializer dbInitializer = new ShowCaseDbInitializer(resourceloader);
		return dbInitializer.createDatabase(DATABASE_DIRECTORY);
	}
}
