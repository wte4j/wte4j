package ch.born.wte.ui.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
	public void updateTemplate(@RequestParam("name") String name, @RequestParam String locale,
			@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			Template<?> template = templateEngine.getTemplateRepository().getTemplate(name, locale);
			User editor = new User("admin", "admin");
			try (InputStream in = file.getInputStream()) {
				template.update(in, editor);
			} catch (IOException e) {
				throw new WteException(e);
			}
		}

	}

}
