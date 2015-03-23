package ch.born.wte.web.jsf.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public final class LocaleSwitcherBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3015892968971735438L;

    private static final List<Locale> LOCALES = new ArrayList<Locale>();
    static {
        LOCALES.add(Locale.GERMAN);
        LOCALES.add(Locale.ENGLISH);

    }

    private Locale locale;

    private Locale getCurrentLocale() {
        return new Locale(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
    }

    public Locale getLocale() {
        if (null == locale) {
            locale = getCurrentLocale();
        }
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    private UIViewRoot getJsfViewRoot() {
        return FacesContext.getCurrentInstance().getViewRoot();
    }

    public void languageChanged(final ValueChangeEvent event) {
        Locale nLocale = (Locale) event.getNewValue();

        setLocale(nLocale);
        UIViewRoot viewRoot = getJsfViewRoot();
        viewRoot.setLocale(nLocale);
    }

    public List<Locale> getLocales() {
        return LOCALES;

    }
}
