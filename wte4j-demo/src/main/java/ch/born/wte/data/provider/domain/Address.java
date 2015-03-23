package ch.born.wte.data.provider.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.born.wte.common.domain.Identifiable;

@Entity
@Table(name = "ADDRESS")
public class Address implements Identifiable {
    @Id
    @GeneratedValue
    @Column(name = "ADDRESS_ID")
    private Integer id;

    private String town;
    private String zipCode;
    private String country;
    private String street;
    @Column(name = "HOUSE_NUMBER")
    private Integer houseNumber;

    @Override
    public Integer getId() {
        return id;
    }

    public final void setId(final Integer id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public final void setTown(final String town) {
        this.town = town;
    }

    public String getZipCode() {
        return zipCode;
    }

    public final void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public final void setCountry(final String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public final void setStreet(final String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public final void setHouseNumber(final Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AddressDao [id=");
        builder.append(id);
        builder.append(", town=");
        builder.append(town);
        builder.append(", street=");
        builder.append(street);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
