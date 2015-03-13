package ch.born.wte.ui.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import ch.born.wte.ui.server.services.RestExceptionHandler;
import ch.born.wte.ui.server.services.TemplateRestService;

@Configuration
@Import(PropertiesConfig.class)
public class RestServiceConfig extends WebMvcConfigurationSupport {

	@Bean
	@Value("${wte.fileupload.maxsizeinbytes}")
	public MultipartResolver multipartResolver(long maxUploadSize) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(maxUploadSize);
		return multipartResolver;
	}

	@Bean
	public TemplateRestService templateRestService() {
		return new TemplateRestService();
	}

	@Bean
	public RestExceptionHandler restExceptionHandler() {
		return new RestExceptionHandler();
	}

}
