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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
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

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment env;

	@Autowired(required = false)
	@Qualifier("wte4j")
	private DataSource externalDataSource;

	private boolean hsqlServerIsRunning = false;

	@Bean
	@Qualifier("wte4j")
	public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
		emfFactoryBean.setDataSource(lookUpDataSource());
		emfFactoryBean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
		emfFactoryBean.setPersistenceUnitName("wte4j-templates");
		emfFactoryBean.setJpaPropertyMap(createJpaPropertyMap());
		return emfFactoryBean;
	}

	private Map<String, ?> createJpaPropertyMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		return map;
	}

	private DataSource lookUpDataSource() {
		if (externalDataSource != null) {
			logger.info("external datasource found with qualifier \"wte4j\" using this DataSource for wte4j");
			return externalDataSource;
		}
		else {

			return wteInternalDataSource();
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
			logger.info("no database configured");
			logger.info("try to start hsql database");
			hsqlServer().start();
			hsqlServerIsRunning = true;
			logger.info("hsql server started");

			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl("jdbc:hsqldb:hsql://localhost/wte4j");
			dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
			dataSource.setUsername("sa");
			return dataSource;
		}

	}

	@Bean
	@Lazy
	public Server hsqlServer() {
		Path path = Paths.get(env.getProperty("java.io.tmpdir"), "hsql", "wte4j");
		String pathAsString = path.toAbsolutePath().toString();
		logger.info("dblocation: {}", pathAsString);

		HsqlProperties p = new HsqlProperties();
		p.setProperty("server.database.0", "file:" + pathAsString);
		p.setProperty("server.dbname.0", "wte4j");
		try {
			Server server = new Server();
			server.setProperties(p);
			return server;
		} catch (Exception e) {
			throw new BeanInitializationException("can not creat hsql server bean", e);
		}
	}

	@Bean
	@Qualifier("wte4j")
	public PlatformTransactionManager wteTransactionManager(@Qualifier("wte4j") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@PreDestroy
	public void stopHsqlServer() {
		if (hsqlServerIsRunning) {
			hsqlServer().stop();
		}
	}
}
