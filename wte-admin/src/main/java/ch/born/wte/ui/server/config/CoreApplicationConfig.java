package ch.born.wte.ui.server.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch.born.wte.ui.server.services.FileUploadResponseFactory;
import ch.born.wte.ui.server.services.FileUploadResponseFactoryImpl;
import ch.born.wte.ui.server.services.MessageFactory;
import ch.born.wte.ui.server.services.MessageFactoryImpl;
import ch.born.wte.ui.server.services.ServiceContext;
import ch.born.wte.ui.server.services.SimpleServiceContext;

@Configuration
@EnableTransactionManagement
@Import({ StandaloneJPAConfig.class, PropertiesConfig.class })
@ImportResource({ "WEB-INF/wte4j-model-service-config.xml", "classpath:wte4j-core-application-context.xml" })
public class CoreApplicationConfig {

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ch.born.wte.ui.shared.Messages");
		return messageSource;
	}

	@Bean
	public MessageFactory messageFactory() {
		return new MessageFactoryImpl();
	}

	@Bean
	public FileUploadResponseFactory fileUploadResponseFactory() {
		return new FileUploadResponseFactoryImpl();
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ServiceContext serviceContext() {
		return new SimpleServiceContext();
	}

}
