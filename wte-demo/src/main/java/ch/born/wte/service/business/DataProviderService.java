package ch.born.wte.service.business;

import java.util.List;

import ch.born.wte.data.provider.domain.Address;
import ch.born.wte.data.provider.domain.organizations.OrganizationRootAdapter;
import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.data.provider.domain.persons.PersonRootAdapter;

public interface DataProviderService {

    List<PersonRootAdapter> getPersonsOrdered();

    List<Address> getAdresses();

    void save(Person person);

    List<OrganizationRootAdapter> getOrganizationsOrdered();

}
