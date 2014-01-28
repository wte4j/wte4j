package ch.born.wte.service.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.born.wte.data.provider.domain.Address;
import ch.born.wte.data.provider.domain.organizations.Organization;
import ch.born.wte.data.provider.domain.organizations.OrganizationRootAdapter;
import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.data.provider.domain.persons.PersonRootAdapter;
import ch.born.wte.service.business.DataProviderService;
import ch.born.wte.service.db.DataProviderDbService;

@Service("dataProviderService")
public final class DataProviderServiceImpl implements DataProviderService {

	private DataProviderDbService dataProviderDbService;

	@Autowired
	public void setDataProviderDbService(
			final DataProviderDbService dataProviderDbService) {
		this.dataProviderDbService = dataProviderDbService;
	}

	@Override
	public List<PersonRootAdapter> getPersonsOrdered() {
		List<Person> persons = dataProviderDbService
				.getPersonsOrderedByLastName();

		List<PersonRootAdapter> ret = new ArrayList<PersonRootAdapter>(
				persons.size());

		for (Person person : persons) {
			ret.add(PersonRootAdapter.newInstance(person));

		}

		return ret;

	}

	@Override
	public List<Address> getAdresses() {

		return dataProviderDbService.getAdressesOrderedById();
	}

	@Override
	public void save(final Person person) {
		dataProviderDbService.save(person);
	}

	@Override
	public List<OrganizationRootAdapter> getOrganizationsOrdered() {
		List<Organization> orgs = dataProviderDbService
				.getOrganizationsOrderedByName();

		List<OrganizationRootAdapter> ret = new ArrayList<OrganizationRootAdapter>(
				orgs.size());

		for (Organization org : orgs) {
			ret.add(OrganizationRootAdapter.newInstance(org));

		}

		return ret;

	}
}
