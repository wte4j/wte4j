package ch.born.wte.ui.server.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ch.born.wte.ui.shared.FileUploadResponse;
import ch.born.wte.ui.shared.FileUploadResponseDto;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FileUploadResponse handleException(final RuntimeException e, WebRequest request) {
		handleExceptionInternal(e, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
		return new FileUploadResponseDto(false, "INTERNAL_EXCEPTION" + e.getClass());
	}
}
