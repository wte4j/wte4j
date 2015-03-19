package ch.born.wte.ui.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface WteResources extends ClientBundle {

	@Source("images/loader.gif")
	ImageResource loading();

	@Source("images/cog.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource contextMenuIcon();

	@Source("images/exclamation.png")
	ImageResource warningImage();

	@Source("images/exclamation-circle.png")
	ImageResource errorImage();

	@Source("images/question-balloon.png")
	ImageResource questionImage();

	@Source("images/information.png")
	ImageResource informationImage();
	
	@Source("images/page_gear.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource downloadAction();
	
	@Source("images/page_save.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource updateAction();
	
	@Source("images/lock.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource lockAction();
	
	@Source("images/lock_open.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource unlockAction();

	@Source("images/page_delete.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource deleteAction();
}
