package org.wte4j.examples.showcase.server.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.wte4j.Template;
import org.wte4j.TemplateRepository;
import org.wte4j.examples.showcase.IntegrationTestApplicationConfig;
import org.wte4j.examples.showcase.server.MessageKey;
import org.wte4j.examples.showcase.server.config.WebContextTestExecutionListener;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.TemplateManagerServiceException;
import org.wte4j.examples.showcase.shared.service.TemplateManagerService;
import org.wte4j.ui.server.services.MessageFactory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IntegrationTestApplicationConfig.class })
@TestExecutionListeners({WebContextTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class})
public class TemplateManagerServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private TemplateManagerService templateManagerService;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	@Qualifier("wte4j-showcase")
	private MessageFactory messageFactory;
	
	@Test
	public void testListDataModel() {
		List<String> dataModels = templateManagerService.listDataModel();
		
		assertNotNull(dataModels);
		assertEquals(dataModels.size(),1);
	}

	@Test
	public void testCreateTemplate_NoError() {
		String testTemplateName = "testClass";
		String language = "en";
		String testClassDataModel = OrderDataDto.class.getCanonicalName();
		templateManagerService.createTemplate(testClassDataModel, testTemplateName);
		Template template = templateRepository.getTemplate(testTemplateName, language);
		assertNotNull(template);
		assertEquals(testTemplateName, template.getDocumentName());
		assertEquals(testClassDataModel, template.getInputType().getCanonicalName());
	}
	
	@Test
	public void testCreateTemplate_ClassNotFound() {
		String testTemplateName = "testClass";
		String testClassDataModel = "noclass";
		boolean didFireException = false;
		try {
			templateManagerService.createTemplate(testClassDataModel, testTemplateName);
		} catch (TemplateManagerServiceException templateManagerServiceException) {
			didFireException = true;
			assertEquals(templateManagerServiceException.getMessage(),messageFactory.createMessage(MessageKey.DATA_MODEL_NOT_FOUND.getValue()));
		}
		assertTrue(didFireException);
	}

	@Test
	public void testCreateTemplate_TemplateExists() {
		String testTemplateName = "testClass";
		String testClassDataModel = OrderDataDto.class.getCanonicalName();
		templateManagerService.createTemplate(testClassDataModel, testTemplateName);
		
		boolean didFireException = false;
		try {
			templateManagerService.createTemplate(testClassDataModel, testTemplateName);
		} catch (TemplateManagerServiceException templateManagerServiceException) {
			didFireException = true;
			assertEquals(templateManagerServiceException.getMessage(),messageFactory.createMessage(MessageKey.TEMPLATE_EXISTS_ALREADY.getValue()));
		}
		assertTrue(didFireException);
	}
}
