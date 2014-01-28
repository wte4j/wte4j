package ch.born.wte.service.db.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.born.wte.data.provider.domain.Address;
import ch.born.wte.data.provider.domain.organizations.Organization;
import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.service.db.DataProviderDbService;
import ch.born.wte.service.db.PersistenceService;

@Repository(value = "dataProviderDbService")
public class DataProviderDbServiceImpl implements DataProviderDbService {
    private PersistenceService persistenceService;

    @Autowired
    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<Person> getPersonsOrderedByLastName() {
        CriteriaBuilder critBuilder = persistenceService.getCriteriaBuilder();

        CriteriaQuery<Person> critQuery = critBuilder.createQuery(Person.class);
        Root<Person> personRoot = critQuery.from(Person.class);

        critQuery.orderBy(critBuilder.asc(personRoot.get("lastName")));

        return persistenceService.getAll(critQuery);
    }

    @Override
    public List<Address> getAdressesOrderedById() {
        CriteriaBuilder critBuilder = persistenceService.getCriteriaBuilder();

        CriteriaQuery<Address> critQuery = critBuilder.createQuery(Address.class);
        Root<Address> addressRoot = critQuery.from(Address.class);

        critQuery.orderBy(critBuilder.asc(addressRoot.get("id")));

        return persistenceService.getAll(critQuery);

    }

    @Override
    public void save(final Person person) {
        persistenceService.save(person);

    }

    @Override
    public List<Organization> getOrganizationsOrderedByName() {
        CriteriaBuilder critBuilder = persistenceService.getCriteriaBuilder();

        CriteriaQuery<Organization> critQuery = critBuilder.createQuery(Organization.class);
        Root<Organization> orgRoot = critQuery.from(Organization.class);

        critQuery.orderBy(critBuilder.asc(orgRoot.get("name")));

        return persistenceService.getAll(critQuery);
    }

}
