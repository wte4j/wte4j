package ch.born.wte.web.jsf.util;

import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.User;
import ch.born.wte.web.upload.UploadRenderer;

import java.io.File;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

/**
 * This class is a standard spring backing bean for the upload template view.
 * 
 */

@Component("uploadTemplateBean")
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public final class UploadTemplateBean implements Serializable {

	private static final String UPLOAD_RENDERER_RENDERER_TYPE = "ch.born.wte.web.upload";

	private static final String UPLOAD_RENDERER_COMPONENT_FAMILY = "javax.faces.Input";

	/**
	 * 
	 */
	private static final long serialVersionUID = 5463351484653093107L;

	private static final Logger LOGGER = Logger
			.getLogger(UploadTemplateBean.class);
	@Autowired
	TemplateRepository templateService;

	private Template<?> template;

	private FileItem uploadedFile;

	public String select(Template<?> aTemplate) {
		template = aTemplate;
		return "/upload/person/uploadPersonTemplate.xhtml?faces-redirect=true";
	}

	public String submitUploadNewTemplateOrganization() {
		if (updateTemplate()) {
			return "/domain/organization/templatesOrganizations.xhtml?faces-redirect=true";
		} else {
			return "";
		}

	}

	public String submitUploadNewTemplatePerson() {
		if (updateTemplate()) {
			return "/domain/person/templatesPersons.xhtml?faces-redirect=true";

		} else {
			return "";
		}

	}

	/**
	 * Handles the button submit event for uploading, returns the next
	 * navigation target.
	 * 
	 * <b> Attention!</b> Reading of the template file and creation of the new
	 * template DAO object will be done by the {@link UploadRenderer}
	 * 
	 * @param organization
	 * 
	 * @return the new navigation target
	 */
	public boolean updateTemplate() {

		boolean retOk = false;

		initUploadedFile();
		// Integer newTemplateId = UploadHelper.getNewDocumentTemplateId();
		if (null != uploadedFile) {

			try {
				template.update(uploadedFile.getInputStream(), new User("Hans",
						"Meier"));
				templateService.persist(template);
				retOk = true;
			} catch (Exception e) {
				LOGGER.error(e);
				FacesContext ctx = FacesContext.getCurrentInstance();
				FacesMessage fMsg = FacesMessageHelper.createFacesMessage(ctx,
						FacesMessage.SEVERITY_ERROR,
						"defaulTemplateUploadError", uploadedFile.getName());

				ctx.addMessage(null, fMsg);
			}
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			FacesMessage fMsg = FacesMessageHelper.createFacesMessage(ctx,
					FacesMessage.SEVERITY_ERROR, "templateUploadError");

			ctx.addMessage(null, fMsg);
		}

		return retOk;

	}

	private void initUploadedFile() {
		Renderer upRend = FacesContext
				.getCurrentInstance()
				.getRenderKit()
				.getRenderer(UPLOAD_RENDERER_COMPONENT_FAMILY,
						UPLOAD_RENDERER_RENDERER_TYPE);

		// secure cast
		if (upRend instanceof UploadRenderer) {
			UploadRenderer uploadRenderer = (UploadRenderer) upRend;
			uploadedFile = uploadRenderer.getUploadedFile();

		} else {
			throw new IllegalArgumentException(
					"UploadRenderer Reference is invalid.");
		}

	}

	/**
	 * This method removes all unnecessary paths to return the pure filename.
	 * Remark: Internet Explorer is giving the whole absolute path as file name
	 * 
	 */
	private String getPureFileName(final FileItem fileItem) {
		File file = new File(fileItem.getName());
		int lastDot = file.getName().lastIndexOf(".");
		return file.getName().substring(0, lastDot);
	}

}
