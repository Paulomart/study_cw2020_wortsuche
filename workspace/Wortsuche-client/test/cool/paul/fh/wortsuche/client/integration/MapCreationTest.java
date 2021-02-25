package cool.paul.fh.wortsuche.client.integration;

import javax.jms.JMSException;

import org.junit.BeforeClass;
import org.junit.Test;

import cool.paul.fh.wortsuche.client.ServiceHandlerImpl;

public class MapCreationTest {
	static ServiceHandlerImpl handler;

	@BeforeClass
	public static void setup() {
		handler = new ServiceHandlerImpl();
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_createNewMap_invalid_01() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLOX", //
				"DESFGN", //
				"DSEFGN", //
				"DSLFGN", //
				"AAAAAA", //
		});
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_createNewMap_invalid_02() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGNX", //
				"DSLFGN", //
				"AAAAAA", //
		});
	}

	@Test
	public void test_createNewMap() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGN", //
				"DSLFGN", //
				"AAAAAA", //
		});
	}
}