package de.fh_dortmund.inf.cw.shop.client;

import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.person.server.entities.Person;
import de.fh_dortmund.inf.cw.shop.client.shared.CustomerRequestHanlder;
import de.fh_dortmund.inf.cw.shop.client.shared.ObservableObject;
import de.fh_dortmund.inf.cw.shop.client.shared.ProductManagementHandler;
import de.fh_dortmund.inf.cw.shop.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.shop.client.shared.ShoppingCartManagementHandler;
import de.fh_dortmund.inf.cw.shop.client.shared.VATCalculatorHandler;
import de.fh_dortmund.inf.cw.shop.client.ui.Window;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ShoppingCartManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.VATCalculatorRemote;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;
import de.fh_dortmund.inf.cw.shop.server.enums.ObserverMessageType;

public class ServiceHandlerImpl extends ServiceHandler implements MessageListener, VATCalculatorHandler, ProductManagementHandler, ShoppingCartManagementHandler, CustomerRequestHanlder {
	private static ServiceHandlerImpl instance;
	
	private Context ctx;
	
	private JMSContext jmsContext;
	private Topic observerTopic;
	private Queue customerRequestQueue;
	
	private VATCalculatorRemote vatCalculator;
	private ProductManagementRemote productManagement;
	private ShoppingCartManagementRemote shoppingCartManagement;
	
	public static void main(String[] args) {
		Window.main(args);
	}
	
	private ServiceHandlerImpl() {	
		try {
			ctx = new InitialContext();	
			
			vatCalculator = (VATCalculatorRemote) ctx.lookup("java:global/Shop-ear/Shop-ejb/VATCalculatorBean!de.fh_dortmund.inf.cw.shop.server.beans.interfaces.VATCalculatorRemote");
			productManagement = (ProductManagementRemote) ctx.lookup("java:global/Shop-ear/Shop-ejb/ProductManagementBean!de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementRemote");
			shoppingCartManagement = (ShoppingCartManagementRemote) ctx.lookup("java:global/Shop-ear/Shop-ejb/ShoppingCartManagementBean!de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ShoppingCartManagementRemote");
		} catch (NamingException e) {
			e.printStackTrace();
		}
			
		initializeJMSConnections();
	}

	public static ServiceHandlerImpl getInstance() {
		if (ServiceHandlerImpl.instance == null) {
			ServiceHandlerImpl.instance = new ServiceHandlerImpl();
		}
	
 
		
		return ServiceHandlerImpl.instance;
	}

	// #########
	// ## JMS ##
	// #########
	
	private void initializeJMSConnections() {
		try {
			// Common
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();
			
			// Topic
			observerTopic = (Topic) ctx.lookup("java:global/jms/ObserverTopic");
			jmsContext.createConsumer(observerTopic).setMessageListener(this);
			
			// Queue
			customerRequestQueue = (Queue) ctx.lookup("java:global/jms/CustomerRequestQueue");
		} catch(Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}
	

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("MESSAGE RECEIVED!!");
			System.out.println(message);
			if (message.getJMSDestination().equals(observerTopic)) {
				int observerType = message.getIntProperty("OBSERVER_TYPE");
				
				if (observerType == ObserverMessageType.INVENTORY.ordinal()) {
					setChanged();
					notifyObservers(ObservableObject.INVENTORY);
				} else if (observerType == ObserverMessageType.CART.ordinal()) {
					setChanged();
					notifyObservers(ObservableObject.CART);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	// ###################
	// ## CustomerRequest ##
	// ###################
	
	@Override
	public void sendCustomerRequest(String name, String firstName, String mail, String message) throws Exception {
		try {
			TextMessage textMessage = jmsContext.createTextMessage();
			textMessage.setStringProperty("NAME", name);
			textMessage.setStringProperty("FIRST_NAME", firstName);
			textMessage.setStringProperty("MAIL", mail);
			textMessage.setText(message);
			
			jmsContext.createProducer().send(customerRequestQueue, textMessage);
		} catch (Exception ex) {
			throw new Exception("Ihre Nachricht konnte nicht verschickt werden.");
		}
	}

	// ###################
	// ## VatCalculator ##
	// ###################

	@Override
	public double calculateVAT(double amount) {
		return vatCalculator.calculateVAT(amount);
	}

	@Override
	public double getVATRate() {
		return vatCalculator.getVATRate();
	}

	// #######################
	// ## ProductManagement ##
	// #######################
	
	@Override
	public List<Product> getProducts() {
		return productManagement.readProducts();
	}

	// ###################
	// ## Shopping Cart ##
	// ###################
	@Override
	public List<Product> getCart() {
		return shoppingCartManagement.getCart();
	}

	@Override
	public void addToCart(Product product) {	
		shoppingCartManagement.addToCart(product);
		setChanged();
		notifyObservers(ObservableObject.CART);
	}

	@Override
	public void removeFromCart(Product product) {	
		shoppingCartManagement.removeFromCart(product);
		setChanged();
		notifyObservers(ObservableObject.CART);
	}

	@Override
	public void clearCart() {		
		shoppingCartManagement.clearCart();
		setChanged();
		notifyObservers(ObservableObject.CART);
	}

	@Override
	public void destroyCart() {		
		shoppingCartManagement.destroyCart();
	}

	@Override
	public Person getCustomer() {
		return shoppingCartManagement.getCustomer();
	}

	@Override
	public void setCustomer(Person customer) {		
		shoppingCartManagement.setCustomer(customer);
	}

	@Override
	public double calculateCartNetAmount() {
		return shoppingCartManagement.calculateCartNetAmount();
	}

	@Override
	public double calculateCartGrossAmount() {
		return shoppingCartManagement.calculateCartGrossAmount();
	}

	@Override
	public void submitOrder() throws Exception {	
		shoppingCartManagement.submitOrder();
	}


	
	
}
