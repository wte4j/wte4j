package ch.born.wte.ui.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface WteResources extends ClientBundle {

	@Source("images/load.gif")
	ImageResource loading();

	@Source("images/toggle-down-alt.png")
	@ImageOptions(height = 32, width = 32)
	ImageResource contextMenuIcon();

	@Source("images/exclamation.png")
	ImageResource warningImage();

	@Source("images/exclamation-circle.png")
	ImageResource errorImage();

	@Source("images/question-balloon.png")
	ImageResource questionImage();

	@Source("images/information.png")
	ImageResource informationImage();
}
