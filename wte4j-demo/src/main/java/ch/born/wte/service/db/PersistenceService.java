package ch.born.wte.service.db;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import ch.born.wte.common.domain.Identifiable;

public interface PersistenceService {

    /**
     * 
     * @return the criteria builder of the corresponding entity manager.
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * 
     * @return all objects of type T that are contained in the persistence context in the defined orders.
     */
    <T extends Identifiable> List<T> getAll(final Class<T> clazz);

    /**
     * Returns all objects of type T that are contained in the persistence context in the defined orders.
     */
    <T extends Identifiable> List<T> getAll(final CriteriaQuery<T> criteriaQuery);

    /**
     * @return the entity of in the persistence context that corresponds to the given id
     * 
     */
    <T extends Identifiable> T findById(Class<T> clazz, Serializable id);

    /**
     * This method persists the modelObjects.
     */
    <T extends Identifiable> void save(T modelObject);

    /**
     * This method merges the modelObjects and returns the merged , managed object.
     */
    <T extends Identifiable> T merge(final T modelObject);

    /**
     * This method refreshes the state of the model object by overriding it from the database.
     */
    <T extends Identifiable> void refresh(T model);

    /**
     * This method deletes the modelObject from the persistence context.
     * 
     */
    <T extends Identifiable> void delete(T model);

    /**
     * This method deletes a entity from the persistence context.
     */
    <T extends Identifiable> void delete(Class<T> clazz, Serializable id);

    /**
     * @return a new criteria query object for type T of the corresponding entity manager.
     */
    <T> CriteriaQuery<T> createCriteriaQuery(Class<T> clazz);

    /**
     * @return a new criteria object corresponding to the ql String.
     */
    Query createQuery(String hql);

    /**
     * 
     * @param <T>
     *            the generic class type
     * @param clazz
     * @param id
     * @return {@code true} if the persistence unit contains the denoted entity, {@code false} otherwise.
     */
    <T> boolean contains(Class<T> clazz, Serializable id);

}
