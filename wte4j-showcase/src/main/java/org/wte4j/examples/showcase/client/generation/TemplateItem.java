package org.wte4j.examples.showcase.client.generation;

import com.google.gwt.event.dom.client.ClickHandler;

public class TemplateItem {
	private String text;
	private ClickHandler clickHandler;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

}
