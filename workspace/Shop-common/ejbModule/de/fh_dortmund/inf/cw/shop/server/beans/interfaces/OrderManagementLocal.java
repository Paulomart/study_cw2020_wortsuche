package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.shop.server.entities.Order;

@Local
public interface OrderManagementLocal extends OrderManagement {
	public List<Order> readAllOrders();
	public Order create(Order order) throws Exception;
	public Order update(Order order);
	public void delete(Order order);
}
