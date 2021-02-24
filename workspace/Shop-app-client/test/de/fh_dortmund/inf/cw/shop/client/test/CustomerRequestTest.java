package de.fh_dortmund.inf.cw.shop.client.test;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.shop.client.ServiceHandlerImpl;

public class CustomerRequestTest {
	private static ServiceHandlerImpl serviceHandler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceHandler = ServiceHandlerImpl.getInstance();
	}
	
	@Test
	public void test_request() throws Exception {
		serviceHandler.sendCustomerRequest("Mustermann", "Max", "max@mustermann.text", "Nachricht...");
	}
}
