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
package org.wte4j.ui.server.services;

import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wte4j.Template;
import org.wte4j.TemplateRepository;
import org.wte4j.ui.server.config.RestServiceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TemplateRestServiceTest.TestContext.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TemplateRestServiceTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	TemplateRepository repository;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		Mockito.reset(repository);
	}

	@Test
	public void submitTemplateTest() throws Exception {
		@SuppressWarnings("unchecked")
		Template<Object> template = Mockito.mock(Template.class);

		when(repository.getTemplate("template", "de")).thenReturn(template);
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates")
						.file("file", "test".getBytes())
						.param("name", "template")
						.param("language", "de"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	public void submitTemplateNotExists() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates")
						.file("file", "test".getBytes())
						.param("name", "template")
						.param("language", "de"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));

		String content = resultActions.andReturn().getResponse().getContentAsString();
		assertTrue(content.contains(MessageKey.TEMPLATE_NOT_FOUND.getValue()));

	}

	@Test
	public void uploadTempFile() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates/temp")
						.file("file", "test".getBytes()).param("name", "fileName"));

		String content = resultActions.andReturn().getResponse().getContentAsString();

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));

		Pattern pattern = Pattern.compile("\"message\":\"(.+)\"");
		Matcher matcher = pattern.matcher(content);
		assertTrue(matcher.find());
		Path file = Paths.get(matcher.group(1));
		try {
			assertTrue("file " + file + "must exist", Files.exists(file));

		} finally {
			Files.deleteIfExists(file);
		}

	}

	@Configuration
	@Import(RestServiceConfig.class)
	public static class TestContext {

		@Bean
		public TemplateRepository templateRepository() {
			return Mockito.mock(TemplateRepository.class);
		}

		@Bean
		public ServiceContext serviceContext() {
			return Mockito.mock(ServiceContext.class);
		}

		@Bean
		@Qualifier("wte4j-admin")
		public MessageFactory messageFactory() {
			MessageFactory messageFactory = Mockito.mock(MessageFactory.class);
			when(messageFactory.createMessage(anyString())).then(new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					return (String) invocation.getArguments()[0];
				}
			});
			return messageFactory;
		}

		@Bean
		public FileUploadResponseFactory fileUploadResponseFactory() {
			return new FileUploadResponseFactoryImpl();
		}
	}

}
