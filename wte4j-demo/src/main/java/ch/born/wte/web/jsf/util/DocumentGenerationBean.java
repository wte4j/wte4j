package ch.born.wte.web.jsf.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public final class DocumentGenerationBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DocumentGenerationBean.class);

	@Autowired
	TemplateBean templateBean;

	public void generate(final Object domainInstance) {

		FacesContext context = FacesContext.getCurrentInstance();
		Object response = context.getExternalContext().getResponse();
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// set content for word files
		httpServletResponse.setContentType("application/msword");

		String propsosedClientFileName = templateBean.getSelectedTemplate()
				.getDocumentName();
		httpServletResponse.setHeader("Content-Disposition", String.format(
				"attachment;filename=\"%s\"", propsosedClientFileName));

		try {
			OutputStream out = httpServletResponse.getOutputStream();
			templateBean.getSelectedTemplate().toDocument(domainInstance, out);
			out.flush();
		} catch (IOException e) {
			FacesMessageHelper.createFacesMessage(context,
					FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}

		// lifecycle is finished
		FacesContext.getCurrentInstance().responseComplete();
	}

}
