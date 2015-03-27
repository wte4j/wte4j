package org.wte4j.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.wte4j.ui.server.config.Wte4jAdminConfig;

@Configuration
@EnableTransactionManagement
@PropertySource("WEB-INF/wte4j.properties")
@ImportResource({ "WEB-INF/wte4j-model-service-config.xml", "classpath:wte4j-core-application-context.xml" })
@Import({ StandaloneJPAConfig.class, Wte4jAdminConfig.class })
public class RootApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}