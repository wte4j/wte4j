package ch.born.wte;

import java.util.List;
import java.util.Map;

/**
 * Interface for querying Templates in the repository.
 */
public interface TemplateQuery {

	TemplateQuery documentName(String name);

	TemplateQuery language(String language);

	TemplateQuery inputType(Class<?> clazz);

	TemplateQuery editor(String userId);

	TemplateQuery isLocked(boolean value);

	TemplateQuery isLockedBy(String userId);

	TemplateQuery hasProperties(Map<String, String> properties);

	List<Template<Object>> list();
}
