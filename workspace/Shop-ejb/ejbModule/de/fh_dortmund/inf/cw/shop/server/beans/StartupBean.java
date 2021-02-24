package de.fh_dortmund.inf.cw.shop.server.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.ProductManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;

@Singleton
@Startup
public class StartupBean {

	@EJB
	private ProductManagementLocal productManagement;
	
	@PostConstruct
	private void init() {
		createDefaultProducts();
	}
	

	private void createDefaultProducts() {
		if (productManagement.readProducts().isEmpty()) {
			productManagement.create(buildProduct("Notebook", "Notebook Desc", 999., 10));
			productManagement.create(buildProduct("Tablet", "Tablet Desc", 499., 20));
			productManagement.create(buildProduct("Smartphone", "Smartphone Desc", 599., 25));
			productManagement.create(buildProduct("TV", "TV Desc", 1299., 5));
		}
	}
	
	private Product buildProduct(String name, String description, double netPrice, int numberOfItems) {
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setNetPrice(netPrice);
		product.setNumberOfItems(numberOfItems);
		
		return product;
	}
	
}
