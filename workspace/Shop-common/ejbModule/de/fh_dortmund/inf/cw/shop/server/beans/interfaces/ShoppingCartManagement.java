package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

import java.util.List;

import de.fh_dortmund.inf.cw.person.server.entities.Person;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;

public interface ShoppingCartManagement {
	public List<Product> getCart();
	public void addToCart(Product product);
	public void removeFromCart(Product product);
	public void clearCart();
	public void destroyCart();
	public Person getCustomer();
	public void setCustomer(Person customer);
	public double calculateCartNetAmount();
	public double calculateCartGrossAmount();
	public void submitOrder() throws Exception;
}
