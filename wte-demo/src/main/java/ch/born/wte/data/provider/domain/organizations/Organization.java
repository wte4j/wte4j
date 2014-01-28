package ch.born.wte.data.provider.domain.organizations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ch.born.wte.data.provider.domain.Party;

@Entity
@Table(name = "ORGANIZATION")
public class Organization extends Party {

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // TODO add responsible

}
