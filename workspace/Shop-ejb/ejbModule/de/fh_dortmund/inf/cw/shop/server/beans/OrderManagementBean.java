package de.fh_dortmund.inf.cw.shop.server.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.OrderManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.OrderManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.entities.Order;
import de.fh_dortmund.inf.cw.shop.server.entities.OrderItem;
import de.fh_dortmund.inf.cw.shop.server.enums.OrderStatus;

@Singleton
@Startup
public class OrderManagementBean implements OrderManagementLocal, OrderManagementRemote {
	private long lastOrderNumber;
	private Map<Long, Order> orders;
	
	@EJB
	private ProductManagementLocal productManagmentBean;
	
	@PostConstruct
	private void init() {
		this.orders = new LinkedHashMap<Long, Order>();
	}
	
	@Override
	public List<Order> readAllOrders() {
		return new ArrayList<Order>(orders.values());
	}

	@Override
	public Order create(Order order) throws Exception {
		if (order == null) {
			throw new IllegalArgumentException("Order cannot be null.");
		}
		if (order.getOrderNumber() != 0) {
			throw new IllegalArgumentException("Order-Number must be 0.");
		}
	
		// Update Inventory (Stock is checked in "removeItemFromInventory")
		Collection<OrderItem> orderItems = order.getOrderItems().values();
		for (OrderItem item : orderItems) {
			productManagmentBean.removeItemFromInventory(item.getProduct(), item.getQuantity());
		}
		
		order.setOrderDate(new Date());
		order.setOrderNumber(++lastOrderNumber);
		order.setOrderStatus(OrderStatus.SHIPPED); // TODO auf SUBMITTED setzen, wenn die MessageDrivenBeans umgesetzt werden
		orders.put(order.getOrderNumber(), order);
				
		return order;
	}

	@Override
	public Order update(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Order cannot be null.");

		}
		if (order.getOrderNumber() < 0) {
			throw new IllegalArgumentException("Order-Number may not be negative.");
		}
		
		if (order.getOrderNumber() == 0) {
			order.setOrderNumber(++lastOrderNumber);
		}
		
		orders.put(order.getOrderNumber(), order);
		
		return order;
	}

	@Override
	public void delete(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Order cannot be null.");

		}
		if (order.getOrderNumber() < 0) {
			throw new IllegalArgumentException("Order-Number may not be negative.");
		}
		
		orders.remove(order.getOrderNumber());		
	}

}
