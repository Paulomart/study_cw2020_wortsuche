package de.fh_dortmund.inf.cw.shop.server.beans;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import de.fh_dortmund.inf.cw.person.server.beans.interfaces.PersonManagementLocal;
import de.fh_dortmund.inf.cw.person.server.entities.Person;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.OrderManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ShoppingCartManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ShoppingCartManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.VATCalculatorLocal;
import de.fh_dortmund.inf.cw.shop.server.entities.Order;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;

@Stateful
public class ShoppingCartManagementBean implements ShoppingCartManagementLocal, ShoppingCartManagementRemote {

	private List<Product> cart;
	private Person customer;
	
	@EJB
	private VATCalculatorLocal vatCalculator;
	
	@EJB
	private OrderManagementLocal orderManagement;
	
	@EJB
	private PersonManagementLocal personManagement;
	
	@PostConstruct
	private void init() {
		cart = new LinkedList<Product>();
	}
	
	@Override
	public List<Product> getCart() {
		return new LinkedList<Product>(cart);
	}

	@Override
	public void addToCart(Product product) {
		cart.add(product);
	}

	@Override
	public void removeFromCart(Product product) {
		cart.remove(product);
	}

	@Override
	public void clearCart() {
		cart.clear();
	}

	@Remove
	@Override
	public void destroyCart() {
	}

	@Override
	public double calculateCartNetAmount() {
		double amount = 0.;
		
		for(Product product : cart) {
			amount += product.getNetPrice();
		}
		return amount;
	}

	@Override
	public double calculateCartGrossAmount() {
		return vatCalculator.calculateVAT(calculateCartNetAmount());
	}

	@Override
	public void submitOrder() throws Exception {
		if (cart.size() == 0) {
			throw new Exception("Ihr Warenkorb ist leer.");
		}
		if (customer == null) {
			throw new Exception("Es sind keine Kundeninformationen vorhanden.");
		}
		
		Order order = new Order();
		
		for(Product product : cart) {
			order.addToOrderItems(product);
		}
		
		order.setInvoiceNetAmount(calculateCartNetAmount());
		order.setInvoiceGrossAmount(calculateCartGrossAmount());
		
		customer = personManagement.create(customer);
		order.setCustomer(customer);
		
		orderManagement.create(order);
		
		clearCart();
	}

	@Override
	public Person getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(Person customer) {
		this.customer = customer;		
	}

}
