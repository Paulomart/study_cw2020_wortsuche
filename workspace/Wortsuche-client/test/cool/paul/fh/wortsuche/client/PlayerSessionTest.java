package cool.paul.fh.wortsuche.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerAlreadyJoinedException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

public class PlayerSessionTest {

	private Context ctx;
	private MapManagementRemote mapManagement;
	private GameInstanceRemote gameInstance;

	private PlayerSessionRemote playerSession;

	@Before
	public void initializeBeans() {
		try {
			ctx = new InitialContext();

			playerSession = (PlayerSessionRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/PlayerSessionBean!cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote");

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");
			mapManagement = (MapManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/MapManagementBean!cool.paul.fh.wortsuche.common.beans.MapManagementRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_join_vaild() throws NoGameFoundException, PlayerNotFoundException, GameAlreadyRunningException,
			PlayerAlreadyJoinedException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);

		playerSession.join("test");

		assertEquals("test", playerSession.getPlayer().getName());
	}

	@Test(expected = PlayerAlreadyJoinedException.class)
	public void test_join_invalid() throws NoGameFoundException, PlayerNotFoundException, GameAlreadyRunningException,
			PlayerAlreadyJoinedException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);

		playerSession.join("test");
		playerSession.join("test");

		assertEquals("test", playerSession.getPlayer().getName());
	}

	@Test
	public void test_getPlayer_invaild() {
		assertNull(playerSession.getPlayer());
	}

	@Test
	public void test_getPlayer_vaild() throws GameAlreadyRunningException, NoGameFoundException,
			PlayerNotFoundException, PlayerAlreadyJoinedException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);

		playerSession.join("test");
		assertNotNull(playerSession.getPlayer());
	}

	@After
	public void cleanup() {
		try {
			gameInstance.stop();
		} catch (NoGameFoundException e) {
		}
	}

}
