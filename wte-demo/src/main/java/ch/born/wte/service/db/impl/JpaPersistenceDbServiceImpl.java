package ch.born.wte.service.db.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

import ch.born.wte.common.domain.Identifiable;
import ch.born.wte.service.db.PersistenceService;

/**
 * This class provides persistence support. Attention all *DbServiceImpl
 * postfixed class will be considered by AOP and will run in a transaction
 * context. See 'applicationContext-hibernate.xml'
 * 
 */
@Repository("persistenceService")
public class JpaPersistenceDbServiceImpl implements PersistenceService {

	@PersistenceContext
	// default of PersistenceUnit injects a shared thread-safe entityManager
	// proxy!
	private EntityManager entityManager;

	/**
	 * 
	 * @return all objects of type T that as result of the criteriaQuery
	 */
	@Override
	public <T extends Identifiable> List<T> getAll(
			final CriteriaQuery<T> criteriaQuery) {
		TypedQuery<T> realQuery = entityManager.createQuery(criteriaQuery);
		return realQuery.getResultList();
	}

	/**
	 * 
	 * @return all objects of type T that are contained in the persistence
	 *         context in the defined orders.
	 */
	@Override
	public <T extends Identifiable> List<T> getAll(final Class<T> clazz) {
		CriteriaQuery<T> critQuery = createCriteriaQuery(clazz);

		TypedQuery<T> realQuery = entityManager.createQuery(critQuery);
		return realQuery.getResultList();
	}

	/**
	 * @throws IllegalStateException
	 *             if no entity can be found.
	 * @throws IllegalArgumentException
	 *             if clazz or id is {@code null}.
	 * @return the entity of in the persistence context that corresponds to the
	 *         given id or <{@code null} if not existing
	 * 
	 */
	@Override
	public <T extends Identifiable> T findById(final Class<T> clazz,
			final Serializable id) {

		T result = entityManager.find(clazz, id);

		return result;
	}

	/**
	 * This method persists the modelObject.
	 * 
	 * This method always flushes the corresponding entity manager.
	 * 
	 * @throws IllegalArgumentException
	 *             if modelObject is {@code null} or the model object is
	 *             detached from the persistence context.
	 * @see EntityManager#persist(Object)
	 * @see EntityManager#flush()
	 */
	@Override
	public <T extends Identifiable> void save(final T modelObject) {

		if (modelObject.getId() != null && !entityManager.contains(modelObject)) {
			throw new IllegalArgumentException(String.format(
					"Detached model object (%s) cannot be saved.",
					modelObject.toString()));
		} else {
			entityManager.persist(modelObject);
		}

		entityManager.flush();

	}

	/**
	 * This method merges the modelObject.
	 * 
	 * This method always flushes the corresponding entity manager.
	 * 
	 * @throws IllegalArgumentException
	 *             if modelObject is {@code null}.
	 * @see EntityManager#merge(Object)
	 * @see EntityManager#flush()
	 */
	@Override
	public <T extends Identifiable> T merge(final T modelObject) {

		T managedObject = null;

		if (modelObject.getId() != null) {
			// merge behavior in JPA 2 differs from saveOrUpdate hibernate
			// behavior!
			managedObject = entityManager.merge(modelObject);
		} else {
			throw new IllegalArgumentException(String.format(
					"Model object (%s) cannot be merged.",
					modelObject.toString()));
		}
		entityManager.flush();

		return managedObject;

	}

	/**
	 * This method refreshes the state of the model object by overriding it from
	 * the database.
	 * 
	 * @throws IllegalArgumentException
	 *             if modelObject is {@code null}.
	 */
	@Override
	public <T extends Identifiable> void refresh(final T modelObject) {
		entityManager.refresh(modelObject);
	}

	/**
	 * This method deletes the modelObject from the persistence context.
	 * Deletion will not be executed for model objects with
	 * {@code modelObject.getId() == null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if modelObject is {@code null}.
	 */
	@Override
	public <T extends Identifiable> void delete(final T modelObject) {
		if (modelObject.getId() == null) {
			return; // ignore
		}
		delete(modelObject.getClass(), modelObject.getId());

	}

	/**
	 * This method deletes a entity from the persistence context. This will be
	 * done by flushing the entity manager after deletion.
	 * 
	 * @throws IllegalArgumentException
	 *             if clazz or id is {@code null}.
	 * @throws IllegalStateException
	 *             if a persistent corresponding instance can not be found
	 */
	@Override
	public <T extends Identifiable> void delete(final Class<T> clazz,
			final Serializable id) {
		T persistentInstance = entityManager.find(clazz, id);

		entityManager.remove(persistentInstance);
		entityManager.flush();

	}

	/**
	 * @return a new criteria query with one root object for type T of the
	 *         corresponding entity manager.
	 */
	@Override
	public <T> CriteriaQuery<T> createCriteriaQuery(final Class<T> clazz) {
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(clazz);
		query.from(clazz);
		return query;
	}

	/**
	 * @return a new criteria object corresponding to the ql String.
	 * @throws IllegalArgumentException
	 *             if the qlString is found to be invalid
	 */
	@Override
	public Query createQuery(final String qlString) {
		return entityManager.createQuery(qlString);
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	@Override
	public <T> boolean contains(final Class<T> clazz, final Serializable id) {

		return null != entityManager.find(clazz, id);
	}

}
