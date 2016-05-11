/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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

import static org.wte4j.ui.client.Application.LABELS;

import java.util.List;
import java.util.logging.Logger;

import org.wte4j.ui.client.Application;
import org.wte4j.ui.client.GrowlFactory;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;
import org.wte4j.ui.client.templates.mapping.MappingPresenter;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter;
import org.wte4j.ui.client.templates.upload.TemplateUploadPresenter.FileUploadedHandler;
import org.wte4j.ui.shared.InvalidTemplateServiceException;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class TemplateListPresenter {

	private Logger logger = Logger.getLogger(getClass().getName());

	private TemplateServiceAsync templateService = TemplateServiceAsync.Util.getInstance();

	private TemplateListDisplay display;
	private TemplateDto current;
	private String uploadedFile;

	private SingleSelectionModel<TemplateDto> selectionModel;
	private ListDataProvider<TemplateDto> dataProvider;

	private TemplateUploadPresenter uploadPresenter;
	private MappingPresenter mappingPresenter;

	public TemplateListPresenter() {

		selectionModel = new SingleSelectionModel<TemplateDto>();
		selectionModel.addSelectionChangeHandler(createSelectionChangeHandler());
		dataProvider = new ListDataProvider<TemplateDto>();

		uploadPresenter = new TemplateUploadPresenter();
		uploadPresenter.addFileUploadedHandler(getFileUploadedHandler());

		mappingPresenter = new InternalMappingPresenter();

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
				startFileUpload();

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
		mappingPresenter.bind(display.getMappingDisplay());

	}

	public void loadData() {

		templateService.getTemplates(new AsyncCallback<List<TemplateDto>>() {

			@Override
			public void onSuccess(List<TemplateDto> result) {
				dataProvider.setList(result);
				dataProvider.refresh();
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
				LABELS.deleteConfirmation(documentName),
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
				showError(LABELS.deleteTemplate(), caught.getLocalizedMessage());
			}
		});
	}

	void lockTemplate() {
		final TemplateDto toLock = current;
		templateService.lockTemplate(toLock, getRefreshAsCallback(toLock, LABELS.unlockTemplate()));
	}

	void unlockTemplate() {
		final TemplateDto toUnlock = current;
		templateService.unlockTemplate(toUnlock, getRefreshAsCallback(toUnlock, LABELS.unlockTemplate()));
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

	void startFileUpload() {
		uploadPresenter.startUpload(current, getTemplateFileRestURL() + "/temp");
		display.showTemplateUploadDisplay("Update Template " + current.getDocumentName());
	}

	void updateTemplate() {
		templateService.saveTemplateData(current, uploadedFile, new AsyncCallback<TemplateDto>() {
			@Override
			public void onSuccess(TemplateDto result) {
				display.hideTemplateUploadDisplay();
				mappingPresenter.startEditMapping(current, uploadedFile);
				display.showMappingDisplay(LABELS.updateTemplate_header(current.getDocumentName()));
			}

			@Override
			public void onFailure(Throwable caught) {
				display.hideTemplateUploadDisplay();
				if (caught instanceof InvalidTemplateServiceException) {
					InvalidTemplateServiceException e = (InvalidTemplateServiceException) caught;
					mappingPresenter.startEditMapping(current, uploadedFile);
					mappingPresenter.displayInvalidTemplateMessage(e);
					display.showMappingDisplay(LABELS.updateTemplate_header(current.getDocumentName()));
				} else {
					showError(LABELS.updateTemplate_header(current.getDocumentName()), caught.getMessage());
				}
			}
		});
	}

	private FileUploadedHandler getFileUploadedHandler() {
		return new FileUploadedHandler() {
			@Override
			public void onSuccess(String result) {
				uploadedFile = result;
				mappingPresenter.startEditMapping(current, uploadedFile);
				display.hideTemplateUploadDisplay();
				display.showMappingDisplay(LABELS.updateTemplate_header(current.getDocumentName()));
			}

			@Override
			public void onFailure(String errorMessage) {
				loadData();
				showError(LABELS.updateTemplate_header(current.getDocumentName()), errorMessage);

			}
		};
	}

	private SelectionChangeEvent.Handler createSelectionChangeHandler() {
		return new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TemplateDto selected = selectionModel.getSelectedObject();
				if (selected != null) {
					current = selected;
					boolean isCurrentTemplateLocked = (current.getLockingUser() != null && current.getLockingUser().getUserId() != null);
					logger.fine("current template locking status = " + isCurrentTemplateLocked);
					display.setLockCommandVisible(!isCurrentTemplateLocked);
					display.setUnLockCommandVisible(isCurrentTemplateLocked);
				}
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

	private class InternalMappingPresenter extends MappingPresenter {

		@Override
		protected void onSubmitSuccess(TemplateDto template) {
			super.onSubmitSuccess(template);
			display.hideMappingDisplay();
			GrowlFactory.success(LABELS.message_templateUpdatedMessage());
			loadData();
		}

		@Override
		public void cancelEditMapping() {
			super.cancelEditMapping();
			display.hideMappingDisplay();
		}

		@Override
		protected void onInitMappingDataFailed(Throwable caught) {
			showError("", caught.getMessage());
			display.hideMappingDisplay();
			startFileUpload();
		}

	}
}
