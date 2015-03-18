package ch.born.wte.ui.server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.born.wte.InvalidTemplateException;
import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.WteException;

@RestController
@RequestMapping("/templates")
public class TemplateRestService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServiceContext serviceContext;

	@Autowired
	private MessageFactory messageFactory;
	
	@Autowired
	private FileUploadResponseFactory fileUploadResponseFactory;

	@Autowired
	private TemplateRepository templateRepository;

	@RequestMapping(method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	public byte[] getTemplate(@RequestParam String name, @RequestParam String language, HttpServletResponse response)
			throws Exception {
		byte[] documentContent = null;
		Template<?> template = templateRepository.getTemplate(name, language);
		if (template != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			template.write(out);
			documentContent = out.toByteArray();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + template.getDocumentName() + ".docx\"");
		}
		return documentContent;
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String updateTemplate(@RequestParam("name") String name, @RequestParam("language") String language,
			@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			throw new WteFileUploadException(MessageKey.UPLOADED_FILE_NOT_READABLE);
		}

		Template<?> template =
				templateRepository.getTemplate(name, language);
		if (template == null) {
			throw new WteFileUploadException(MessageKey.TEMPLATE_NOT_FOUND);
		}

		try (InputStream in = file.getInputStream()) {
			template.update(in, serviceContext.getUser());
			return fileUploadResponseFactory.createJsonSuccessResponse(MessageKey.TEMPLATE_UPLOADED);
		} catch(IOException e) {
			throw new WteFileUploadException(MessageKey.UPLOADED_FILE_NOT_READABLE);
		}
	}
	
	@ExceptionHandler(WteFileUploadException.class)
	public String handleException(WteFileUploadException e) {
		return fileUploadResponseFactory.createJsonErrorResponse(e.getMessageKey());
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public String handleException(IllegalStateException e) {
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.TEMPLATE_CLASS_NOT_FOUND);
	}
	
	@ExceptionHandler(WteException.class)
	public String handleException(WteException e) {
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.UPLOADED_FILE_NOT_VALID);
	}

	@ExceptionHandler(LockingException.class)
	public String handleException(LockingException e) {
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.LOCKED_TEMPLATE);
	}

	@ExceptionHandler(InvalidTemplateException.class)
	public String handleException(InvalidTemplateException e) {
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.UPLOADED_FILE_NOT_VALID);
	}

	@Order(Ordered.LOWEST_PRECEDENCE)
	@ExceptionHandler(RuntimeException.class)
	public String handleException(RuntimeException e) {
		logger.error("error on processing request", e);
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.INTERNAL_SERVER_ERROR);
	}
}
