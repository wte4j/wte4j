package ch.born.wte.ui.server.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {

	@Autowired
	FileUploadResponseFactory fileUploadResponseFactory;
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e) {
		logger.warning("MaxUploadSizeExceededException: " + e.getLocalizedMessage());
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.UPLOADED_FILE_TOO_LARGE);
	}
	
	@Order(Ordered.LOWEST_PRECEDENCE)
	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(final RuntimeException e) {
		logger.warning("RuntimeException: " + e.getLocalizedMessage());
		return fileUploadResponseFactory.createJsonErrorResponse(MessageKey.INTERNAL_SERVER_ERROR);
	}
}
