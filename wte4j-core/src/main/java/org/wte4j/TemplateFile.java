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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface TemplateFile {

	/**
	 * Writes the content of this File to an output stream and closes the
	 * stream.
	 * 
	 * @param out
	 *            - the stream to write to content to.
	 * @throws IOException
	 */
	void write(OutputStream out) throws IOException;

	/**
	 * Writes the content of this file to a given File location
	 * 
	 * @param file
	 *            - the file
	 * @throws IOException
	 */
	void write(File file) throws IOException;

	/**
	 * Lists all ids, set in content controls in the template document
	 * 
	 * @return
	 */
	public List<String> listContentIds();
}
