package ch.born.wte.data.provider.domain.organizations;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DirectiveFormExampleData {

    private static final String PROJECT_NAME = "Jugend bestimmt, Politikpartizipation im ausserschulischen Bereich";
    private static final String EDITOR_FIRST_NAME = "Thomas";
    private static final String EDITOR_LAST_NAME = "Gilbert";
    private static final String EDITOR_TITLE = "Bereichsleiter";

    private static final String DOC_ID = "753.12/2008/02119 20.12.2012 Doknr:301";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final GregorianCalendar DUE_DATE = new GregorianCalendar(2012, Calendar.SEPTEMBER, 12);
    private static final BigDecimal AID_AMOUNT = BigDecimal.valueOf(12000.00);

    // Currency.getInstance("CHE").getSymbol();
    private static final String CURRENCY = "Fr";

    private static final String HEAD_FIRST_NAME = "Martin";
    private static final String HEAD_LAST_NAME = "Hauber";

    private static final String HEAD_TITLE = "Leiter Kinder- und Jugendfragen";

    public String getProjectName() {
        return PROJECT_NAME;
    }

    public String getEditorFirstName() {
        return EDITOR_FIRST_NAME;
    }

    public String getEditorLastName() {
        return EDITOR_LAST_NAME;
    }

    public String getEditorTitle() {
        return EDITOR_TITLE;
    }

    public String getDocId() {
        return DOC_ID;
    }

    public String getDate() {
        Date retDate = new Date();

        return DATE_FORMAT.format(retDate);
    }

    public String getAidAmount() {
        return String.format("%.2f", AID_AMOUNT);

    }

    public String getCurrency() {
        return CURRENCY;
    }


    public String getDueDate() {
        return DATE_FORMAT.format(DUE_DATE.getTime());

    }

    public String getHeadFirstName() {
        return HEAD_FIRST_NAME;
    }

    public String getHeadLastName() {
        return HEAD_LAST_NAME;
    }

    public String getHeadTitle() {
        return HEAD_TITLE;
    }


}
