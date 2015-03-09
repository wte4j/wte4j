package ch.born.wte.ui.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WteResources extends ClientBundle {
	@Source("images/edit.png")
	ImageResource tableActionMenu();
	
	@Source("images/load.gif")
	ImageResource loading();
}
