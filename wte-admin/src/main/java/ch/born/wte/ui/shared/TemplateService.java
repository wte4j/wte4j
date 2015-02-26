package ch.born.wte.ui.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("templateService")
public interface TemplateService extends RemoteService {
	List<TemplateDto> getTemplates();

	TemplateDto lockTemplate(TemplateDto template);

	TemplateDto unlockTemplate(TemplateDto template);

	void deleteTemplate(TemplateDto template);
}
