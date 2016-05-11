/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
import java.nio.file.Path;

public interface TemplateEngine {

	/**
	 * Creates a builder for a new templates
	 * 
	 * @param inputType
	 *            - the type of the data passed in to generate a new document
	 * @return a new builder
	 */
	<E> TemplateBuilder<E> getTemplateBuilder(Class<E> inputType);

	/**
	 * Creates a new document as a temporary file. Its up the client to handle
	 * the file.
	 * 
	 * @param documentName
	 *            - name of the document to be generated
	 * @param language
	 *            - the language to be used
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InvalidTemplateException
	 * @throws IOException
	 */
	Path createDocument(String documentName, String language, Object data)
			throws IllegalArgumentException, InvalidTemplateException,
			IOException;

	/**
	 * converts a given docx file in template file
	 * 
	 * @param aFile
	 *            path to the docx file
	 * @return
	 * @throws IOException
	 */
	TemplateFile asTemplateFile(Path aFile) throws IOException;

	/**
	 * Gives acces to the repository.
	 */
	TemplateRepository getTemplateRepository();

}
