package ch.born.wte.data.provider.domain.organizations;

import ch.born.wte.common.domain.Domain;
import ch.born.wte.data.provider.domain.Address;

public final class OrganizationRootAdapter {

    private static final DirectiveFormExampleData DIRC_FORM = new DirectiveFormExampleData();

    private static final String FIRST_NAME_ORGANIZATION_RESPONIBLE = "Martin";
    private static final String LAST_NAME_ORGANIZATION_RESPONSIBLE = "Kurz";


    private final Organization organization;
    private Address address;

    private OrganizationRootAdapter(final Organization orgDao) {
        organization = orgDao;
        if (organization.getAddresses().size() != 0) {
            address = organization.getAddresses().get(0);
        }
    }

    public static Domain getDomain() {
        return Domain.ORGANIZATION;
    }

    public static OrganizationRootAdapter newInstance(final Organization orgDao) {
        return new OrganizationRootAdapter(orgDao);
    }

    public String getName() {
        return organization.getName();
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
        return organization.getId();
    }

    public boolean hasAddress() {
        return address != null;
    }

    public String getProjectName() {
        return DIRC_FORM.getProjectName();
    }

    public String getEditorFirstName() {
        return DIRC_FORM.getEditorFirstName();
    }

    public String getEditorLastName() {
        return DIRC_FORM.getEditorLastName();
    }

    public String getEditorTitle() {
        return DIRC_FORM.getEditorTitle();
    }

    public String getDocId() {
        return DIRC_FORM.getDocId();
    }

    public String getDate() {
        return DIRC_FORM.getDate();
    }

    public String getAidAmount() {
        return DIRC_FORM.getAidAmount();
    }

    public String getCurrency() {
        return DIRC_FORM.getCurrency();
    }

    public String getDueDate() {
        return DIRC_FORM.getDueDate();
    }

    public String getHeadFirstName() {
        return DIRC_FORM.getHeadFirstName();
    }

    public String getHeadLastName() {
        return DIRC_FORM.getHeadLastName();
    }

    public String getHeadTitle() {
        return DIRC_FORM.getHeadTitle();
    }

    public String getFirstNameOrganizationResponsible() {
        return FIRST_NAME_ORGANIZATION_RESPONIBLE;
    }

    public String getLastNameOrganizationResponsible() {
        return LAST_NAME_ORGANIZATION_RESPONSIBLE;
    }

}
