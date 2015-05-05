/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wte4j.ui.client.templates.mapping;

import static org.wte4j.ui.client.Application.LABELS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Strong;
import org.gwtbootstrap3.client.ui.html.Text;
import org.wte4j.ui.shared.MappingDto;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;

public class MappingPanel extends Composite implements
		MappingDisplay {

	private static MappingPanelUiBInder uiBinder = GWT
			.create(MappingPanelUiBInder.class);

	@UiField
	Alert alert;

	@UiField
	Strong mainMessage;

	@UiField
	FlowPanel detailMessages;

	@UiField
	CellTable<MappingDto> mappings;
	Column<MappingDto, String> contentControlIdColumn;
	Column<MappingDto, String> modelIdColumn;
	Column<MappingDto, String> formatterExpressionColumn;

	private List<String> modelIdOptions = new ArrayList<>();
	private ListDataProvider<MappingDto> dataProvider = new ListDataProvider<>();

	@UiField
	Pagination mappingsPagination;
	private SimplePager internalPager;

	@UiField
	Button cancelButton;

	@UiField
	Button saveButton;

	public MappingPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initMappingTable();

	}

	private void initMappingTable() {

		internalPager = new SimplePager();
		internalPager.setRangeLimited(true);
		internalPager.setPageSize(mappings.getPageSize());
		internalPager.setDisplay(mappings);

		initcontentControlIdColumnColumn();
		initModelColumn();
		initFormatterExpressionColumn();

		mappings.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
				mappingsPagination.rebuild(internalPager);
			}
		});

		mappings.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				mappingsPagination.rebuild(internalPager);
			}
		});

		dataProvider.addDataDisplay(mappings);
	}

	private void initcontentControlIdColumnColumn() {
		contentControlIdColumn = new TextColumn<MappingDto>() {
			@Override
			public String getValue(MappingDto mapping) {
				return mapping.getConentControlId();
			}
		};
		mappings.addColumn(contentControlIdColumn, LABELS.contentControlIdHeader());

	}

	private void initModelColumn() {
		int columnIndex = -1;
		if (modelIdColumn != null) {
			columnIndex = mappings.getColumnIndex(modelIdColumn);
			mappings.removeColumn(columnIndex);
		}

		modelIdColumn = createModelIdColumn();
		String heading = LABELS.modelIdHeader();
		if (columnIndex >= 0) {
			mappings.insertColumn(columnIndex, modelIdColumn, heading);
		} else {
			mappings.addColumn(modelIdColumn, heading);
		}
	}

	private Column<MappingDto, String> createModelIdColumn() {
		Column<MappingDto, String> column = new Column<MappingDto, String>(new SelectionCell(modelIdOptions)) {
			@Override
			public String getValue(MappingDto mapping) {
				return mapping.getModelKey();
			}
		};
		column.setFieldUpdater(new FieldUpdater<MappingDto, String>() {

			@Override
			public void update(int index, MappingDto mapping, String value) {
				mapping.setModelKey(value);
			}
		});
		return column;
	}

	private void initFormatterExpressionColumn() {
		formatterExpressionColumn = new Column<MappingDto, String>(new TextInputCell()) {
			@Override
			public String getValue(MappingDto mapping) {
				return mapping.getFormatterDefinition();
			}
		};
		formatterExpressionColumn.setFieldUpdater(new FieldUpdater<MappingDto, String>() {
			@Override
			public void update(int index, MappingDto mapping, String value) {
				mapping.setFormatterDefinition(value);
			}
		});

		mappings.addColumn(formatterExpressionColumn, LABELS.formatterColumnHeader());
	}

	@Override
	public void resetTable() {
		dataProvider.setList(Collections.<MappingDto> emptyList());
		mappings.setRowCount(1, false);
		mappings.setVisibleRangeAndClearData(mappings.getVisibleRange(), true);
	}

	@Override
	public void setMappingData(List<MappingDto> mappingData) {
		dataProvider.setList(mappingData);
	}

	@Override
	public void setModelIdOptions(List<String> someModelIdOptions) {
		modelIdOptions.clear();
		modelIdOptions.addAll(someModelIdOptions);
		initModelColumn();
		mappings.redraw();
	}

	@Override
	public void addSaveButtonClickHandler(ClickHandler clickHandler) {
		saveButton.addClickHandler(clickHandler);

	}

	@Override
	public void addCancelButtonClickHandler(ClickHandler clickHandler) {
		cancelButton.addClickHandler(clickHandler);
	}

	@Override
	public void hideAlert() {
		alert.setVisible(false);
		mainMessage.setText("");
		detailMessages.clear();
	}

	@Override
	public void showAlert(AlertType type, String message, List<String> details) {
		alert.setType(type);
		mainMessage.setText(message);
		detailMessages.clear();
		for (String detailMessage : details) {
			detailMessages.add(new Text(detailMessage));
		}
		alert.setVisible(true);
	}

	interface MappingPanelUiBInder extends UiBinder<Widget, MappingPanel> {
	}

}
