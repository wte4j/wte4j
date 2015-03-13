package ch.born.wte.ui.server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.born.wte.InvalidTemplateException;
import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.ui.shared.FileUploadResponse;
import ch.born.wte.ui.shared.FileUploadResponseDto;

@RestController
@RequestMapping("/templates")
public class TemplateRestService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServiceContext serviceContext;

	@Autowired
	private MessageFactory messageFactory;

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

	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public FileUploadResponse updateTemplate(@RequestParam("name") String name, @RequestParam("language") String language,
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
			return new FileUploadResponseDto(true, messageFactory.createMessage(MessageKey.TEMPLATE_UPLOADED.getValue()));
		} catch (IOException e) {
			throw new WteFileUploadException(MessageKey.UPLOADED_FILE_NOT_READABLE,
					e);
		}

	}

	@ExceptionHandler(WteFileUploadException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public FileUploadResponse handleException(WteFileUploadException e) {
		return createErrorResponse(e.getMessageKey());
	}

	@ExceptionHandler(LockingException.class)
	@ResponseStatus(value = HttpStatus.LOCKED)
	public FileUploadResponse handleException(LockingException e) {
		return createErrorResponse(MessageKey.LOCKED_TEMPLATE);
	}

	@ExceptionHandler(InvalidTemplateException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@RequestMapping(produces = "application/json")
	public FileUploadResponse handleException(InvalidTemplateException e) {
		return createErrorResponse(MessageKey.UPLOADED_FILE_NOT_VALID);
	}

	@ExceptionHandler(Exception.class)
	// @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<FileUploadResponse> handleException(Exception e) {
		logger.error("error on processing request", e);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		return new ResponseEntity<FileUploadResponse>(createErrorResponse(MessageKey.INTERNAL_SERVER_ERROR), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private FileUploadResponse createErrorResponse(MessageKey messageKey) {
		FileUploadResponseDto response = new FileUploadResponseDto();
		response.setDone(false);
		String message = messageFactory.createMessage(messageKey.getValue());
		response.setMessage(message);
		return response;
	}

}
