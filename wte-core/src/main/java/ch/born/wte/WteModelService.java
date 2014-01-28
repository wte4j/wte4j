package ch.born.wte;

import java.util.List;
import java.util.Map;

/**
 * The engine requires a WteModelService to generate documents and create new
 * templates with the suported tags for a given model configuration.
 */
public interface WteModelService {

	/**
	 * Returns a list of dynamic content elements available for the given
	 * properties and input type.
	 */
	Map<String, Class<?>> listModelElements(Class<?> inputClass,
			Map<String, String> properties);

	/**
	 * Lists the property names required to create models (
	 * {@link #createModel(Template, Object)})and list the the model elements(
	 * {@link #listModelElements(Class, Map)}).
	 */
	List<String> listRequiredModelProperties();

	/**
	 * Wraps the given input in a {@link WteDataModel}. This model is used to
	 * get the content to be filled in the template.
	 */
	WteDataModel createModel(Template<?> template, Object input);
}
