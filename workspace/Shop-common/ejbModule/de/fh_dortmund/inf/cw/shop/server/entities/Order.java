package de.fh_dortmund.inf.cw.shop.server.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import de.fh_dortmund.inf.cw.person.server.entities.Person;
import de.fh_dortmund.inf.cw.shop.server.enums.OrderStatus;

public class Order implements Serializable {
	private long orderNumber;
	private Date orderDate;
	private Map<Product, OrderItem> orderItems;
	private double invoiceNetAmount;
	private double invoiceGrossAmount;
	private OrderStatus orderStatus;
	private Person customer;
	
	public Order() {
		orderItems = new LinkedHashMap<Product, OrderItem>();
	}
	
	public void addToOrderItems(Product product) {
		if (orderItems.containsKey(product)) {
			OrderItem orderItem = orderItems.get(product);
			orderItem.setQuantity(
					orderItem.getQuantity() + 1
					);
		}
		else {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			orderItem.setNetPrice(product.getNetPrice());
			orderItem.setQuantity(1);
			
			addOrderItem(orderItem);
		}
	}
	
	public void removeFromOrderItems(Product product) {
		
	}
	
	public void addOrderItem(OrderItem item) {
		orderItems.put(item.getProduct(), item);
	}

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getInvoiceNetAmount() {
		return invoiceNetAmount;
	}

	public void setInvoiceNetAmount(double invoiceNetAmount) {
		this.invoiceNetAmount = invoiceNetAmount;
	}

	public double getInvoiceGrossAmount() {
		return invoiceGrossAmount;
	}

	public void setInvoiceGrossAmount(double invoiceGrossAmount) {
		this.invoiceGrossAmount = invoiceGrossAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Person getCustomer() {
		return customer;
	}

	public void setCustomer(Person customer) {
		this.customer = customer;
	}

	public Map<Product, OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Map<Product, OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (orderNumber ^ (orderNumber >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderNumber != other.orderNumber)
			return false;
		return true;
	}	
}
