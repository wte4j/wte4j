package ch.born.wte.ui.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ch.born.wte.ui.server.services.SpringDocumentController;

@Configuration
@EnableWebMvc
public class AdminApplicationConfig {

	@Bean
	@Value("${wte.fileupload.maxsizeinbytes}")
	public MultipartResolver multipartResolver(long maxUploadSize) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(maxUploadSize);
		return multipartResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ch.born.wte.ui.shared.Messages");
		return messageSource;
	}

	@Bean
	public SpringDocumentController documentController() {
		return new SpringDocumentController();
	}

}
