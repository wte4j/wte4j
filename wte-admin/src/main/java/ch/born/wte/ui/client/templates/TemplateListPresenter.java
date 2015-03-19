package ch.born.wte.ui.client.templates;

import java.util.List;

import ch.born.wte.ui.client.Application;
import ch.born.wte.ui.client.MessageDialog;
import ch.born.wte.ui.client.templates.TemplateUploadPresenter.FileUploadedHandler;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.TemplateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class TemplateListPresenter {

	private TemplateServiceAsync templateService = GWT
			.create(TemplateService.class);

	private TemplateListDisplay display;
	private TemplateDto current;

	private NoSelectionModel<TemplateDto> selectionModel;
	private ListDataProvider<TemplateDto> dataProvider;

	public TemplateListPresenter() {
		selectionModel = new NoSelectionModel<TemplateDto>();
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				current = selectionModel.getLastSelectedObject();
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

		display.setDowndLoadCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				downLoadTemplate();
			}
		});

		display.setLockCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				lockTemplate();

			}
		});

		display.setUnlockCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				unlockTemplate();

			}
		});
		display.setDeleteCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				deleteTemplate();

			}
		});
		display.setUpdateCommand(new ScheduledCommand() {

			@Override
			public void execute() {
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
				MessageDialog dialog = new MessageDialog("", caught
						.getMessage(), MessageDialog.ERROR);
				dialog.show();
			}
		});
	}

	void deleteTemplate() {
		final TemplateDto toRemove = current;
		String documentName = toRemove.getDocumentName();
		MessageDialog deleteConfirmationDialog = new MessageDialog(
				documentName, 
				Application.LABELS.deleteConfirmation(documentName), 
				MessageDialog.QUESTION, 
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

		PopupPanel updateTemplatePopupPanel = getUpdateTemplatePopupPanel();
		updateTemplatePopupPanel.show();
	}

	private PopupPanel getUpdateTemplatePopupPanel() {
		final PopupPanel updateTemplatePopupPanel = new PopupPanel();

		updateTemplatePopupPanel.add(getUpdateTemplateFormPanel(updateTemplatePopupPanel));
		updateTemplatePopupPanel.setGlassEnabled(true);
		updateTemplatePopupPanel.center();

		return updateTemplatePopupPanel;
	}

	private TemplateUploadDisplay getUpdateTemplateFormPanel(PopupPanel updateTemplatePopupPanel) {
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
			final PopupPanel updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
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

	private ClickHandler getSubmitButtonClickHandler(final PopupPanel updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
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

	private ClickHandler getCancelButtonClickHandler(final PopupPanel updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
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
		showDialog(title, message, MessageDialog.ERROR);
	}

	void showInfo(String title, String message) {
		showDialog(title, message, MessageDialog.INFO);
	}

	void showDialog(String title, String message, int dialogType) {
		new MessageDialog(title, message, dialogType).show();
	}
}
