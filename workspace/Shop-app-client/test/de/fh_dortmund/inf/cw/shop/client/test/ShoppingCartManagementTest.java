package de.fh_dortmund.inf.cw.shop.client.test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.fh_dortmund.inf.cw.person.server.entities.NaturalPerson;
import de.fh_dortmund.inf.cw.shop.client.ServiceHandlerImpl;
import de.fh_dortmund.inf.cw.shop.server.entities.Product;
import de.fh_dortmund.inf.cw.shop.server.enums.ObserverMessageType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingCartManagementTest implements Observer {

	private static ServiceHandlerImpl serviceHandler;
	private static List<Product> products;
	
	private CountDownLatch observerLatch;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceHandler = ServiceHandlerImpl.getInstance();
		products = serviceHandler.getProducts();
	}
	
	@Before
	public void setUp() throws Exception {
		serviceHandler.clearCart();
		serviceHandler.setCustomer(null);
		
		serviceHandler.addObserver(this);
		observerLatch = new CountDownLatch(1);
	}
	
	@After
	public void tearDown() throws Exception {
		serviceHandler.deleteObserver(this);
	}
	

	@Override
	public void update(Observable o, Object arg) {
		if (serviceHandler.equals(o) && arg.equals(ObserverMessageType.INVENTORY)) {
			observerLatch.countDown();
		}		
	}
	
	@Test
	public void test001_cart() {
		assertNotNull(serviceHandler.getCart());
		assertEquals(0, serviceHandler.getCart().size());
	}
	
	@Test
	public void test002_addToCart() {
		serviceHandler.addToCart(products.get(0));
		assertEquals(1,  serviceHandler.getCart().size());
		assertThat(serviceHandler.getCart(), contains(products.get(0)));
	}
	
	@Test
	public void test003_removeFromCart() {
		serviceHandler.addToCart(products.get(0));
		assertEquals(1,  serviceHandler.getCart().size());
		assertThat(serviceHandler.getCart(), contains(products.get(0)));
		
		serviceHandler.removeFromCart(products.get(0));
		assertEquals(0,  serviceHandler.getCart().size());
		assertThat(serviceHandler.getCart(), not(contains(products.get(0))));
	}
	
	@Test
	public void test004_clearCart() {
		serviceHandler.addToCart(products.get(0));
		assertEquals(1,  serviceHandler.getCart().size());
		
		serviceHandler.clearCart();
		assertEquals(0,  serviceHandler.getCart().size());
		assertThat(serviceHandler.getCart(), not(contains(products.get(0))));
	}
	
	@Test
	public void test005_setCustomer() {
		NaturalPerson customer = new NaturalPerson();
		serviceHandler.setCustomer(customer);
		assertEquals(customer, serviceHandler.getCustomer());
	}
	
	@Test
	public void test006_calcNetAmount() {
		serviceHandler.addToCart(products.get(0));
		assertEquals(products.get(0).getNetPrice(), serviceHandler.calculateCartNetAmount(), 0.1);
	}
	
	@Test
	public void test007_calcGrossAmount() {
		serviceHandler.addToCart(products.get(0));
		assertEquals(products.get(0).getNetPrice() * (1.0 + serviceHandler.getVATRate()), serviceHandler.calculateCartGrossAmount(), 0.1);
	}
	
	@Test(expected = Exception.class)
	public void test008_submitOrder_empty() throws Exception{
		serviceHandler.submitOrder();
	}
	
	@Test(expected = Exception.class)
	public void test009_submitOrder2_noCustomer() throws Exception {
		serviceHandler.addToCart(products.get(0));
		serviceHandler.submitOrder();
	}
	
	@Test//(timeout = 1000)
	public void test010_submitOrder3() throws Exception {
		Product productBefore = products.get(0);
		Product productAfter = null;
		int numberOfItemsBefore = productBefore.getNumberOfItems();
		int numberOfItemsAfter = -1;
		
		serviceHandler.addToCart(productBefore);
		serviceHandler.setCustomer(new NaturalPerson());
		serviceHandler.submitOrder();
		
		// Warten bis update aufgerufen wird.
		// await() blockiert so lange, wie der Count-Down-Wert größer als Null ist.
		observerLatch.await();
		
		productAfter = serviceHandler.getProducts().get(0);
		numberOfItemsAfter = productAfter.getNumberOfItems();
		assertEquals(productBefore, productAfter);
		assertEquals(numberOfItemsAfter, numberOfItemsBefore - 1);
	}
	
	@Test(expected = Exception.class)
	public void test011_destroyCart() {
		serviceHandler.destroyCart();
		serviceHandler.addToCart(products.get(0));
	}

}
