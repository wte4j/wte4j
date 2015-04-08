package org.wte4j.examples.showcase.server.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.wte4j.WteModelService;
import org.wte4j.impl.service.SimpleDbViewModelService;

public class Wte4jConfiguration {

	@Autowired
	private DataSource dataSource;

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
	@Qualifier("wte4j")
	public PlatformTransactionManager wteTransactionManager(@Qualifier("wte4j") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	WteModelService wteModelService() {
		return new SimpleDbViewModelService(dataSource);
	}

}
