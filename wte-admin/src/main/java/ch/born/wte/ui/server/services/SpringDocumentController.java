package ch.born.wte.ui.server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.born.wte.Template;
import ch.born.wte.TemplateEngine;

@RestController
@RequestMapping("/WteAdmin/documents/templates")
public class SpringDocumentController {

	@Autowired
	private ServiceContext serviceContext;

	@Autowired
	private TemplateEngine templateEngine;

	@RequestMapping(method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	public @ResponseBody byte[] getTemplate(@RequestParam String name, @RequestParam String language)
			throws Exception {
		byte[] documentContent = null;
		Template<?> template = templateEngine.getTemplateRepository().getTemplate(name, language);
		if (template != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			template.write(out);
			documentContent = out.toByteArray();
		}
		return documentContent;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Map<String, String> updateTemplate(@RequestParam("name") String name, @RequestParam("language") String language,
			@RequestParam("file") MultipartFile file) {
		Map<String, String> response = new HashMap<String, String>();
		if (!file.isEmpty()) {
			Template<?> template = templateEngine.getTemplateRepository().getTemplate(name, language);

			if (template != null) {
				try (InputStream in = file.getInputStream()) {
					template.update(in, serviceContext.getUser());
					response.put("result", "wte.message.fileupload.ok");
				} catch (IOException e) {
					throw new WteFileUploadException("wte.message.fileupload.err.inputstream", e);
				}
			} else {
				throw new WteFileUploadException("wte.message.fileupload.err.missingtemplate");
			}
		} else {
			throw new WteFileUploadException("wte.message.fileupload.err.missingfile");
		}
		return response;
	}

	@ExceptionHandler(WteFileUploadException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleException(WteFileUploadException e) {
		Map<String, String> response = new HashMap<String, String>();
		response.put("error", e.getMessage());
		return response;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleException(Exception e) {
		Map<String, String> response = new HashMap<String, String>();
		response.put("error", e.getMessage());
		return response;
	}

}
