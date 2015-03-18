package ch.born.wte.ui.server.services;

import org.springframework.beans.factory.annotation.Autowired;

import ch.born.wte.ui.shared.FileUploadResponseDto;

public class FileUploadResponseFactoryImpl implements FileUploadResponseFactory {

	@Autowired
	MessageFactory messageFactory;
	
	@Override
	public FileUploadResponseDto createErrorResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createResponse(messageKey);
		response.setDone(false);
		return response;
	}
	
	@Override
	public FileUploadResponseDto createSuccessResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createResponse(messageKey);
		response.setDone(true);
		return response;
	}
	
	private FileUploadResponseDto createResponse(MessageKey messageKey) {
		FileUploadResponseDto response = new FileUploadResponseDto();
		String message = messageFactory.createMessage(messageKey.getValue());
		response.setMessage(message);
		return response;
	}

	@Override
	public String createJsonSuccessResponse(MessageKey messageKey) {
		return createSuccessResponse(messageKey).toJson();
	}
	
	@Override
	public String createJsonErrorResponse(MessageKey messageKey) {
		return createErrorResponse(messageKey).toJson();
	}
}
