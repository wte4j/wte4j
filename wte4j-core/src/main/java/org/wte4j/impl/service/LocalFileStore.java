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
package org.wte4j.impl.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.wte4j.FileStore;

public class LocalFileStore implements FileStore {

	private File templateDirectory;

	public LocalFileStore() {
		templateDirectory = new File(System.getProperty("user.dir")
				+ File.pathSeparator + "templates");
	}

	public void setTemplateDirectory(File templateDirectory) {
		this.templateDirectory = templateDirectory;
	}

	@Override
	public OutputStream getOutStream(String fileName) throws IOException {
		File templateFile = new File(templateDirectory, fileName);
		if (!templateFile.exists()) {
			templateFile.createNewFile();
		}
		return FileUtils.openOutputStream(templateFile);
	}

	@Override
	public void deleteFile(String fileName) throws IllegalArgumentException {
		File templateFile = new File(templateDirectory, fileName);
		if (!templateFile.exists()) {
			throw new IllegalArgumentException("file " + fileName);
		}
		templateFile.delete();
	}

}
