package ch.born.wte.ui.shared;

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
