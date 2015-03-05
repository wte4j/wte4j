package ch.born.wte.ui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class MessageDialog extends DialogBox {

    public static final int INFO = 0;
    public static final int WARN = 1;
    public static final int ERROR = 2;
    public static final int QUESTION = 3;

    private ClickHandler okHandler;


    public MessageDialog(String titel, String msg, int type, int width) {
        init(titel, msg, type, width);
    }

    public MessageDialog(String titel, String msg, int type) {
        init(titel, msg, type, 200);
    }

    public MessageDialog(String titel, String msg, int type, ClickHandler okHandler) {
        //this.type=type;
        this.okHandler = okHandler;
        init(titel, msg, type, 200);
    }

    private void init(String titel, String msg, int type, int width) {
        setText(titel);

        //DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setSpacing(10);

        final Image img;
        switch (type) {
            case WARN:
                img = new Image("images/warn.png");
                break;

            case ERROR:
                img = new Image("images/error.png");
                break;

            case QUESTION:
                img = new Image("images/question.png");
                break;

            default:
                img = new Image("images/info.png");
                break;
        }
        hPanel.add(img);

        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setSpacing(10);
        //dock.add(panel);

        Label msgLabel = new Label(msg);
        vPanel.add(msgLabel);

        if (type == QUESTION) {
            Grid grid = new Grid(1, 2);
            grid.setCellPadding(10);
            vPanel.add(grid);
            Button okButton = new Button("OK", new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    hide();
                    if (okHandler != null) {
                        okHandler.onClick(event);
                    }

                }

            });
            Button cancelButton = new Button("Cancel", new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    hide();

                }

            });
            grid.setWidget(0, 0, okButton);
            grid.setWidget(0, 1, cancelButton);
        }
        else {
            Button closeButton = new Button("Close", new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    hide();

                }

            });
            vPanel.add(closeButton);
        }

        //dock.setWidth("100%");
        vPanel.setWidth(width + "px");
        hPanel.add(vPanel);
        setWidget(hPanel);
        setGlassEnabled(true);
        center();
    }

}
