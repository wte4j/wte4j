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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.wte4j.WteModelService;
import org.wte4j.impl.service.FlatBeanModelService;
import org.wte4j.persistence.config.Wte4jPersistenceConfig;
import org.wte4j.ui.server.config.Wte4jAdminConfig;
import org.wte4j.ui.server.services.MessageFactory;
import org.wte4j.ui.server.services.MessageFactoryImpl;
import org.wte4j.ui.server.services.ServiceContext;

@Configuration
@Import({Wte4jAdminConfig.class,Wte4jPersistenceConfig.class})
public class Wte4jConfiguration {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ServiceContext serviceContext;
	
	@Bean
	@Qualifier("wte4j")
	public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
		emfFactoryBean.setDataSource(dataSource);
		emfFactoryBean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
		emfFactoryBean.setPersistenceUnitName("wte4j-templates");
		return emfFactoryBean;
	}
	
	@Bean
	@Qualifier("wte4j-showcase")
	public MessageFactory messageFactoryShowcase() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("org.wte4j.examples.showcase.shared.Messages");
		return new MessageFactoryImpl(messageSource, serviceContext);
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
