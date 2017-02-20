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
package org.wte4j.ui.server.services;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.wte4j.WteModelService;
import org.wte4j.impl.service.FlatBeanModelService;
import org.wte4j.persistence.config.Wte4jPersistenceConfig;
import org.wte4j.ui.server.config.Wte4jAdminConfig;

@Configuration
@Import({Wte4jAdminConfig.class,Wte4jPersistenceConfig.class})
@ImportResource({ "classpath:wte4j-core-application-context.xml" })
@EnableTransactionManagement
public class IntegrationTestConfiguration {

	@Bean
	@Qualifier("wte4j")
	public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
		emfFactoryBean.setDataSource(dataSource());
		emfFactoryBean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
		emfFactoryBean.setPersistenceXmlLocation("classpath:/test-persistence.xml");
		emfFactoryBean.setPersistenceUnitName("wte4j-templates");
		emfFactoryBean.setJpaPropertyMap(createJpaPropertyMap());
		return emfFactoryBean;
	}

	private Map<String, ?> createJpaPropertyMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		return map;
	}

	@Bean
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).setName("wte-admin-integration").build();
	}

	@Bean
	@Qualifier("wte4j")
	public PlatformTransactionManager wteTransactionManager(@Qualifier("wte4j") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	WteModelService wteModelService() {
		return new FlatBeanModelService();
	}
}
