package ch.born.wte;

import java.io.File;
import java.io.IOException;

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
	File createDocument(String documentName, String language, Object data)
			throws IllegalArgumentException, InvalidTemplateException,
			IOException;

	/**
	 * Gives acces to the repository.
	 */
	TemplateRepository getTemplateRepository();

}
