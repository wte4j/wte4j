package ch.born.wte.ui.server.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;

import ch.born.wte.User;
import ch.born.wte.ui.server.services.ServiceContext;

public class SessionContextFilter implements Filter {

	private ServiceContext serviceContext;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			if (httpRequest.getUserPrincipal() != null) {
				String userName = httpRequest.getUserPrincipal().getName();
				if (userName != null) {
					serviceContext.setUser(new User(userName, userName));
				}
				serviceContext.setLocale(httpRequest.getLocale());
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
