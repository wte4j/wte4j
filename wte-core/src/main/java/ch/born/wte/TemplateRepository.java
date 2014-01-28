package ch.born.wte;

/**
 * {@linkplain TemplateRepository} gives access to the stored templates and
 * provide methodes to register new templates and edit templates
 */
public interface TemplateRepository {

	TemplateQuery queryTemplates();

	/**
	 * Sucht nach einem passenden Template im Repository
	 * 
	 * @param documentName
	 *            - name des Dokuments
	 * @param languageCode
	 *            - Sprache des Templates
	 * @return das Template oder <code>null</code> wenn kein Template gefunden
	 *         wurde.
	 */
	Template<Object> getTemplate(String documentName, String languageCode);

	/**
	 * Sucht nach einem passenden Template im Repository
	 * 
	 * @param documentName
	 *            - name des Dokuments
	 * @param languageCode
	 *            - Sprache und Region des Templates
	 * @param inputType
	 *            - Erwarteter Type für den Input
	 * @return das Template oder <code>null</code> wenn kein Template gefunden
	 *         wurde.
	 * @throws IllegalArgumentException
	 *             wenn InputType nicht passt zum Template.
	 */
	<E> Template<E> getTemplate(String documentName, String languageCode,
			Class<? extends E> inputType) throws IllegalArgumentException;

	/**
	 * Markiert ein Template fuer die Bearbeitung. Andere Benutzer können das
	 * Template nicht mehr markieren. Dokumente koennen immer noch generiert
	 * werden.
	 * 
	 * @param template
	 *            - das zu markierende Template
	 * @param user
	 *            - der markierende User
	 * @return das editierbare Template
	 * @throws LockingException
	 *             wenn das Template durch einen anderen User markiert ist.
	 */
	<E> Template<E> lockForEdit(Template<E> template, User user)
			throws LockingException;

	/**
	 * Gibt das markierte Template wieder frei.
	 * 
	 * @param template
	 *            - freizugebendes Template
	 * @return das unmarktierte Template. <code>template</code> kann nicht mehr
	 *         verwendet werden.
	 */
	<E> Template<E> unlock(Template<E> template);

	/**
	 * Speichert und gibt das template frei.
	 * 
	 * @param template
	 *            -zu speicherndes Template
	 * @return das aktualisierte Template. Das uebergebene<code>template</code>
	 *         kann nicht mehr verwendet werden.
	 * @throws LockingException
	 *             wenn das Template durch einen anderen Benutzer verändert
	 *             wurde oder gesperrt wurde (
	 *             {@link #lockForEdit(Template, User)}) wurde.
	 */
	<E> Template<E> persist(Template<E> template) throws LockingException,
			TemplateExistException;

	void delete(Template<?> template);

}
