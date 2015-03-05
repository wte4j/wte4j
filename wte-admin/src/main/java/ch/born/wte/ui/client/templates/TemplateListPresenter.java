package ch.born.wte.ui.client.templates;

import java.util.List;

import ch.born.wte.ui.client.MessageDialog;
import ch.born.wte.ui.client.templates.TemplateUploadFormPanel.FileUploadedHandler;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.TemplateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
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
					"presenter is allready bound to a display");
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
		templateService.deleteTemplate(toRemove, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				dataProvider.getList().remove(toRemove);
				dataProvider.flush();
			}

			@Override
			public void onFailure(Throwable caught) {
				loadData();
				new MessageDialog("", caught.getMessage(), MessageDialog.ERROR)
						.show();
			}
		});
	}

	void lockTemplate() {
		final TemplateDto toLock = current;
		templateService.unlockTemplate(toLock, new AsyncCallback<TemplateDto>() {

			@Override
			public void onSuccess(TemplateDto updated) {
				replaceInList(toLock, updated);
			}

			@Override
			public void onFailure(Throwable caught) {
				showError(caught.getMessage());
			}
		});
	}

	void unlockTemplate() {
		final TemplateDto toUnlock = current;
		templateService.unlockTemplate(toUnlock, new AsyncCallback<TemplateDto>() {

			@Override
			public void onSuccess(TemplateDto updated) {
				replaceInList(toUnlock, updated);
			}

			@Override
			public void onFailure(Throwable caught) {
				showError(caught.getMessage());
			}
		});
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
	
	private Widget getUpdateTemplateFormPanel(PopupPanel updateTemplatePopupPanel) {
		TemplateUploadFormPanel updateTemplateFormPanel = new TemplateUploadFormPanel(current, getTemplateFileRestURL());
		PopupPanel uploadingPopup = getUploadingPopup();
		updateTemplateFormPanel.addSubmitButtonClickHandler(getSubmitButtonClickHandler(uploadingPopup));
		updateTemplateFormPanel.addCancelButtonClickHandler(getCancelButtonClickHandler(updateTemplatePopupPanel,uploadingPopup));
		updateTemplateFormPanel.addFileUploadedHandler(getFileUploadedHandler(updateTemplatePopupPanel, uploadingPopup));
		return updateTemplateFormPanel;
	}

	private FileUploadedHandler getFileUploadedHandler(
			final PopupPanel updateTemplatePopupPanel, final PopupPanel uploadingPopup) {
		return new FileUploadedHandler() {
			
			@Override
			public void onSuccess(String result) {
				uploadingPopup.hide();
				updateTemplatePopupPanel.hide();
				new MessageDialog("Template Updated", result, MessageDialog.INFO).show();
			}
			
			@Override
			public void onFailure(String errorMessage) {
				uploadingPopup.hide();
				new MessageDialog("Error", errorMessage, MessageDialog.ERROR).show();
			}
		};
	}

	private ClickHandler getSubmitButtonClickHandler(final PopupPanel uploadingPopup) {
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				uploadingPopup.show();
			}
		};
	}

	private PopupPanel getUploadingPopup() {
		PopupPanel updateTemplatePopupPanel = new PopupPanel();
		updateTemplatePopupPanel.center();
		updateTemplatePopupPanel.setGlassEnabled(true);
		updateTemplatePopupPanel.add(new Label("Uploading..."));
		return updateTemplatePopupPanel;
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

	void downLoadTemplate() {
		TemplateDto template = current;
		String url = getTemplateFileRestURL() + "?name=" + template.getDocumentName() + "&language=" + template.getLanguage();
		Window.open(url, "parent", "");
	}
	
	String getTemplateFileRestURL() {
		return GWT.getModuleBaseURL() + "documents/templates";
	}

	void replaceInList(TemplateDto toReplace, TemplateDto newTemplateDto) {
		int index = dataProvider.getList().indexOf(toReplace);
		if (index >= 0) {
			dataProvider.getList().set(index, newTemplateDto);
		}
	}

	void showError(String message) {
		new MessageDialog("", message, MessageDialog.ERROR).show();
	}

}
