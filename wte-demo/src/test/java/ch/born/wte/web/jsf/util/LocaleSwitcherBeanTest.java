package ch.born.wte.web.jsf.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.event.ValueChangeEvent;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.junit.Before;
import org.junit.Test;

import ch.born.wte.web.jsf.util.LocaleSwitcherBean;

public class LocaleSwitcherBeanTest extends AbstractJsfTestCase {

    private LocaleSwitcherBean localeSwitcher;

    public LocaleSwitcherBeanTest(final String name) {
        super(name);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        localeSwitcher = new LocaleSwitcherBean();

    }

    @Test
    public void testGetLocale() {
        Locale exp = Locale.getDefault();
        Locale loc = localeSwitcher.getLocale();

        assertEquals(exp.getLanguage(), loc.getLanguage());
    }

    @Test
    public void testSetLocale() {

        localeSwitcher.setLocale(Locale.ENGLISH);

        assertEquals(Locale.ENGLISH, localeSwitcher.getLocale());

    }

    @Test
    public void testLanguageChanged() {
        Locale newLocale = Locale.ENGLISH;

        assertFalse(localeSwitcher.getLocale().equals(newLocale));

        ValueChangeEvent eventMock = mock(ValueChangeEvent.class);
        when(eventMock.getNewValue()).thenReturn(newLocale);

        localeSwitcher.languageChanged(eventMock);

        assertEquals(newLocale, localeSwitcher.getLocale());

        assertEquals(newLocale, facesContext.getViewRoot().getLocale());

    }

    @Test
    public void testGetLocales() {

        List<Locale> exp = new ArrayList<Locale>();
        exp.add(Locale.GERMAN);
        exp.add(Locale.ENGLISH);

        List<Locale> locs = localeSwitcher.getLocales();
        assertEquals(exp, locs);

    }

}
