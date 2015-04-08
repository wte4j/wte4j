package org.wte4j.examples.showcase.client.main;

import org.wte4j.examples.showcase.client.generation.GenerateDocumentDisplay;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPanel;
import org.wte4j.examples.showcase.client.generation.GenerateDocumentPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class MainPresenter {
	private MainDisplay mainDisplay;

	public void bind(MainDisplay aDisplay) {
		if (mainDisplay != null) {
			throw new IllegalStateException("Presenter allready bound");
		}
		mainDisplay = aDisplay;

		bindGenerateDocumentAction();
		bindManageTemplateAction();

	}

	private void bindManageTemplateAction() {
		mainDisplay.addManageTemplateClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				displayTemplatesContent();

			}
		});
	}

	private void bindGenerateDocumentAction() {
		mainDisplay.addGenerateDocumentClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displayGenerateDocumentContent();
			}
		});
	}

	public void displayTemplatesContent() {
		mainDisplay.setMainContent(new Label("Manage templates"));
		mainDisplay.setRightContent(new Label("Helptext manage templates"));
		mainDisplay.setGenerateDocumentActive(false);
		mainDisplay.setManageTemplatesActive(true);
	}

	public void displayGenerateDocumentContent() {
		GenerateDocumentDisplay contentDisplay = new GenerateDocumentPanel();
		GenerateDocumentPresenter contentPresenter = new GenerateDocumentPresenter();
		contentPresenter.bind(contentDisplay);

		mainDisplay.setMainContent(contentDisplay);
		mainDisplay.setRightContent(new Label("Helptext generate documents"));
		mainDisplay.setGenerateDocumentActive(true);
		mainDisplay.setManageTemplatesActive(false);

	}
}
