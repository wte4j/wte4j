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
		display.setContent(new Label("Manage Templates"));
		display.setGenerateDocumentActive(false);
		display.setManageTemplatesActive(true);
	}

	public void displayGenerateDocumentContent() {
		display.setContent(new Label("generate documents"));
		display.setGenerateDocumentActive(true);
		display.setManageTemplatesActive(false);

	}
}
