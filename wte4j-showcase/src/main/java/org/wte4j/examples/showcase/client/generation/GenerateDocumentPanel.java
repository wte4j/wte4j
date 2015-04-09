package org.wte4j.examples.showcase.client.generation;

import java.util.Date;
import java.util.List;

import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.wte4j.examples.showcase.shared.OrderDataDto;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;

public class GenerateDocumentPanel extends Composite implements GenerateDocumentDisplay {
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);
	private static NumberFormat numberFormat = NumberFormat.getCurrencyFormat("GBP");
	private static GenerateDocumentPanelUiBinder uiBinder = GWT.create(GenerateDocumentPanelUiBinder.class);

	@UiField
	CellTable<OrderDataDto> orderTable;

	@UiField
	Modal dialog;

	@UiField
	LinkedGroup templateList;

	public GenerateDocumentPanel() {
		this.initWidget(uiBinder.createAndBindUi(this));
		initOrderTable();
	}

	private void initOrderTable() {
		orderTable.addColumn(createNameColumn(), "Name");
		orderTable.addColumn(createAddressColumn(), "Address");
		orderTable.addColumn(createZipColumn(), "Zip");
		orderTable.addColumn(createCityColumn(), "City");
		orderTable.addColumn(createCountryColumn(), "Country");
		orderTable.addColumn(createOrderDateColumn(), "Ordered at");
		orderTable.addColumn(createDeliveryDateColumn(), "Delivered at");
		orderTable.addColumn(createAmountColumn(), "amount");
	}

	private Column<OrderDataDto, Number> createAmountColumn() {
		return new Column<OrderDataDto, Number>(new NumberCell(numberFormat)) {

			@Override
			public Number getValue(OrderDataDto orderData) {
				return orderData.getAmount();
			}
		};
	}

	private TextColumn<OrderDataDto> createNameColumn() {
		return new TextColumn<OrderDataDto>() {

			@Override
			public String getValue(OrderDataDto orderData) {
				return orderData.getFirstName() + " " + orderData.getLastName();
			}
		};
	}

	private Column<OrderDataDto, SafeHtml> createAddressColumn() {
		return new Column<OrderDataDto, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(OrderDataDto orderData) {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendEscapedLines(orderData.getAddress());
				return builder.toSafeHtml();
			}
		};
	}

	private TextColumn<OrderDataDto> createZipColumn() {
		return new TextColumn<OrderDataDto>() {

			@Override
			public String getValue(OrderDataDto orderData) {
				return orderData.getZip();
			}
		};
	}

	private TextColumn<OrderDataDto> createCityColumn() {
		return new TextColumn<OrderDataDto>() {

			@Override
			public String getValue(OrderDataDto orderData) {
				return orderData.getCity();
			}
		};
	}

	private TextColumn<OrderDataDto> createCountryColumn() {
		return new TextColumn<OrderDataDto>() {

			@Override
			public String getValue(OrderDataDto orderData) {
				return orderData.getCountry();
			}
		};
	}

	private Column<OrderDataDto, Date> createOrderDateColumn() {
		return new Column<OrderDataDto, Date>(new DateCell(dateFormat)) {

			@Override
			public Date getValue(OrderDataDto orderData) {
				return orderData.getOrderDate();
			}
		};
	}

	private Column<OrderDataDto, Date> createDeliveryDateColumn() {
		return new Column<OrderDataDto, Date>(new DateCell(dateFormat)) {

			@Override
			public Date getValue(OrderDataDto orderData) {
				return orderData.getDeliveryDate();
			}
		};
	}

	@Override
	public HasData<OrderDataDto> getOrderContainer() {
		return orderTable;
	}

	@Override
	public void setTemplateListItems(List<TemplateItem> templateItems) {
		templateList.clear();
		for (TemplateItem item : templateItems) {
			LinkedGroupItem groupItem = new LinkedGroupItem();
			groupItem.setText(item.getText());
			groupItem.addClickHandler(item.getClickHandler());
			templateList.add(groupItem);
		}
	}

	public void showTemplateList() {
		dialog.show();
	}

	public void hideTemplateList() {
		dialog.hide();
	}

	interface GenerateDocumentPanelUiBinder extends UiBinder<Widget, GenerateDocumentPanel> {
	}

}
