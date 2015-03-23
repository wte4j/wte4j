package ch.born.wte.service.db.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.service.AbstractIntegrationTest;
import ch.born.wte.service.db.DataProviderDbService;

public final class DataProviderDbServiceTest extends AbstractIntegrationTest {

    private DataProviderDbServiceImpl dataProviderDbService;

    @Autowired
    public final void setDataProviderDbService(final DataProviderDbService dataProviderDbService) {
        this.dataProviderDbService = (DataProviderDbServiceImpl) dataProviderDbService;
    }

    @Test
    public final void testGetPersonsOrderedByLastName() {
        Person pA = createPerson("Junit", "A");
        Person pB = createPerson("Junit", "B");
        Person pC = createPerson("Junit", "C");

        dataProviderDbService.save(pC);
        dataProviderDbService.save(pB);

        List<Person> personsFirst = dataProviderDbService.getPersonsOrderedByLastName();

        int idxB = personsFirst.indexOf(pB);
        int idxC = personsFirst.indexOf(pC);
        // check order index
        Assert.assertTrue(idxB != -1);
        Assert.assertTrue(idxC != -1);
        Assert.assertTrue(idxB < idxC);

        dataProviderDbService.save(pA);

        List<Person> personsSecond = dataProviderDbService.getPersonsOrderedByLastName();

        idxB = personsSecond.indexOf(pB);
        idxC = personsSecond.indexOf(pC);
        int idxA = personsSecond.indexOf(pA);

        Assert.assertTrue(idxB != -1);
        Assert.assertTrue(idxC != -1);
        Assert.assertTrue(idxA != -1);
        Assert.assertTrue(idxA < idxB);
        Assert.assertTrue(idxB < idxC);

    }

    @Test
    public final void testSave() {
        Person p = createPerson("Junit", "testSave");

        dataProviderDbService.save(p);
        JdbcTemplate jdbcAccess = getJdbcTemplate();

        // project to the last name column
        String sql = String.format("SELECT LAST_NAME FROM PERSON WHERE PARTY_ID=%d", p.getId());

        String lastName = jdbcAccess.queryForObject(sql, String.class);
        Assert.assertEquals(p.getLastName(), lastName);

    }

    private Person createPerson(final String firstName, final String lastName) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        return p;
    }

}
