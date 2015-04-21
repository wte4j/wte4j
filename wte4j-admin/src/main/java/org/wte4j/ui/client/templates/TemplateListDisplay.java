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
package org.wte4j.ui.client.templates;

import org.wte4j.ui.shared.TemplateDto;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.HasData;

public interface TemplateListDisplay extends IsWidget {

	HasData<TemplateDto> getDataContainer();

	void setDowndLoadCommand(ClickHandler command);

	void setUpdateCommand(ClickHandler command);

	void setUnLockCommandVisible(boolean visible);

	void setUnlockCommand(ClickHandler command);

	void setLockCommandVisible(boolean visible);

	void setLockCommand(ClickHandler command);

	void setDeleteCommand(ClickHandler command);

	void setContextCloseHandler(CloseHandler<PopupPanel> handler);

}
