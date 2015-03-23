/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wte4j.ui.client;

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
