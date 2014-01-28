package ch.born.wte.service.business.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ch.born.wte.service.business.ApplicationInfoService;

@Service("applicationInfoService")
public final class ApplicationInfoServiceImpl implements ApplicationInfoService {

    private final DateFormat dateFormatter = new SimpleDateFormat();

    /**
     * inject the property value
     */
    @Value(value = "${wte.product.version}")
    private String version;

    @Value(value = "${wte.product.name}")
    private String productName;

    @Value(value = "${wte.product.short.name}")
    private String productShortName;

    private ApplicationContext appContext;

    private Date appStartDate;

    @Autowired
    public void setAppContext(final ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String getDisplayNameApplication() {

        return String.format("%s v%s (%s)", getProductShortName(), version, dateFormatter.format(getAppStartDate()));
    }

    public String getProductShortName() {
        return productShortName;
    }

    private Date getAppStartDate() {
        if (null == appStartDate) {
            appStartDate = new Date(appContext.getStartupDate());
        }
        return appStartDate;
    }

    public String getProductName() {
        return productName;
    }

}
