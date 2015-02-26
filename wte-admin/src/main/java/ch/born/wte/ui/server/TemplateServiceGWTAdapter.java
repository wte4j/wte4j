package ch.born.wte.ui.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class TemplateServiceGWTAdapter extends RemoteServiceServlet implements TemplateService {	

	@Autowired
	private TemplateService templateService;
	
	public void init(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);
	}

	public List<TemplateDto> getTemplates() {
		return templateService.getTemplates();
	}

	public TemplateDto lockTemplate(TemplateDto template) {
		return templateService.lockTemplate(template);
	}

	public TemplateDto unlockTemplate(TemplateDto template) {
		return templateService.unlockTemplate(template);
	}

	public void deleteTemplate(TemplateDto template) {
		templateService.deleteTemplate(template);
	}

	

}