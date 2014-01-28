package ch.born.wte.data.provider.domain.persons;

import ch.born.wte.common.domain.Domain;
import ch.born.wte.data.provider.domain.Address;

public final class PersonRootAdapter {

    private final Person person;

    private Address address;

    private PersonRootAdapter(final Person person) {
        this.person = person;
        if (person.getAddresses().size() != 0) {
            address = person.getAddresses().get(0);
        }
    }

    public static Domain getDomain() {
        return Domain.PERSON;
    }

    public static PersonRootAdapter newInstance(final Person person) {
        return new PersonRootAdapter(person);
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public String getLastName() {
        return person.getLastName();
    }

    public String getTown() {
        return address.getTown();
    }

    public String getZipCode() {
        return address.getZipCode();
    }

    public String getCountry() {
        return address.getCountry();
    }

    public String getStreet() {
        return address.getStreet();
    }

    public Integer getHouseNumber() {
        return address.getHouseNumber();
    }

    public Integer getId() {
        return person.getId();
    }

    public boolean hasAddress() {
        return address != null;
    }

}
