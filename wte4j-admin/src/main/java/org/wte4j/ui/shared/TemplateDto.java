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
package org.wte4j.ui.shared;

import java.io.Serializable;
import java.util.Date;

public class TemplateDto implements Serializable {

	private String documentName;
	private String language;
	private Date updatedAt;
	private UserDto editor;
	private UserDto lockingUser;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updatedAt = updateAt;
	}

	public UserDto getEditor() {
		return editor;
	}

	public void setEditor(UserDto editor) {
		this.editor = editor;
	}

	public UserDto getLockingUser() {
		return lockingUser;
	}

	public void setLockingUser(UserDto lockingUser) {
		this.lockingUser = lockingUser;
	}
}
