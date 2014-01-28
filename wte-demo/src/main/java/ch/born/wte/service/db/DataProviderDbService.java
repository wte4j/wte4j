package ch.born.wte.service.db;

import java.util.List;

import ch.born.wte.data.provider.domain.Address;
import ch.born.wte.data.provider.domain.organizations.Organization;
import ch.born.wte.data.provider.domain.persons.Person;

public interface DataProviderDbService {

    List<Person> getPersonsOrderedByLastName();

    List<Address> getAdressesOrderedById();

    void save(Person person);

    List<Organization> getOrganizationsOrderedByName();

}
