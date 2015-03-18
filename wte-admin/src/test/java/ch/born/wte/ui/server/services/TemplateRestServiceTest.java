package ch.born.wte.ui.server.services;

import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
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

import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.ui.server.config.RestServiceConfig;

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
				.andExpect(MockMvcResultMatchers.content().contentType("text/html"));
	}

	@Test
	public void submitTemplateNotExists() throws Exception {
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates")
						.file("file", "test".getBytes())
						.param("name", "template")
						.param("language", "de"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html"));

		String content = resultActions.andReturn().getResponse().getContentAsString();
		assertTrue(content.contains(MessageKey.TEMPLATE_NOT_FOUND.getValue()));

	}

	@Test
	@Ignore
	public void submitTemplateToLarge() throws Exception {

		@SuppressWarnings("unchecked")
		Template<Object> template = Mockito.mock(Template.class);
		when(repository.getTemplate("template", "de")).thenReturn(template);

		byte[] bytes = new byte[50002];
		Arrays.fill(bytes, Byte.MIN_VALUE);
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates")
						.file("file", bytes)
						.param("name", "template")
						.param("language", "de"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html"));

		String content = resultActions.andReturn().getResponse().getContentAsString();
		assertTrue(content.contains("done"));
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
