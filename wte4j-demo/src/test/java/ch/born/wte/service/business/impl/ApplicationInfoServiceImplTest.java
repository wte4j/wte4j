package ch.born.wte.service.business.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.born.wte.service.AbstractIntegrationTest;
import ch.born.wte.service.business.ApplicationInfoService;

public class ApplicationInfoServiceImplTest extends AbstractIntegrationTest {

    private static final String APPLICATION_SHORT_NAME = "WTE";
    private static final String APPLICATION_LONG_NAME = "Word Template Engine";

    private ApplicationInfoServiceImpl appInfoService;

    @Autowired
    public final void setApplicationInfoService(final ApplicationInfoService applicationInfoService) {
        appInfoService = (ApplicationInfoServiceImpl) applicationInfoService;
    }


    @Test
    public void testGetDisplayNameApplication() {
        Assert.assertTrue(appInfoService.getDisplayNameApplication().startsWith(APPLICATION_SHORT_NAME));
    }

    @Test
    public void testGetProductShortName() {
        Assert.assertEquals(APPLICATION_SHORT_NAME, appInfoService.getProductShortName());
    }

    @Test
    public void testGetProductName() {
        Assert.assertEquals(APPLICATION_LONG_NAME, appInfoService.getProductName());
    }

}
