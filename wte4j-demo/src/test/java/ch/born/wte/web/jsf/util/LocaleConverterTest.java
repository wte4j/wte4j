package ch.born.wte.web.jsf.util;

import java.util.Locale;

import javax.faces.convert.ConverterException;

import junit.framework.Assert;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.junit.Before;
import org.junit.Test;

import ch.born.wte.web.jsf.util.LocaleConverter;

public class LocaleConverterTest extends AbstractJsfTestCase {


    private LocaleConverter converter;

    public LocaleConverterTest(final String name) {
        super(name);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        converter = new LocaleConverter();

    }

    @Test
    public void testGetAsObject() {
        String lang = "de";
        Object obj = converter.getAsObject(facesContext, null, lang);

        Assert.assertTrue(obj instanceof Locale);

        Locale locale = (Locale) obj;
        Assert.assertEquals("de", locale.getLanguage());
    }

    @Test
    public void testGetAsObjectNull() {
        Object obj = converter.getAsObject(facesContext, null, null);

        Assert.assertNull(obj);
    }

    @Test
    public void testGetAsObjectemptyString() {
        Object obj = converter.getAsObject(facesContext, null, "");

        Assert.assertNull(obj);
    }

    @Test
    public void testGetAsStringNull() {
        String result = converter.getAsString(facesContext, null, null);

        assertNull(result);

    }

    @Test
    public void testGetAsStringOtherObject() {
        Integer idx = Integer.valueOf(2);
        try{
            converter.getAsString(facesContext, null, idx);
            fail();
        }catch (ConverterException e) {
            // expected behavior
        }


    }


}
