package de.fh_dortmund.inf.cw.shop.server.entities;

import java.io.Serializable;

public class OrderItem implements Serializable {
	private int quantity;
	private double netPrice;
	private Product product;
		
	public OrderItem() {
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
