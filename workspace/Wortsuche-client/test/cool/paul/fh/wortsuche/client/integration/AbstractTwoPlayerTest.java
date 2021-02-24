package cool.paul.fh.wortsuche.client.integration;

import java.util.concurrent.CountDownLatch;

import org.junit.BeforeClass;

import cool.paul.fh.wortsuche.client.ServiceHandlerImpl;
import cool.paul.fh.wortsuche.common.entity.Player;

public abstract class AbstractTwoPlayerTest {

	static CountDownLatch latch;
	static ServiceHandlerImpl i1;
	static ServiceHandlerImpl i2;
	static Player p1;
	static Player p2;

	@BeforeClass
	public static void setup() {
		latch = new CountDownLatch(0);

		i1 = new ServiceHandlerImpl();
		i2 = new ServiceHandlerImpl();

		i1.addObserver((o, arg) -> {
			latch.countDown();
		});
		i2.addObserver((o, arg) -> {
			latch.countDown();
		});
	}
}
