package ch.born.wte.servlet.filter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.util.Assert;

/**
 * This class is used to upload a file to the application.
 * 
 * See the book: "Core Java Server Faces, David Geary" for more details.
 * 
 */
public final class UploadFilter implements Filter {

	/** file size limit */
	private static final String PARAM_NAME_SIZE_THRESHOLD = "ch.born.wte.servlet.filter.UploadFilter.sizeThreshold";

	private static final String PARAM_NAME_REPOSITORY_PATH = "ch.born.wte.servlet.filter.UploadFilter.repositoryPath";

	private int sizeThreshold = -1;
	private String repositoryPath;

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean isMultipartContent = ServletFileUpload
				.isMultipartContent(httpRequest);

		if (!isMultipartContent) {
			chain.doFilter(request, response);
			return;
		}

		// assert that request has multipart content and is httpServeltRequest
		// for the following
		DiskFileItemFactory factory = createFileItemFactory();

		ServletFileUpload upload = new ServletFileUpload(factory);

		List<FileItem> items = null;
		try {
			items = upload.parseRequest(new ServletRequestContext(httpRequest));
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}

		HashMap<String, String[]> parameters = new HashMap<String, String[]>();

		for (FileItem fileItem : items) {
			String itemString = fileItem.getString();
			if (fileItem.isFormField()) {
				parameters.put(fileItem.getFieldName(),
						new String[] { itemString });
			} else {
				httpRequest.setAttribute(fileItem.getFieldName(), fileItem);
			}
		}

		chain.doFilter(new HttpServletRequestWrapperUploadFile(httpRequest,
				parameters), response);

	}

	private DiskFileItemFactory createFileItemFactory() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// configure factory before use

		if (sizeThreshold > 0) {
			factory.setSizeThreshold(sizeThreshold);
		}
		if (null != repositoryPath) {
			factory.setRepository(new File(repositoryPath));
		}
		return factory;
	}

	@Override
	public void init(final FilterConfig config) throws ServletException {
		repositoryPath = config.getInitParameter(PARAM_NAME_REPOSITORY_PATH);
		try {
			String paramValue = config
					.getInitParameter(PARAM_NAME_SIZE_THRESHOLD);
			if (null != paramValue) {
				sizeThreshold = Integer.parseInt(paramValue);
			}

		} catch (NumberFormatException e) {
			throw new ServletException(e);
		}
	}

}

/**
 * This class contains the prepared data for renderers of the uploaded file.
 * 
 */
class HttpServletRequestWrapperUploadFile extends HttpServletRequestWrapper {

	private final Map<String, String[]> parameters;

	/**
	 * 
	 * @throws IllegalArgumentException
	 *             if parameters map is {@code null}.
	 */
	public HttpServletRequestWrapperUploadFile(
			final HttpServletRequest request,
			final Map<String, String[]> parameters) {
		super(request);
		Assert.notNull(parameters);
		this.parameters = parameters;

	}

	@Override
	public String getParameter(final String name) {
		String ret = null;
		String[] values = getParameterValues(name);
		if (null != values) {
			ret = values[0];
		}
		return ret;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameters;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

	@Override
	public String[] getParameterValues(final String name) {
		return parameters.get(name);
	}

}
