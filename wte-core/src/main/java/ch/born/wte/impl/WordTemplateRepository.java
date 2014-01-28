package ch.born.wte.impl;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.born.wte.FileStore;
import ch.born.wte.FormatterFactory;
import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.TemplateExistException;
import ch.born.wte.TemplateQuery;
import ch.born.wte.TemplateRepository;
import ch.born.wte.User;
import ch.born.wte.WteModelService;

@Repository
@Transactional("wte")
public class WordTemplateRepository implements TemplateRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = "wte-templates")
	protected EntityManager em;

	@Autowired
	@Qualifier("wteModelService")
	protected WteModelService modelService;

	@Autowired
	protected FormatterFactory formatterFactory;

	@Autowired(required = false)
	protected FileStore fileStore;

	protected WordTemplateRepository() {
	};

	public WordTemplateRepository(EntityManager em,
			WteModelService modelService, FormatterFactory formatterFactory) {
		super();
		this.em = em;
		this.modelService = modelService;
		this.formatterFactory = formatterFactory;
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
			return new WordTemplate<Object>(persistentTemplate, modelService,
					formatterFactory);
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
			return new WordTemplate<E>(persistentTemplate, modelService,
					formatterFactory);
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
		return new WordTemplate<E>(unwrapped, modelService, formatterFactory);
	}

	@Override
	public <E> Template<E> unlock(Template<E> template) {
		PersistentTemplate unwrapped = unwrap(template);
		unwrapped.unlock();
		unwrapped = update(unwrapped);
		return new WordTemplate<E>(unwrapped, modelService, formatterFactory);
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

		return new WordTemplate<E>(unwrapped, modelService, formatterFactory);

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
					modelService, formatterFactory));
		}
		return wrapped;
	}

	boolean isFileStoreSet() {
		return fileStore != null;
	}

}
