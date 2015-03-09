package ch.born.wte.ui.client.cell;

import com.google.gwt.core.shared.GWT;

public class CellTableResources {

	public static final Resources RESOURCES = GWT.create(Resources.class);

	private static final String EMTPY_CELL_TABLE_STYLE = "CellTable.css";

	public static interface Resources extends com.google.gwt.user.cellview.client.CellTable.Resources {

		@Source(EMTPY_CELL_TABLE_STYLE)
		com.google.gwt.user.cellview.client.CellTable.Style cellTableStyle();
	}
}
