package de.fh_dortmund.inf.cw.shop.client.test;

import org.junit.BeforeClass;

import de.fh_dortmund.inf.cw.shop.client.ServiceHandlerImpl;

public class PersonManagementTest {
	private static ServiceHandlerImpl serviceHandler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceHandler = ServiceHandlerImpl.getInstance();
	}
}
