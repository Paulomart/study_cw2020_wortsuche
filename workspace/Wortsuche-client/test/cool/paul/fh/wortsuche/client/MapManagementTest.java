package cool.paul.fh.wortsuche.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Map;

public class MapManagementTest {

	private Context ctx;
	private JMSContext jmsContext;

	private MapManagementRemote mapManagement;
	private Queue mapCreationQueue;

	@Before
	public void prepareTest() {
		initializeBeans();
		initializeJMSConnections();
	}

	private void initializeBeans() {
		try {
			ctx = new InitialContext();

			mapManagement = (MapManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/MapManagementBean!cool.paul.fh.wortsuche.common.beans.MapManagementRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	private void initializeJMSConnections() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();

			mapCreationQueue = (Queue) ctx.lookup("java:global/jms/MapCreationQueue");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_getAllMaps_vaild() {
		mapManagement.getAllMaps().size();
	}

	@Test
	public void test_getMapById_invalid_mapId() {
		assertNull(mapManagement.getMapById(-1));
	}

	@Test
	public void test_getMapById_valid() {
		Map map = mapManagement.getAllMaps().get(0);

		assertNotNull(mapManagement.getMapById(map.getId()));
	}

	@Test
	public void test_createMap() throws JMSException, InterruptedException {
		int sizePre = mapManagement.getAllMaps().size();

		TextMessage textMessage = jmsContext.createTextMessage();
		textMessage.setIntProperty("width", 6);
		textMessage.setText("AHELLODESFGNDSEFGNDSLFGNAAAAAA");

		jmsContext.createProducer().send(mapCreationQueue, textMessage);

		Thread.sleep(5_000);

		int sizeNow = mapManagement.getAllMaps().size();

		assertEquals(sizePre + 1, sizeNow);
	}

}