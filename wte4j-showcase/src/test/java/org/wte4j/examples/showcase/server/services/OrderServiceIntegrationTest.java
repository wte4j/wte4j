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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.wte4j.examples.showcase.IntegrationTestApplicationConfig;
import org.wte4j.examples.showcase.shared.OrderDataDto;
import org.wte4j.examples.showcase.shared.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(classes = { IntegrationTestApplicationConfig.class })
public class OrderServiceIntegrationTest {

	@Autowired
	private OrderService orderService;

	@Test
	public void testListOrderData() {
		List<OrderDataDto> orderData = orderService.listOrderData();
		assertEquals(4, orderData.size());
	}

	@Test
	public void testListOrderTemplates() {
		List<String> templates = orderService.listOrderTemplates();
		assertEquals(1, templates.size());
	}

	@Test
	public void testCreateDocument() {
		String documentPath = orderService.createDocument(new OrderDataDto(), "Order Confirmation");
		File document = new File(documentPath);
		boolean fileExists = document.exists();
		document.delete();
		assertTrue(fileExists);
	}
}
