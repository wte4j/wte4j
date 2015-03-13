package ch.born.wte.ui.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ch.born.wte.ui.server.services.TemplateRestService;

@Configuration
@EnableWebMvc
@Import(PropertiesConfig.class)
public class RestServiceConfig {
	
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

}
