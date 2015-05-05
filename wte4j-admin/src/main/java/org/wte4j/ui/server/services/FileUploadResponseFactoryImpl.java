/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.wte4j.ui.shared.FileUploadResponseDto;

import com.google.gwt.thirdparty.json.JSONObject;

public class FileUploadResponseFactoryImpl implements FileUploadResponseFactory {

	@Autowired
	@Qualifier("wte4j-admin")
	MessageFactory messageFactory;

	@Override
	public String createJsonSuccessResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createSuccessResponse(messageKey);
		return toJson(response);
	}

	@Override
	public FileUploadResponseDto createSuccessResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createResponse(messageKey);
		response.setDone(true);
		return response;
	}

	@Override
	public String createJsonErrorResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createErrorResponse(messageKey);
		return toJson(response);
	}

	@Override
	public FileUploadResponseDto createErrorResponse(MessageKey messageKey) {
		FileUploadResponseDto response = createResponse(messageKey);
		response.setDone(false);
		return response;
	}

	private FileUploadResponseDto createResponse(MessageKey messageKey) {
		FileUploadResponseDto response = new FileUploadResponseDto();
		String message = messageFactory.createMessage(messageKey.getValue());
		response.setMessage(message);
		return response;
	}

	@Override
	public String toJson(FileUploadResponseDto response) {
		return "{\"done\":" + response.getDone() + ",\"message\":" + JSONObject.quote(response.getMessage()) + "}";
	}
}
