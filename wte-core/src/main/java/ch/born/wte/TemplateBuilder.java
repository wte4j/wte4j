package ch.born.wte;

import java.util.Map;

public interface TemplateBuilder<E> {

	TemplateBuilder<E> setDocumentName(String name);

	TemplateBuilder<E> setLanguage(String languageCode);

	TemplateBuilder<E> setAuthor(User user);

	TemplateBuilder<E> setProperties(Map<String, String> properties);

	/**
	 * 
	 * @return a valid template containing all the elements provided by 
	 * {@linkplain WteModelService WteModelService.createModel(Template&lt;?&gt;, Object)}
	 * @throws TemplateBuildException
	 */
	Template<E> build() throws TemplateBuildException;
}
