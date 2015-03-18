package ch.born.wte.ui.server.services;

import ch.born.wte.ui.shared.FileUploadResponse;

public interface FileUploadResponseFactory {

	public abstract FileUploadResponse createErrorResponse(MessageKey messageKey);

	public abstract FileUploadResponse createSuccessResponse(
			MessageKey messageKey);

	public abstract String createJsonSuccessResponse(MessageKey templateUploaded);

	public abstract String createJsonErrorResponse(MessageKey messageKey);

}