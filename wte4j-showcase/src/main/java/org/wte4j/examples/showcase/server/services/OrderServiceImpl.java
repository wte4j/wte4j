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
package org.wte4j.examples.showcase.server.services;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.wte4j.Template;
import org.wte4j.TemplateEngine;
import org.wte4j.TemplateQuery;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private static final String ORDER_DATA_SELECT = "select "
			+ " o.id as id, o.amount as amount, o.order_date as order_date, o.delivery_date as delivery_date,"
			+ " p.first_name as first_name, p.last_name as last_name, p.address_line_1 as address_line_1, p.address_line_2 as address_line_2, p.zip as zip, p.city as city, p.country as country"
			+ " from PURCHASE_ORDER o"
			+ " join person p on p.id = o.PERSON_ID";

	@Autowired
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TemplateEngine templateEngine;

	public OrderDataDto getOrderData(long orderId) {
		String sql = ORDER_DATA_SELECT + " where id= ?";
		return getJdbcTemplate().queryForObject(sql, new OrderDataRowMapper(), orderId);
	}

	@Override
	public List<OrderDataDto> listOrderData() {
		return getJdbcTemplate().query(ORDER_DATA_SELECT, new OrderDataRowMapper());
	}

	@Override
	public List<String> listOrderTemplates() {
		TemplateQuery queryTemplates = templateEngine.getTemplateRepository().queryTemplates();
		List<Template<Object>> templates = queryTemplates.inputType(OrderDataDto.class).language("en").list();
		List<String> templateNames = new ArrayList<String>();
		for (Template<Object> template : templates) {
			templateNames.add(template.getDocumentName());
		}
		return templateNames;
	}

	@Override
	public String createDocument(OrderDataDto order, String documentName) {

		try {
			Path file = templateEngine.createDocument(documentName, "en", order);
			return file.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

	private static class OrderDataRowMapper implements RowMapper<OrderDataDto> {

		@Override
		public OrderDataDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderDataDto orderData = new OrderDataDto();

			orderData.setFirstName(rs.getString("first_name"));
			orderData.setLastName(rs.getString("last_name"));

			String addressLine1 = rs.getString("address_line_1");
			String addressLine2 = rs.getString("address_line_2");
			if (rs.wasNull()) {
				orderData.setAddress(addressLine1);
			}
			else {
				orderData.setAddress(addressLine1 + "\n" + addressLine2);
			}

			orderData.setZip(rs.getString("zip"));
			orderData.setCity(rs.getString("city"));
			orderData.setCountry(rs.getString("country"));
			orderData.setAmount(rs.getDouble("amount"));
			orderData.setOrderDate(rs.getTimestamp("order_date"));
			orderData.setDeliveryDate(rs.getTimestamp("delivery_date"));
			orderData.setOrderId(rs.getLong("id"));
			return orderData;

		}

	}

	@Override
	public List<String> listDataModel() {
		List<String> models = new ArrayList<String>();
		models.add(OrderDataDto.class.getName());
		return models;
	}

}
