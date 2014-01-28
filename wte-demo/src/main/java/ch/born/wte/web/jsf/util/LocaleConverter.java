package ch.born.wte.web.jsf.util;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


@FacesConverter(value = "ch.born.wte.jsf.converter.localeConverter", forClass = Locale.class)
public final class LocaleConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext ctx, final UIComponent uiComp, final String language) {
        if (null == language || language.isEmpty()) {
            return null;
        }
        return new Locale(language);
    }

    @Override
    public String getAsString(final FacesContext ctx, final UIComponent uiComp, final Object obj) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof Locale) {
            Locale locale = (Locale) obj;
            return locale.getLanguage();
        }
        // indicate a converter configuration error
        FacesMessage msg = FacesMessageHelper.createFacesMessage(ctx, FacesMessage.SEVERITY_ERROR, "localeConverterTypeError");
        throw new ConverterException(msg);

    }

}
