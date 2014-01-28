package ch.born.wte.web.jsf.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import junit.framework.Assert;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.junit.Test;

import ch.born.wte.web.jsf.util.FacesMessageHelper;

public final class FacesMessageHelperTest extends AbstractJsfTestCase {

    public FacesMessageHelperTest(final String name) {
        super(name);
    }


    @Test
    public void testCreateFacesMessage() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        // create a message even if the resource bundle is not available
        Assert.assertNotNull(FacesMessageHelper.createFacesMessage(ctx, FacesMessage.SEVERITY_ERROR, "anyKey", "testfilename.dotx", "testdescription"));

        Assert.assertNotNull(FacesMessageHelper.createFacesMessage(ctx, FacesMessage.SEVERITY_ERROR, "anyKey"));
    }


    @Test
    public void testCreateFacesMessageCtxNull() {
        try {

            Assert.assertNotNull(FacesMessageHelper.createFacesMessage(null, FacesMessage.SEVERITY_ERROR, "anyKey"));
            fail();
        } catch (IllegalArgumentException e) {
            // expected behavior, expected attribute at test annotation is being ignored
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFacesMessageSeverityNull() {
        try {
            Assert.assertNotNull(FacesMessageHelper.createFacesMessage(facesContext, null, "anyKey"));
            fail();
        } catch (IllegalArgumentException e) {
            // expected behavior, expected attribute at test annotation is being ignored
        }

    }

    @Test
    public void testCreateFacesMessageKeyNull() {

        try {
            Assert.assertNotNull(FacesMessageHelper.createFacesMessage(facesContext, FacesMessage.SEVERITY_ERROR, null));
            fail();
        } catch (IllegalArgumentException e) {
            // expected behavior, expected attribute at test annotation is being ignored
        }
    }

    @Test
    public void testCreateFacesMessageKeyEmpty() {

        try {
            Assert.assertNotNull(FacesMessageHelper.createFacesMessage(facesContext, FacesMessage.SEVERITY_ERROR, ""));
            fail();
        } catch (IllegalArgumentException e) {
            // expected behavior, expected attribute at test annotation is being ignored
        }
    }

}
