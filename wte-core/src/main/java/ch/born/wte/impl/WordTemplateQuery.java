/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
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
package ch.born.wte.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import ch.born.wte.Template;
import ch.born.wte.TemplateQuery;

public class WordTemplateQuery implements TemplateQuery {
	private final WordTemplateRepository repository;
	private final CriteriaBuilder criteriaBuilder;
	private final CriteriaQuery<PersistentTemplate> query;
	private final Root<PersistentTemplate> persistentTemplate;
	private final Collection<Predicate> restrictions;

	public WordTemplateQuery(WordTemplateRepository aRepository,
			CriteriaBuilder aCriteriaBuilder) {
		repository = aRepository;
		criteriaBuilder = aCriteriaBuilder;
		query = criteriaBuilder.createQuery(PersistentTemplate.class);
		persistentTemplate = query.from(PersistentTemplate.class);
		restrictions = new ArrayList<Predicate>();
	}

	public WordTemplateQuery documentName(String name) {
		equal(persistentTemplate.get("documentName"), name);
		return this;
	}

	public WordTemplateQuery language(String language) {
		equal(persistentTemplate.get("language"), language);
		return this;
	}

	public WordTemplateQuery inputType(Class<?> clazz) {
		equal(persistentTemplate.get("inputClassName"), clazz.getName());
		return this;
	}

	public WordTemplateQuery hasProperties(Map<String, String> someProperties) {
		Subquery<Long> subQuery = query.subquery(Long.class);
		Root<PersistentTemplate> fromSubQuery = subQuery
				.from(PersistentTemplate.class);
		Path<Long> id = fromSubQuery.get("id");
		subQuery.select(id);
		MapJoin<PersistentTemplate, String, String> propertiesMap = fromSubQuery
				.joinMap("properties", JoinType.INNER);
		subQuery.where(buildPropertyRestriction(propertiesMap, someProperties));
		subQuery.groupBy(id);
		subQuery.having(criteriaBuilder.equal(criteriaBuilder.count(id),
				someProperties.size()));
		restrictions.add(persistentTemplate.get("id").in(subQuery));
		return this;
	}

	private Predicate buildPropertyRestriction(
			MapJoin<PersistentTemplate, String, String> propertiesMap,
			Map<String, String> someProperties) {
		Path<?> keyPath = propertiesMap.key();
		Path<?> valuePath = propertiesMap.value();
		List<Predicate> keyValuePairs = new LinkedList<Predicate>();
		for (Map.Entry<String, String> entry : someProperties.entrySet()) {
			Predicate keyPredicate = criteriaBuilder.equal(keyPath,
					entry.getKey());
			Predicate valuePredicate = criteriaBuilder.equal(valuePath,
					entry.getValue());
			Predicate keyValuePair = criteriaBuilder.and(keyPredicate,
					valuePredicate);
			keyValuePairs.add(keyValuePair);
		}
		return criteriaBuilder.or(keyValuePairs
				.toArray(new Predicate[keyValuePairs.size()]));
	}

	public WordTemplateQuery editor(String userId) {
		Path<?> pathEditorId = persistentTemplate.get("editor").get("userId");
		equal(pathEditorId, userId);
		return this;
	}

	public WordTemplateQuery isLocked(boolean locked) {
		Predicate restriction = null;
		Path<?> path = persistentTemplate.get("lockingDate");
		if (locked) {
			restriction = criteriaBuilder.isNotNull(path);
		} else {
			restriction = criteriaBuilder.isNull(path);
		}
		restrictions.add(restriction);
		return this;
	}

	public WordTemplateQuery isLockedBy(String userId) {
		Path<?> lockingUserId = persistentTemplate.get("lockingUser").get(
				"userId");
		equal(lockingUserId, userId);
		return this;
	}

	void equal(Path<?> path, Object value) {
		Predicate restriction = criteriaBuilder.equal(path, value);
		restrictions.add(restriction);
	}

	List<PersistentTemplate> list(EntityManager entityManager) {
		query.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<PersistentTemplate> executableQuery = entityManager
				.createQuery(query);
		return executableQuery.getResultList();
	}

	@Override
	public List<Template<Object>> list() {
		return repository.execute(this);
	}
}
