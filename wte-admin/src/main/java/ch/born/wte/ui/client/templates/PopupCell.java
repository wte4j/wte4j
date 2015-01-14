package ch.born.wte.ui.client.templates;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class PopupCell<T> implements Cell<T> {

	private Cell<T> contentCell;
	private PopupPanel popupPanel;
	private Set<String> consumedEvents;

	public PopupCell(PopupPanel popupPanel, Cell<T> contentCell) {
		this.contentCell=contentCell;
		this.popupPanel = popupPanel;
		
		consumedEvents = new HashSet<String>();
		if(contentCell.getConsumedEvents() != null){
			consumedEvents.addAll(contentCell.getConsumedEvents() );
		}		
		consumedEvents.add(BrowserEvents.CLICK);
		
	}

	@Override
	public void render(Context context, T value, SafeHtmlBuilder sb) {
		contentCell.render(context, value, sb);
	}

	@Override
	public void onBrowserEvent(Cell.Context context, Element parent, T value,
			NativeEvent event, ValueUpdater<T> valueUpdater) {
		contentCell.onBrowserEvent(context, parent, value, event, valueUpdater);
		EventTarget eventTarget = event.getEventTarget();
		if (!BrowserEvents.CLICK.equals(event.getType())) {
			return;
		}
		final Element domElement = Element.as(eventTarget);
		popupPanel.setPopupPositionAndShow(new PositionCallback() {			
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				popupPanel.setPopupPosition(domElement.getAbsoluteLeft(), domElement.getAbsoluteTop());
			}
		});		
	}

	public boolean dependsOnSelection() {
		return contentCell.dependsOnSelection();
	}

	public Set<String> getConsumedEvents() {
		return consumedEvents;
	}

	public boolean handlesSelection() {
		return contentCell.handlesSelection();
	}

	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, T value) {
		return contentCell.isEditing(context, parent, value);
	}

	public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context,
			Element parent, T value) {
		return contentCell.resetFocus(context, parent, value);
	}

	public void setValue(com.google.gwt.cell.client.Cell.Context context,
			Element parent, T value) {
		contentCell.setValue(context, parent, value);
	}
}
