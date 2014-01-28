package ch.born.wte.web.jsf.util;

import java.io.Serializable;
import java.util.List;

import org.richfaces.component.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import ch.born.wte.data.provider.domain.organizations.OrganizationRootAdapter;
import ch.born.wte.service.business.DataProviderService;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public class DataSelectionOrganizationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5700652529947913361L;

    private DataProviderService dataProviderService;


    private final SortOrder organizationSorting = SortOrder.ascending;

    @Autowired
    public final void setDataProviderService(final DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;
    }


    public List<OrganizationRootAdapter> getAllOrganizations() {
        return dataProviderService.getOrganizationsOrdered();
    }



    public SortOrder getOrganizationSorting() {
        return organizationSorting;
    }

}
