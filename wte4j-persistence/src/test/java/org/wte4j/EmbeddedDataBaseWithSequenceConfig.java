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
package org.wte4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

@Configuration
public class EmbeddedDataBaseWithSequenceConfig {

	EmbeddedDatabase database;

	@PostConstruct
	public void init() {
		database = new EmbeddedDatabaseBuilder()
				.setName(this.toString())
				.setType(EmbeddedDatabaseType.HSQL).ignoreFailedDrops(true)
				.addScript("classpath:sql/create-sequence-schema.sql")
				.build();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(){
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setPersistenceUnitName("wte4j-templates");
		localContainerEntityManagerFactoryBean.setDataSource(dataSource());
		
		OpenJpaVendorAdapter openJpaVendorAdapter = new OpenJpaVendorAdapter();
		openJpaVendorAdapter.setDatabase(Database.HSQL);
		
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(openJpaVendorAdapter);
		
		localContainerEntityManagerFactoryBean.setPersistenceXmlLocation("classpath:test-sequence-persistence.xml");
		return localContainerEntityManagerFactoryBean;
	}
	
	@Bean
	public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf){
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		
		jpaTransactionManager.setEntityManagerFactory(emf);
		return jpaTransactionManager;
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
