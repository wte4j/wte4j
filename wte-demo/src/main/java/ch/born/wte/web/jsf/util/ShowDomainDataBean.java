package ch.born.wte.web.jsf.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.born.wte.data.provider.domain.organizations.OrganizationRootAdapter;
import ch.born.wte.data.provider.domain.persons.PersonRootAdapter;
import ch.born.wte.service.business.DataProviderService;

@Component
public final class ShowDomainDataBean {
    private DataProviderService dataProvider;

    @Autowired
    public void setDataProvider(final DataProviderService dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<PersonRootAdapter> getAllPersonsOrdered() {
        return dataProvider.getPersonsOrdered();

    }

    public List<OrganizationRootAdapter> getAllOrganizationsOrdered() {
        return dataProvider.getOrganizationsOrdered();

    }

}
