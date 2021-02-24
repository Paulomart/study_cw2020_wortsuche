package de.fh_dortmund.inf.cw.shop.server.beans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementRemote;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;
import de.fh_dortmund.inf.cw.shop.server.enums.ObserverMessageType;

@Stateless
public class ProductManagementBean implements ProductManagementLocal, ProductManagementRemote {

	@Inject
	private JMSContext jmsContext;
	
	@Resource(lookup = "java:global/jms/ObserverTopic")
	private Topic observerTopic;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Product> readProducts() {
		TypedQuery<Product> query = entityManager.createNamedQuery("Product.all", Product.class);
		
		List<Product> products = query.getResultList();
		
		return new ArrayList<Product>(products);
	}

	@Override
	public Product create(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null.");
		}
		
		entityManager.persist(product);
		entityManager.flush();
		
		notifyViaObserverTopic();
		
		return product;
	}

	@Override
	public Product update(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null.");
		}
		
		product = entityManager.merge(product);
		
		notifyViaObserverTopic();
		
		return product;
	}

	@Override
	public void delete(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null.");
		}
		if (product.getItemNumber() < 0) {
			throw new IllegalArgumentException("Item-Number may not be negative.");
		}
		
		entityManager.remove(product);
		
		notifyViaObserverTopic();
	}

	@Override
	public Product addItemToInventory(Product product, int quantity) {
		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null.");
		}
		if (product.getItemNumber() <= 0) {
			throw new IllegalArgumentException("Item-Number must be greater than 0.");
		}
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		}
		
		
		Product inventoryProduct = entityManager.find(Product.class, product.getItemNumber());
		
		if (inventoryProduct == null) {
			throw new IllegalArgumentException("Cannot find the product in the inventory.");
		}
		
		inventoryProduct.setNumberOfItems(inventoryProduct.getNumberOfItems() + quantity);
		
		return update(inventoryProduct);
	}
	
	@Override
	public Product removeItemFromInventory(Product product, int quantity) throws Exception {
		if (product == null) {
			throw new IllegalArgumentException("Product cannot be null.");
		}
		if (product.getItemNumber() <= 0) {
			throw new IllegalArgumentException("Item-Number must be greater than 0.");
		}
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		}
		
		Product inventoryProduct = entityManager.find(Product.class, product.getItemNumber());
		
		if (inventoryProduct == null) {
			throw new IllegalArgumentException("Cannot find the product in the inventory.");
		}
		
		int newNumberOfItems = inventoryProduct.getNumberOfItems() - quantity;
		
		if (newNumberOfItems >= 0) {
			inventoryProduct.setNumberOfItems(newNumberOfItems);
		} else {
			throw new Exception(String.format("There are no more items available (Product-Item-Number: %d)", product.getItemNumber()));
		}
		
		return update(inventoryProduct);
	}
	private void notifyViaObserverTopic() {
		try {
			Message message = jmsContext.createMessage();
			message.setIntProperty("OBSERVER_TYPE", ObserverMessageType.INVENTORY.ordinal());
		
			jmsContext.createProducer().send(observerTopic, message);
		} catch (JMSException ex) {
			System.err.println("Error while notify observers via topic " + ex.getMessage());
		}
	}
}
