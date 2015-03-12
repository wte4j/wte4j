package ch.born.wte.ui.server.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch.born.wte.ui.server.services.ServiceContext;
import ch.born.wte.ui.server.services.SimpleServiceContext;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "ch.born.wte.impl" })
@PropertySource("/WEB-INF/wte.properties")
@Import(StandaloneJPAConfig.class)
public class CoreApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ch.born.wte.ui.shared.Messages");
		return messageSource;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ServiceContext serviceContext() {
		return new SimpleServiceContext();
	}

}
