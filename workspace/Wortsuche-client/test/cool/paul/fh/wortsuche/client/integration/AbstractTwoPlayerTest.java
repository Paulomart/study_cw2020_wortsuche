package cool.paul.fh.wortsuche.client.integration;

import java.util.concurrent.CountDownLatch;

import org.junit.BeforeClass;

import cool.paul.fh.wortsuche.client.ServiceHandlerImpl;
import cool.paul.fh.wortsuche.common.entity.Player;

public abstract class AbstractTwoPlayerTest {

	static CountDownLatch latch;
	static ServiceHandlerImpl h1;
	static ServiceHandlerImpl h2;
	static Player p1;
	static Player p2;

	@BeforeClass
	public static void setup() {
		latch = new CountDownLatch(0);

		h1 = new ServiceHandlerImpl();
		h2 = new ServiceHandlerImpl();

		h1.addObserver((o, arg) -> {
			latch.countDown();
		});
		h2.addObserver((o, arg) -> {
			latch.countDown();
		});
	}
}
