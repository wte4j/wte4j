package ch.born.wte.ui.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {

	@Autowired
	private FileUploadResponseFactory fileUploadResponseFactory;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e, WebRequest request) {
		logger.warn("Uploaded file to big from {}: ", request.getRemoteUser(), e);
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.UPLOADED_FILE_TOO_LARGE);
	}

	@Order(Ordered.LOWEST_PRECEDENCE)
	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(final RuntimeException e, NativeWebRequest request) {
		logger.error("error on processing request from {} ", request.getRemoteUser(), e);
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.INTERNAL_SERVER_ERROR);
	}
}
