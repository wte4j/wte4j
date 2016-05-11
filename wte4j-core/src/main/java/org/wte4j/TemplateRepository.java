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
package org.wte4j;

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
