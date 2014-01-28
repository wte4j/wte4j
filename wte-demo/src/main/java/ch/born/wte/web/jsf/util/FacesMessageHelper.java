package ch.born.wte.web.jsf.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public final class FacesMessageHelper {

	private FacesMessageHelper() {
		// avoid instantiation
	}

	public static FacesMessage createFacesMessage(final FacesContext ctx,
			final Severity severity, final String msgKey,
			final Object... msgArgs) {

		String retMsg = "";
		// see faces config for the var attribute at the resource bundle tag
		String bundleName = "msgs";
		try {
			ResourceBundle bundle = ctx.getApplication().getResourceBundle(ctx,
					bundleName);
			retMsg = bundle.getString(msgKey);
		} catch (MissingResourceException e) {
			// jsf standard description of missing resource
			retMsg = "???" + msgKey + "???";
		}

		if (null != msgArgs) {
			MessageFormat format = new MessageFormat(retMsg);
			retMsg = format.format(msgArgs);
		}
		return new FacesMessage(severity, null, retMsg);

	}

}
