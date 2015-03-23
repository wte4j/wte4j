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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.wte4j.ui.server.services.RestExceptionHandler;
import org.wte4j.ui.server.services.TemplateRestService;

@Configuration
@Import(PropertiesConfig.class)
public class RestServiceConfig extends WebMvcConfigurationSupport {

	@Bean
	@Value("${wte4j.fileupload.maxsizeinbytes}")
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
