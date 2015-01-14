package ch.born.wte.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainPanel extends Composite {

	private static MainPanelUiBinder uiBinder = GWT
			.create(MainPanelUiBinder.class);

	@UiField
	FlowPanel contentPanel;	

	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setContent(Widget widget){
		contentPanel.clear();
		contentPanel.add(widget);
	}
	
	
	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {
	}
}
