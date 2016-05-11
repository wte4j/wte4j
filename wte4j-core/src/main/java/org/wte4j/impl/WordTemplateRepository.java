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
package org.wte4j.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wte4j.FileStore;
import org.wte4j.LockingException;
import org.wte4j.Template;
import org.wte4j.TemplateExistException;
import org.wte4j.TemplateQuery;
import org.wte4j.TemplateRepository;
import org.wte4j.User;

@Repository
@Transactional("wte4j")
public class WordTemplateRepository implements TemplateRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = "wte4j-templates")
	protected EntityManager em;
	@Autowired
	protected TemplateContextFactory contextFactory;

	@Autowired(required = false)
	protected FileStore fileStore;

	protected WordTemplateRepository() {
	}

	public WordTemplateRepository(EntityManager em, TemplateContextFactory contextFactory) {
		super();
		this.em = em;
		this.contextFactory = contextFactory;
	}

	public void setFileStore(FileStore fileStore) {
		this.fileStore = fileStore;
	}

	@Override
	public TemplateQuery queryTemplates() {
		return new WordTemplateQuery(this, em.getCriteriaBuilder());
	}

	@Override
	public Template<Object> getTemplate(String documentName, String language) {
		try {
			PersistentTemplate persistentTemplate = getPersistentTemplate(
					documentName, language);
			return new WordTemplate<Object>(persistentTemplate,
					contextFactory);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <E> Template<E> getTemplate(String documentName, String language,
			Class<? extends E> inputType) throws IllegalArgumentException {
		try {
			PersistentTemplate persistentTemplate = getPersistentTemplate(
					documentName, language);
			if (!persistentTemplate.getInputType().isAssignableFrom(inputType)) {
				throw new IllegalArgumentException(inputType.getName()
						+ " is not suported by the specified template");
			}
			return new WordTemplate<E>(persistentTemplate,
					contextFactory);
		} catch (NoResultException e) {
			return null;
		}

	}

	private PersistentTemplate getPersistentTemplate(String documentName,
			String language) throws NoResultException {

		String queryString = "select t from PersistentTemplate t where t.documentName=:documentName "
				+ "and t.language= :language";
		TypedQuery<PersistentTemplate> query = em.createQuery(queryString,
				PersistentTemplate.class);
		query.setParameter("documentName", documentName);
		query.setParameter("language", language);
		return query.getSingleResult();
	}

	@Override
	public <E> Template<E> lockForEdit(Template<E> aTemplate, User user)
			throws LockingException {
		PersistentTemplate unwrapped = unwrap(aTemplate);
		unwrapped.lock(user);
		unwrapped = update(unwrapped);
		return new WordTemplate<E>(unwrapped, contextFactory);
	}

	@Override
	public <E> Template<E> unlock(Template<E> template) {
		PersistentTemplate unwrapped = unwrap(template);
		unwrapped.unlock();
		unwrapped = update(unwrapped);
		return new WordTemplate<E>(unwrapped, contextFactory);
	}

	@Override
	public <E> Template<E> persist(Template<E> toPersist)
			throws LockingException {

		PersistentTemplate unwrapped = unwrap(toPersist);
		if (unwrapped.getId() == null) {
			persist(unwrapped);
		} else {
			unwrapped.unlock();
			unwrapped = update(unwrapped);
		}
		if (isFileStoreSet()) {
			updateFileStore(unwrapped);
		}

		return new WordTemplate<E>(unwrapped, contextFactory);

	}

	@Override
	public void delete(Template<?> template) {
		PersistentTemplate unwrapped = unwrap(template);
		if (unwrapped.isLocked()) {
			throw new LockingException("Template is locked");
		}
		delete(unwrapped);
		if (isFileStoreSet()) {
			fileStore.deleteFile(unwrapped.getTemplateFileName());
		}
	}

	private PersistentTemplate unwrap(Template<?> aTemplate) {
		WordTemplate<?> wt = (WordTemplate<?>) aTemplate;
		return wt.getPersistentData();
	}

	private void persist(PersistentTemplate toPersist) throws LockingException,
			TemplateExistException {
		if (getTemplate(toPersist.getDocumentName(), toPersist.getLanguage()) != null) {
			throw new TemplateExistException("Template with name="
					+ toPersist.getDocumentName() + " and language="
					+ toPersist.getLanguage() + " allready exists");
		}
		em.persist(toPersist);
		em.flush();
	}

	private PersistentTemplate update(PersistentTemplate toUpdate) {
		try {
			PersistentTemplate updated = em.merge(toUpdate);
			em.flush();
			return updated;
		} catch (OptimisticLockException e) {
			throw new LockingException("Template has been locked", e);
		}
	}

	private void delete(PersistentTemplate aTemplate) {
		try {
			PersistentTemplate template = em.merge(aTemplate);
			em.remove(template);
			em.flush();
		} catch (OptimisticLockException e) {
			throw new LockingException("Template has been locked", e);
		}
	}

	void updateFileStore(PersistentTemplate template) {
		String fileName = template.getTemplateFileName();
		OutputStream out = null;
		try {
			out = fileStore.getOutStream(fileName);
			template.writeContent(out);
		} catch (IOException e) {
			logger.error("Error occured when storring {}", fileName, e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@Transactional
	public List<Template<Object>> execute(WordTemplateQuery query) {
		List<PersistentTemplate> templates = query.list(em);
		List<Template<Object>> wrapped = new ArrayList<Template<Object>>(
				templates.size());
		for (PersistentTemplate persistentTemplate : templates) {
			wrapped.add(new WordTemplate<Object>(persistentTemplate,
					contextFactory));
		}
		return wrapped;
	}

	boolean isFileStoreSet() {
		return fileStore != null;
	}

}
