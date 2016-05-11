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
package org.wte4j.examples.showcase.server.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.service.OrderService;

import com.google.common.io.Files;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtOrderServiceServlet extends RemoteServiceServlet implements OrderService {
	private static final long serialVersionUID = 1L;

	private static final String DOCUMENT_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	@Autowired
	OrderService orderService;

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public List<OrderDataDto> listOrderData() {
		return orderService.listOrderData();
	}

	public List<String> listOrderTemplates() {
		return orderService.listOrderTemplates();
	}

	public List<String> listDataModel() {
		return orderService.listDataModel();
	}

	@Override
	public String createDocument(OrderDataDto order, String documentName) {
		return orderService.createDocument(order, documentName);
	}

	private void sendDocument(File file, HttpServletResponse resp) throws IOException {
		resp.setContentType(DOCUMENT_CONTENT_TYPE);
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName());
		Files.copy(file, resp.getOutputStream());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileParameter = req.getParameter("file");
		if (StringUtils.isNotEmpty(fileParameter)) {
			File file = new File(fileParameter);
			sendDocument(file, resp);
			file.delete();
		}
		else
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

}
