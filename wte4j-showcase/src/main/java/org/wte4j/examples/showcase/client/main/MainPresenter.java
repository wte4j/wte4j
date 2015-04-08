package org.wte4j.examples.showcase.client.main;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class MainPresenter {
	private MainDisplay display;

	public void bind(MainDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("Presenter allready bound");
		}
		display = aDisplay;

		bindGenerateDocumentAction();
		bindManageTemplateAction();

	}

	private void bindManageTemplateAction() {
		display.addManageTemplateClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				displayTemplatesContent();

			}
		});
	}

	private void bindGenerateDocumentAction() {
		display.addGenerateDocumentClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displayGenerateDocumentContent();
			}
		});
	}

	public void displayTemplatesContent() {
		display.setMainContent(new Label("Manage templates"));
		display.setRightContent(new Label("Helptext manage templates"));
		display.setGenerateDocumentActive(false);
		display.setManageTemplatesActive(true);
	}

	public void displayGenerateDocumentContent() {
		display.setMainContent(new Label("Generate documents"));
		display.setRightContent(new Label("Helptext generate documents"));
		display.setGenerateDocumentActive(true);
		display.setManageTemplatesActive(false);

	}
}
