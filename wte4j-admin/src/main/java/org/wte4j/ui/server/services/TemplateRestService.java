/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wte4j.ui.server.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.wte4j.InvalidTemplateException;
import org.wte4j.LockingException;
import org.wte4j.Template;
import org.wte4j.TemplateRepository;
import org.wte4j.WteException;
import org.wte4j.ui.shared.FileUploadResponseDto;

@RestController
@RequestMapping("/templates")
public class TemplateRestService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServiceContext serviceContext;

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

	@RequestMapping(method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
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
			templateRepository.persist(template);
			return fileUploadResponseFactory.createJsonSuccessResponse(MessageKey.TEMPLATE_UPLOADED);
		} catch (IOException e) {
			throw new WteFileUploadException(MessageKey.UPLOADED_FILE_NOT_READABLE);
		}
	}

	/**
	 * processes template file upload and save the file on the server side in
	 * temp folder. returns the path
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "temp", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public String uploadFile(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new WteFileUploadException(MessageKey.UPLOADED_FILE_NOT_READABLE);
		}
		try {
			File tempFile = File.createTempFile(name, ".docx");
			try (OutputStream out = new FileOutputStream(tempFile); InputStream in = file.getInputStream()) {
				IOUtils.copy(in, out);
				FileUploadResponseDto response = new FileUploadResponseDto();
				response.setDone(true);
				response.setMessage(tempFile.toString());
				return fileUploadResponseFactory.toJson(response);
			}
		} catch (IOException e) {
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
