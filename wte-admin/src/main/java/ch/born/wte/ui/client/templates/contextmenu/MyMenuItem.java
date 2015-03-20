package ch.born.wte.ui.client.templates.contextmenu;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.MenuItem;

class MyMenuItem extends MenuItem {
	
    @UiConstructor
    public MyMenuItem(String text, ImageResource res) {
        super(SafeHtmlUtils.fromString(text));

        ImageResourceRenderer renderer = new ImageResourceRenderer();
        setHTML(renderer.render(res).asString() + "&nbsp;" + text);
    }
}