package ch.born.wte;

import java.util.List;

public interface FormatterFactory {

	/**
	 * Creates a new formatter instance
	 * 
	 * @param name
	 *            - the name of a formatter
	 * @param args
	 *            - a list of args to construct the formatter
	 * @return a new formatter instance
	 * @throws UnknownFormatterException
	 *             - if the formatter with the given name does not exists
	 * @throws InstantiationException
	 *             -if for the given arguments the formatter can not be build
	 */

	Formatter createFormatter(String name, List<String> args)
			throws UnknownFormatterException, FormatterInstantiationException;

	/**
	 * Creates a new formatter instance for a given type.
	 * 
	 * @throws FormatterInstantiationException
	 *             if a new Instance can not be created.
	 */
	Formatter createDefaultFormatter(Class<?> type)
			throws FormatterInstantiationException;

}