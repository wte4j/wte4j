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

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.wte4j.ui.client.Application;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;
import org.wte4j.ui.client.templates.upload.TemplateUploadDisplay;
import org.wte4j.ui.client.templates.upload.TemplateUploadFormPanel;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter.FileUploadedHandler;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateService;
import org.wte4j.ui.shared.TemplateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class TemplateListPresenter {

	private Logger logger = Logger.getLogger(getClass().getName());

	private TemplateServiceAsync templateService = GWT
			.create(TemplateService.class);

	private TemplateListDisplay display;
	private TemplateDto current;

	private NoSelectionModel<TemplateDto> selectionModel;
	private ListDataProvider<TemplateDto> dataProvider;

	public TemplateListPresenter() {
		((ServiceDefTarget) templateService).setServiceEntryPoint(Application.BASE_PATH + "templateService");
		selectionModel = new NoSelectionModel<TemplateDto>();
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				current = selectionModel.getLastSelectedObject();
				boolean isCurrentTemplateLocked = (current.getLockingUser() != null && current.getLockingUser().getUserId() != null);
				logger.fine("current template locking status = " + isCurrentTemplateLocked);
				display.setLockCommandVisible(!isCurrentTemplateLocked);
				display.setUnLockCommandVisible(isCurrentTemplateLocked);
			}
		});
		dataProvider = new ListDataProvider<TemplateDto>();

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

		Modal updateTemplatePopupPanel = getUpdateTemplatePopupPanel();
		updateTemplatePopupPanel.show();
	}

	private Modal getUpdateTemplatePopupPanel() {
		final Modal updateTemplatePopupPanel = new Modal();
		updateTemplatePopupPanel.setTitle("Update Template " + current.getDocumentName());
		ModalBody body = new ModalBody();
		body.add(getUpdateTemplateFormPanel(updateTemplatePopupPanel));
		updateTemplatePopupPanel.add(body);
		return updateTemplatePopupPanel;
	}

	private TemplateUploadDisplay getUpdateTemplateFormPanel(Modal updateTemplatePopupPanel) {
		TemplateUploadDisplay updateTemplateFormPanel = new TemplateUploadFormPanel();
		TemplateUploadPresenter templateListPresenter = new TemplateUploadPresenter(current, getTemplateFileRestURL());
		templateListPresenter.bindTo(updateTemplateFormPanel);
		PopupPanel uploadingPopup = getUploadingPopup();
		templateListPresenter.addSubmitButtonClickHandler(getSubmitButtonClickHandler(updateTemplatePopupPanel, uploadingPopup));
		templateListPresenter.addCancelButtonClickHandler(getCancelButtonClickHandler(updateTemplatePopupPanel, uploadingPopup));
		templateListPresenter.addFileUploadedHandler(getFileUploadedHandler(updateTemplatePopupPanel, uploadingPopup));
		return updateTemplateFormPanel;
	}

	private FileUploadedHandler getFileUploadedHandler(
			final Modal updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
		return new FileUploadedHandler() {

			@Override
			public void onSuccess(String result) {
				uploadingPopup.hide();
				updateTemplatePopupPanel.hide();
				showInfo(Application.LABELS.updateTemplate(), result);
			}

			@Override
			public void onFailure(String errorMessage) {
				uploadingPopup.hide();
				updateTemplatePopupPanel.setVisible(true);
				showError(Application.LABELS.updateTemplate(), errorMessage);
			}
		};
	}

	private ClickHandler getSubmitButtonClickHandler(final Modal updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				updateTemplatePopupPanel.setVisible(false);
				uploadingPopup.show();
			}
		};
	}

	private PopupPanel getUploadingPopup() {
		PopupPanel uploadingPanel = new PopupPanel();
		uploadingPanel.center();
		uploadingPanel.add(new Image(Application.RESOURCES.loading()));
		uploadingPanel.setGlassEnabled(true);
		return uploadingPanel;
	}

	private ClickHandler getCancelButtonClickHandler(final Modal updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uploadingPopup.hide();
				updateTemplatePopupPanel.hide();
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
