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
package ch.born.wte.ui.server.services;

public enum MessageKey {
	INTERNAL_SERVER_ERROR,
	LOCKED_TEMPLATE,
	TEMPLATE_NOT_FOUND,
	TEMPLATE_UPLOADED,
	TEMPLATE_CLASS_NOT_FOUND,
	UPLOADED_FILE_NOT_READABLE,
	UPLOADED_FILE_NOT_VALID, 
	UPLOADED_FILE_TOO_LARGE; 

	private static final String WTE4J_MESSAGE_BASE = "wte4j.message.";
	private final String value;

	private MessageKey() {
		value = WTE4J_MESSAGE_BASE + this.name().toLowerCase();
	}

	public String getValue() {
		return value;
	}
}
