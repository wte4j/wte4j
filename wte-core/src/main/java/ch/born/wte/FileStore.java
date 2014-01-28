package ch.born.wte;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 */
public interface FileStore {

	OutputStream getOutStream(String fileName) throws IOException;

	void deleteFile(String fileName) throws IllegalArgumentException;
}
