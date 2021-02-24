package de.fh_dortmund.inf.cw.shop.client.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.shop.client.ServiceHandlerImpl;

public class VATCalculatorTest {
	private static ServiceHandlerImpl serviceHandler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceHandler = ServiceHandlerImpl.getInstance();
	}
	
	@Test
	public void test_rate() {
		assertEquals(0.19, serviceHandler.getVATRate(), 0.);
	}
	
	@Test
	public void test__33_33() {
		// 33.33 * 1.19 = 39.6627 = 39.66
		assertEquals(39.66, serviceHandler.calculateVAT(33.33), 0.01);
	}
	
	@Test
	public void test__100_00() {
		// 100.00 * 1.19 = 119.00
		assertEquals(119.00, serviceHandler.calculateVAT(100.00), 0.01);
	}
}
