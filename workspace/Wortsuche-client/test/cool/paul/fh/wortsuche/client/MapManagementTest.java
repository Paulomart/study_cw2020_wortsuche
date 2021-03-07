package cool.paul.fh.wortsuche.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Map;

public class MapManagementTest {

	private Context ctx;

	private MapManagementRemote mapManagement;

	@Before
	public void prepareTest() {
		initializeBeans();
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

}
