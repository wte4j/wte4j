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
package org.wte4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Defines a template. A template is identified by the documentname and
 * language.
 * 
 * @param <E>
 *            type of the input to generate a document.
 */
public interface Template<E> extends TemplateFile {
	String getDocumentName();

	String getLanguage();

	Map<String, String> getProperties();

	Class<?> getInputType();

	User getLockingUser();

	User getEditor();

	Date getEditedAt();

	Date getCreatedAt();

	/**
	 * Update the content of the template.
	 * 
	 * @param in
	 *            - the new content
	 * @param editor
	 *            - the editor of the content
	 * @throws IOException
	 * @throws LockingException
	 *             if the template is locked by an other user
	 */
	void update(InputStream in, User editor) throws IOException, LockingException;

	public Map<String, MappingDetail> getContentMapping();

	/**
	 * Validates the template against the model and mapping.
	 * 
	 * @throws InvalidTemplateException
	 *             if the template is not valid for the given mapping and model
	 *             definition.
	 */
	public void validate() throws InvalidTemplateException;

	/**
	 * Lists all ids, set in content controls in the template
	 * 
	 * @return
	 */
	public List<String> listContentIds();

	/**
	 * Generates a new document.
	 * 
	 * @param data
	 *            - the data to be filled in the template
	 * @param out
	 *            - writes the generated document to this stream.
	 * @throws IOException
	 * @throws InvalidTemplateException
	 * @throws WteException
	 */
	void toDocument(E data, OutputStream out) throws IOException,
			InvalidTemplateException, WteException;

	/**
	 * Generates a document with dummy content.
	 * 
	 * @param out
	 *            - writes the generated document to this stream. The Stream
	 *            will be closed at the end.
	 * @throws InvalidTemplateException
	 *             if there is a mismatch between model and template
	 * 
	 */
	void toTestDocument(OutputStream out) throws IOException,
			InvalidTemplateException;

	void toPDFDocument(E data, OutputStream out) throws IOException;

}
