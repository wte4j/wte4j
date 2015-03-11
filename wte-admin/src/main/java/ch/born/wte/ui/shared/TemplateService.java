package ch.born.wte.ui.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("templateService")
public interface TemplateService extends RemoteService {
	List<TemplateDto> getTemplates() throws TemplateServiceException;;

	TemplateDto lockTemplate(TemplateDto template) throws TemplateServiceException;

	TemplateDto unlockTemplate(TemplateDto template) throws TemplateServiceException;;

	void deleteTemplate(TemplateDto template) throws TemplateServiceException;
}
