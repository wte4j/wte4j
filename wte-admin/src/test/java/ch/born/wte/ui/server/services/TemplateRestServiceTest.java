package ch.born.wte.ui.server.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.born.wte.TemplateRepository;
import ch.born.wte.ui.server.config.RestServiceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TemplateRestServiceTest.TestContext.class })
public class TemplateRestServiceTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void submitTemplateTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/templates")
						.file("file", "test".getBytes())
						.param("name", "template")
						.param("language", "de"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
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
			return Mockito.mock(MessageFactory.class);
		}

	}

}
