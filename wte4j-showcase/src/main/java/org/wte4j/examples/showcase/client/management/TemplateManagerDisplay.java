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
package org.wte4j.examples.showcase.client.management;

import java.util.List;

import org.wte4j.ui.client.templates.TemplateListDisplay;
import org.wte4j.ui.client.templates.TemplateListPanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

public interface TemplateManagerDisplay extends IsWidget {

	void showDataModelList();

	void hideDataModelList();

	void setDataModelListItems(List<DataModelItem> dataModelItems);

	void addAddTemplateClickHandler(ClickHandler clickHandler);

	String getTemplateName();

	void addCloseDialogClickHandler(ClickHandler clickHandler);

	void addCreateTemplateClickHandler(ClickHandler clickHandler);

	String getSelectedDataModel();

	boolean validate();

	void showModalLoading();

	void hideModalLoading();

	void displayError(String message);

	void setTemplateListPanel(TemplateListPanel displayTemplatesPanel);

	void setSelectedDataModel(String dataModel);

	TemplateListDisplay getTemplateListDisplay();
}
