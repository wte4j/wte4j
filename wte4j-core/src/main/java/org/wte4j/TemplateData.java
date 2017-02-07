package org.wte4j;

import java.util.Date;
import java.util.Map;

public interface TemplateData {

	String getDocumentName();

	Class<?> getInputType();

	Map<String, String> getProperties();

	String getLanguage();

	User getLockingUser();

	Date getCreatedAt();

	User getEditor();

	Date getEditedAt();

	Map<String, MappingDetail> getContentMapping();

	void setContent(byte[] content, User editor);
	
	byte[] getContent();

}
