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
package org.wte4j.ui.server.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wte4j.User;
import org.wte4j.ui.server.services.ServiceContext;

public class SessionContextFilter implements Filter {

	private static final String ANONYMOUS_USER = "anonymous";

	private ServiceContext serviceContext;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			serviceContext.setLocale(httpRequest.getLocale());

			if (httpRequest.getUserPrincipal() != null) {
				String userName = httpRequest.getUserPrincipal().getName();
				serviceContext.setUser(new User(userName, userName));
			}
			else {
				serviceContext.setUser(new User(ANONYMOUS_USER, ANONYMOUS_USER));
			}

		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		serviceContext = WebApplicationContextUtils.
				getRequiredWebApplicationContext(filterConfig.getServletContext()).
				getBean(ServiceContext.class);
	}

}
