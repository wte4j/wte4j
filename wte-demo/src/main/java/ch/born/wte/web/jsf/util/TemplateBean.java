package ch.born.wte.web.jsf.util;

import ch.born.wte.Template;
import ch.born.wte.TemplateBuilder;
import ch.born.wte.TemplateEngine;
import ch.born.wte.TemplateRepository;
import ch.born.wte.User;
import ch.born.wte.common.domain.Domain;
import ch.born.wte.data.provider.domain.organizations.OrganizationRootAdapter;
import ch.born.wte.data.provider.domain.persons.PersonRootAdapter;
import ch.born.wte.impl.service.SimpleDbViewModelService;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.richfaces.component.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

/**
 * This class is a standard spring backing bean for the template view.
 * 
 */
@Component("templateBean")
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public class TemplateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8156287838232861813L;

	private static final Logger LOGGER = Logger.getLogger(TemplateBean.class);

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private TemplateRepository repository;

	private Template<Object> selectedTemplate;

	private final SortOrder tagSorting = SortOrder.ascending;

	private final SortOrder templateSorting = SortOrder.ascending;

	private String documentName;

	public String select(final Template<Object> template) {

		selectedTemplate = template;

		String nextPage = "";

		if (template.getInputType().equals(PersonRootAdapter.class)) {
			nextPage = "/domain/person/dataSelectionPersons.xhtml";

		} else if (template.getInputType()
				.equals(OrganizationRootAdapter.class)) {
			nextPage = "/domain/organization/dataSelectionOrganizations.xhtml";
		}
		return nextPage;
	}

	public void delete(final Template<?> template) {

		repository.delete(template);

	}

	public void createNewTemplate(Domain domain) {

		Class<?> inputClass = null;
		if (domain.equals(Domain.PERSON)) {
			inputClass = PersonRootAdapter.class;
		} else {
			inputClass = OrganizationRootAdapter.class;
		}
		TemplateBuilder<?> builder = templateEngine
				.getTemplateBuilder(inputClass);

		Map<String, String> modelProperties = new HashMap<String, String>();
		modelProperties.put(SimpleDbViewModelService.VIEW_NAME,
				"PERSONROOTADAPTERVIEW");
		modelProperties.put(SimpleDbViewModelService.PRIMARY_KEY_COLUMN_NAME,
				"PARTY_ID");
		builder.setProperties(modelProperties);

		builder.setDocumentName(documentName);
		builder.setLanguage("de");
		builder.setAuthor(new User("demo", "demo"));
		Template<?> template = builder.build();
		repository.persist(template);
	}

	/**
	 * This method sends the template document to the client.
	 */
	public void download(final Template template) {
		downloadInternal(template, FacesContext.getCurrentInstance());
	}

	void downloadInternal(final Template template, final FacesContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format(
					"Download template called for template '%s' ",
					template.toString()));
		}

		Object response = context.getExternalContext().getResponse();

		if (!isHttpServletResponse(response)) {
			LOGGER.warn("Could not send file to client, current response is not a http servlet response.");
		} else {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			sendTemplateToClient(template, httpServletResponse);

			// jsf lifecycle is finished
			context.responseComplete();
		}

	}

	/**
	 * This method sends the given template to the client be writing to the
	 * response.
	 * 
	 */
	private void sendTemplateToClient(final Template<?> template,
			final HttpServletResponse httpServletResponse) {

		httpServletResponse.setHeader(
				"Content-Disposition",
				String.format("attachment;filename=\"%s\"",
						template.getDocumentName() + template.getLanguage()));
		// set content for word files
		httpServletResponse.setContentType("application/msword");

		try {
			template.write(httpServletResponse.getOutputStream());
		} catch (IOException e1) {
			throw new FaceletException(e1);
		}

	}

	private boolean isHttpServletResponse(final Object response) {
		return response instanceof HttpServletResponse;
	}

	public List<Template<Object>> getGenerationTemplates() {
		return repository.queryTemplates().list();
	}

	public List<Template<Object>> getAllTemplatesOrganization() {
		return repository.queryTemplates()
				.inputType(OrganizationRootAdapter.class).list();
	}

	public List<Template<Object>> getAllTemplatesPerson() {
		return repository.queryTemplates().inputType(PersonRootAdapter.class)
				.list();
	}

	public Template<Object> getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(Template<Object> selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public SortOrder getTagSorting() {
		return tagSorting;
	}

	public SortOrder getTemplateSorting() {
		return templateSorting;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

}
