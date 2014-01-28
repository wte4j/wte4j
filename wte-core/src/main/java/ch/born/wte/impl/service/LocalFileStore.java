package ch.born.wte.impl.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import ch.born.wte.FileStore;

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
