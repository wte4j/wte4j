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
}
