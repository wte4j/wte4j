package ch.born.wte.ui.client.templates;

import java.util.List;

import ch.born.wte.ui.client.MessageDialog;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.TemplateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

	void downLoadTemplate() {
		TemplateDto template = current;
		String url = GWT.getModuleBaseURL() + "documents/templates?" + "name=" + template.getDocumentName() + "&language=" + template.getLanguage();
		Window.open(url, "parent", "");
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
