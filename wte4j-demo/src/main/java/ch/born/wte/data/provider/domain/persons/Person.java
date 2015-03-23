package ch.born.wte.data.provider.domain.persons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ch.born.wte.data.provider.domain.Party;

@Entity
@Table(name = "PERSON")
public class Person extends Party {

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    public void setFirstName(final String name) {
        firstName = name;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", [firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append("]");
        return builder.toString();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

}
