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

import java.util.List;
import java.util.logging.Logger;

import org.wte4j.ui.client.Application;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter.FileUploadedHandler;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateService;
import org.wte4j.ui.shared.TemplateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class TemplateListPresenter {

	private Logger logger = Logger.getLogger(getClass().getName());

	private TemplateServiceAsync templateService = GWT
			.create(TemplateService.class);

	private TemplateListDisplay display;
	private TemplateDto current;

	private SingleSelectionModel<TemplateDto> selectionModel;
	private ListDataProvider<TemplateDto> dataProvider;

	private TemplateUploadPresenter uploadPresenter;

	public TemplateListPresenter() {
		((ServiceDefTarget) templateService).setServiceEntryPoint(Application.BASE_PATH + "templateService");
		selectionModel = new SingleSelectionModel<TemplateDto>();
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				current = selectionModel.getSelectedObject();
				if (current != null) {
					boolean isCurrentTemplateLocked = (current.getLockingUser() != null && current.getLockingUser().getUserId() != null);
					logger.fine("current template locking status = " + isCurrentTemplateLocked);
					display.setLockCommandVisible(!isCurrentTemplateLocked);
					display.setUnLockCommandVisible(isCurrentTemplateLocked);
				}
			}
		});
		dataProvider = new ListDataProvider<TemplateDto>();
		uploadPresenter = new TemplateUploadPresenter();
		uploadPresenter.addFileUploadedHandler(getFileUploadedHandler());

	}

	public void bindTo(TemplateListDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException(
					"presenter is already bound to a display");
		}

		display = aDisplay;
		display.getDataContainer().setSelectionModel(selectionModel);
		dataProvider.addDataDisplay(display.getDataContainer());

		display.setDowndLoadCommand(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				downLoadTemplate();

			}
		});

		display.setLockCommand(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				lockTemplate();

			}
		});

		display.setUnlockCommand(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				unlockTemplate();

			}
		});
		display.setDeleteCommand(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteTemplate();

			}
		});
		display.setUpdateCommand(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				updateTemplate();

			}
		});

		display.addContextMenuCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				selectionModel.clear();
			}
		});

		display.getTemplateUploadDisplay().addCancelButtonClickHandler(getCancelButtonClickHandler());
		uploadPresenter.bindTo(display.getTemplateUploadDisplay());

	}

	public void loadData() {

		templateService.getTemplates(new AsyncCallback<List<TemplateDto>>() {

			@Override
			public void onSuccess(List<TemplateDto> result) {
				dataProvider.setList(result);
				dataProvider.refresh();
				display.getDataContainer().setRowData(0, result);
			}

			@Override
			public void onFailure(Throwable caught) {
				showError("", caught.getMessage());
			}
		});
	}

	void deleteTemplate() {
		final TemplateDto toRemove = current;
		String documentName = toRemove.getDocumentName();
		MessageDialog deleteConfirmationDialog = new MessageDialog(
				documentName,
				Application.LABELS.deleteConfirmation(documentName),
				DialogType.QUESTION,
				getDeleteTemplateConfirmationOkHandler(toRemove));
		deleteConfirmationDialog.show();
	}

	private ClickHandler getDeleteTemplateConfirmationOkHandler(final TemplateDto toRemove) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				sendDeleteTemplateRequest(toRemove);
			}
		};
	}

	private void sendDeleteTemplateRequest(final TemplateDto toRemove) {
		templateService.deleteTemplate(toRemove, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				dataProvider.getList().remove(toRemove);
				dataProvider.flush();
			}

			@Override
			public void onFailure(Throwable caught) {
				loadData();
				showError(Application.LABELS.deleteTemplate(), caught.getLocalizedMessage());
			}
		});
	}

	void lockTemplate() {
		final TemplateDto toLock = current;
		templateService.lockTemplate(toLock, getRefreshAsCallback(toLock, Application.LABELS.unlockTemplate()));
	}

	void unlockTemplate() {
		final TemplateDto toUnlock = current;
		templateService.unlockTemplate(toUnlock, getRefreshAsCallback(toUnlock, Application.LABELS.unlockTemplate()));
	}

	private AsyncCallback<TemplateDto> getRefreshAsCallback(final TemplateDto templateToRefresh, final String actionDescriptor) {

		return new AsyncCallback<TemplateDto>() {

			@Override
			public void onSuccess(TemplateDto updated) {
				replaceInList(templateToRefresh, updated);
			}

			@Override
			public void onFailure(Throwable caught) {
				showError(actionDescriptor, caught.getLocalizedMessage());
				loadData();
			}
		};
	}

	void downLoadTemplate() {
		TemplateDto toDownload = current;
		String url = getTemplateFileRestURL() + "?name=" + toDownload.getDocumentName() + "&language=" + toDownload.getLanguage();
		Window.open(url, "parent", "");
	}

	void updateTemplate() {
		uploadPresenter.startUpload(current, getTemplateFileRestURL());
		display.showTemplateUploadDisplay("Update Template " + current.getDocumentName());
	}

	private FileUploadedHandler getFileUploadedHandler() {
		return new FileUploadedHandler() {
			@Override
			public void onSuccess(String result) {
				display.hideTemplateUploadDisplay();
				showInfo(Application.LABELS.updateTemplate(), result);
			}

			@Override
			public void onFailure(String errorMessage) {
				display.hideTemplateUploadDisplay();
				showError(Application.LABELS.updateTemplate(), errorMessage);
			}
		};
	}

	private ClickHandler getCancelButtonClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				display.hideTemplateUploadDisplay();
			}
		};
	}

	private String getTemplateFileRestURL() {
		return Application.REST_SERVICE_BASE_URL + "templates";
	}

	void replaceInList(TemplateDto toReplace, TemplateDto newTemplateDto) {
		int index = dataProvider.getList().indexOf(toReplace);
		if (index >= 0) {
			dataProvider.getList().set(index, newTemplateDto);
		}
	}

	void showError(String title, String message) {
		showDialog(title, message, DialogType.ERROR);
	}

	void showInfo(String title, String message) {
		showDialog(title, message, DialogType.INFO);
	}

	void showDialog(String title, String message, DialogType dialogType) {
		new MessageDialog(title, message, dialogType).show();
	}
}
