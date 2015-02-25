package ch.born.wte.ui.server;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends BaseServlet {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TemplateRepository templateRepository;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			sendTemplate(request, response);
		} catch (Exception e) {
			logger.error("can not read output dir", e);
		}
	}

	void sendTemplate(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String documentName = request.getParameter("documentName");
		String language = request.getParameter("language");
		Template<?> template = templateRepository.getTemplate(documentName,
				language);

		String fileName = template.getDocumentName() + template.getLanguage()
				+ ".docx";
		String mimetype = getServletConfig().getServletContext().getMimeType(
				fileName);

		response.setContentType((mimetype != null) ? mimetype
				: "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		OutputStream out = response.getOutputStream();
		template.write(out);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
