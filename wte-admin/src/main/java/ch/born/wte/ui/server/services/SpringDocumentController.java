package ch.born.wte.ui.server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.born.wte.Template;
import ch.born.wte.TemplateEngine;
import ch.born.wte.User;
import ch.born.wte.WteException;

@RestController
@RequestMapping("/templates")
public class SpringDocumentController {

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
	public Map<String,String> updateTemplate(@RequestParam("name") String name, @RequestParam("language") String language,
			@RequestParam("file") MultipartFile file) {
		Map<String,String> response = new HashMap<String,String>();
		if (!file.isEmpty()) {
			Template<?> template = templateEngine.getTemplateRepository().getTemplate(name, language);
			User editor = new User("admin", "admin");
			if (template != null) {
				try (InputStream in = file.getInputStream()) {
					template.update(in, editor);
					response.put("result", "wte.message.fileupload.ok");
				} catch (IOException e) {
					response.put("error", "wte.message.fileupload.err.inputstream");
				}
			} else {
				response.put("error", "wte.message.fileupload.err.missingtemplate");
			}
		} else {
			response.put("error", "wte.message.fileupload.err.missingfile");
		}
		return response;
	}

}
