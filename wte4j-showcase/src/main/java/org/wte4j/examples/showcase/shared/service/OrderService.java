/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j.examples.showcase.shared.service;

import java.util.List;

import org.wte4j.examples.showcase.shared.OrderDataDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("orderService")
public interface OrderService extends RemoteService {

	/**
	 * Lists orders
	 * 
	 * @return a List of orders
	 */
	List<OrderDataDto> listOrderData();

	/**
	 * create a document for a order
	 * 
	 * @param order
	 *            -the order
	 * @param documentName
	 *            - name of the template
	 * @param language
	 *            - the language
	 * @return path of the generated file
	 */
	String createDocument(OrderDataDto order, String documentName);

	/**
	 * Returns a list of templates available for order data
	 */
	List<String> listOrderTemplates();

	List<String> listDataModel();
}
