package cool.paul.fh.wortsuche.client;

import java.util.Arrays;

import javax.jms.JMSException;

import org.junit.BeforeClass;
import org.junit.Test;

import cool.paul.fh.wortsuche.client.ServiceHandlerImpl;
import cool.paul.fh.wortsuche.common.entity.Word;

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
		}, Arrays.asList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_createNewMap_invalid_02() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGNX", //
				"DSLFGN", //
				"AAAAAA", //
		}, Arrays.asList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_createNewMap_invalid_03() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGN", //
				"DSLFGN", //
				"AAAAAA", //
		}, Arrays.asList( //
				new Word(1, 0, 6, 0), //
				new Word(2, 0, 2, 3) //
		));
	}

	@Test
	public void test_createNewMap() throws JMSException, InterruptedException {
		handler.createNewMap(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGN", //
				"DSLFGN", //
				"AAAAAA", //
		}, Arrays.asList( //
				new Word(1, 0, 5, 0), //
				new Word(2, 0, 2, 3) //
		));
	}

}
