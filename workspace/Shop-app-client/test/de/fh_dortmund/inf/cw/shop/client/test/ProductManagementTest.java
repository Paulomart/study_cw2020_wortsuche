package de.fh_dortmund.inf.cw.shop.client.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.shop.client.ServiceHandlerImpl;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;

public class ProductManagementTest {
	private static ServiceHandlerImpl serviceHandler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceHandler = ServiceHandlerImpl.getInstance();
	}
	
	@Test
	public void test_products() {
		List<Product> products = serviceHandler.getProducts();
		assertNotNull(products);
		assertThat(products.size(), greaterThan(0));
		
		/*
		System.out.println("Products: ");
		for (Product p : serviceHandler.readProducts()) {
			System.out.println(p);
		}
		*/
	}
}
