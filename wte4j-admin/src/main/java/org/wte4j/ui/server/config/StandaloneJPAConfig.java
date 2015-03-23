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
package org.wte4j.ui.server.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StandaloneJPAConfig {

	@Autowired
	Environment env;

	@Autowired(required = false)
	@Qualifier("wte4j")
	private DataSource externalDataSource;

	@Bean
	@Qualifier("wte4j")
	public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
		emfFactoryBean.setDataSource(lookUpDataSource());
		emfFactoryBean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
		emfFactoryBean.setPersistenceUnitName("wte4j-templates");
		return emfFactoryBean;
	}

	private DataSource lookUpDataSource() {
		if (env.containsProperty("wte4j.jdbc.jndi") || env.containsProperty("wte4j.jdbc.url")) {
			return wteInternalDataSource();
		}
		else {
			return externalDataSource;
		}

	}

	@Bean
	@Lazy
	public DataSource wteInternalDataSource() {
		if (env.containsProperty("wte4j.jdbc.jndi")) {
			JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
			DataSource dataSource = dsLookup.getDataSource(env.getProperty("wte4j.jdbc.jndi"));
			return dataSource;

		}
		else if (env.containsProperty("wte4j.jdbc.url")) {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl(env.getProperty("wte4j.jdbc.url"));
			dataSource.setDriverClassName(env.getProperty("wte4j.jdbc.driver"));
			dataSource.setUsername(env.getProperty("wte4j.jdbc.user"));
			dataSource.setPassword(env.getProperty("wte4j.jdbc.password"));
			return dataSource;

		} else {
			throw new BeanCreationException("NO Datasource defined");
		}

	}

	@Bean
	@Qualifier("wte4j")
	public PlatformTransactionManager wteTransactionManager(@Qualifier("wte4j") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}
}
