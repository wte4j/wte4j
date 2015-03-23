package ch.born.wte.web.jsf.util;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

@Component
@Scope(value = RequestAttributes.REFERENCE_SESSION)
public final class PanelMenuBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1650480976141831856L;

    // Attention: Redirect navigation targets in order to get the components rendered!
    public String goHome() {
        return "/index.xhtml?faces-redirect=true";
    }

    public String openSettings() {
        return "/settings.xhtml?faces-redirect=true";
    }

    public String openTemplates() {
        return "/domainSelection.xhtml?faces-redirect=true";
    }

    public String openGeneration() {
        return "/templateSelection.xhtml?faces-redirect=true";
    }

    public String openHelp() {
        return "/help.xhtml?faces-redirect=true";
    }

    public String openNewPerson() {
        return "/newPerson.xhtml?faces-redirect=true";
    }

}
