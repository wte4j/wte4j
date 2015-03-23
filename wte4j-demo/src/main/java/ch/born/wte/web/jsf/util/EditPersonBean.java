package ch.born.wte.web.jsf.util;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import ch.born.wte.data.provider.domain.persons.Person;
import ch.born.wte.service.business.DataProviderService;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public final class EditPersonBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6733634242963465976L;

    private DataProviderService dataProviderService;

    // only for debugging
    @Length(min = 2, message = "Der Vorname muss mindestens 2 Zeichen sein.")
    private String firstName;

    @Length(min = 2, message = "Der Nachname muss mindestens 2 Zeichen lang sein.")
    private String lastName;

    @Autowired
    public void setDataProviderService(final DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String saveNewPerson() {

        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        dataProviderService.save(person);

        return "showPerson.xhtml";
    }

}
