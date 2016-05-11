/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ImportResource({ "classpath:test-persistence-context.xml" })
public class EmbeddedDataBaseConfig {

	EmbeddedDatabase database;

	@PostConstruct
	public void init() {
		database = new EmbeddedDatabaseBuilder()
				.setName(this.toString())
				.setType(EmbeddedDatabaseType.HSQL).ignoreFailedDrops(true)
				.addScript("classpath:sql/create-schema.sql")
				.addScript("classpath:sql/wte4j_template.sql")
				.addScript("classpath:sql/wte4j_template_properties.sql").build();
	}

	@Bean
	public DataSource dataSource() {
		return database;
	}

	@PreDestroy
	public void shutdown() {
		database.shutdown();
		database = null;
	}

}
