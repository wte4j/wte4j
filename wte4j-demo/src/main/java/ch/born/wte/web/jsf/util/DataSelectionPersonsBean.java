package ch.born.wte.web.jsf.util;

import java.io.Serializable;
import java.util.List;

import org.richfaces.component.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import ch.born.wte.data.provider.domain.persons.PersonRootAdapter;
import ch.born.wte.service.business.DataProviderService;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public class DataSelectionPersonsBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5700652529947913361L;

    private final SortOrder personSorting = SortOrder.ascending;

    private DataProviderService dataProviderService;

    @Autowired
    public final void setDataProviderService(final DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;
    }

    public List<PersonRootAdapter> getAllPersons() {
        return dataProviderService.getPersonsOrdered();
    }

    public SortOrder getPersonSorting() {
        return personSorting;
    }


}
