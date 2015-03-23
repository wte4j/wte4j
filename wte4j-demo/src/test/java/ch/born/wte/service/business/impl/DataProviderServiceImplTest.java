package ch.born.wte.service.business.impl;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.service.AbstractIntegrationTest;
import ch.born.wte.service.db.DataProviderDbService;

public class DataProviderServiceImplTest extends AbstractIntegrationTest {

    private DataProviderServiceImpl dataProviderService;
    private DataProviderDbService dataProviderDbServiceMock;

    @Before
    public void setupMock() {

        dataProviderService = new DataProviderServiceImpl();
        dataProviderDbServiceMock = mock(DataProviderDbService.class);
        dataProviderService.setDataProviderDbService(dataProviderDbServiceMock);

    }

    @Test
    public void testGetPersonsOrdered() {
        Assert.assertNotNull(dataProviderService.getPersonsOrdered());

        Mockito.verify(dataProviderDbServiceMock).getPersonsOrderedByLastName();
    }

    @Test
    public void testGetAdresses() {
        Assert.assertNotNull(dataProviderService.getAdresses());

        Mockito.verify(dataProviderDbServiceMock).getAdressesOrderedById();
    }

    @Test
    public void testSave() {
        Person person = new Person();
        dataProviderService.save(person);

        Mockito.verify(dataProviderDbServiceMock).save(person);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNull() {
        dataProviderService.save(null);

    }

}
