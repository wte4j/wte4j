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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.wte4j.ui.server.services.FileUploadResponseFactory;
import org.wte4j.ui.server.services.FileUploadResponseFactoryImpl;
import org.wte4j.ui.server.services.MessageFactory;
import org.wte4j.ui.server.services.MessageFactoryImpl;
import org.wte4j.ui.server.services.ServiceContext;
import org.wte4j.ui.server.services.SimpleServiceContext;

@Configuration
public class Wte4jAdminConfig {

	@Bean
	public MessageFactory messageFactory() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("org.wte4j.ui.shared.Messages");
		return new MessageFactoryImpl(messageSource);
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
