package cool.paul.fh.wortsuche.client;

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

public class GameInstanceTest {

	private Context ctx;
	private PlayerSessionRemote playerSession;
	private MapManagementRemote mapManagement;

	private GameInstanceRemote gameInstance;

	@Before
	public void initializeBeans() {
		try {
			ctx = new InitialContext();

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");

			playerSession = (PlayerSessionRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/PlayerSessionBean!cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote");
			mapManagement = (MapManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/MapManagementBean!cool.paul.fh.wortsuche.common.beans.MapManagementRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_newGame_vaild() throws GameAlreadyRunningException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
	}

	@Test(expected = GameAlreadyRunningException.class)
	public void test_newGame_already_running() throws GameAlreadyRunningException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
		gameInstance.newGame(map);
	}

	@Test
	public void test_resumeGame_vaild() throws GameAlreadyRunningException {
		gameInstance.resumeGame();
	}

	@Test(expected = GameAlreadyRunningException.class)
	public void test_resumeGame_already_running() throws GameAlreadyRunningException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
		gameInstance.resumeGame();
	}

	@Test(expected = NoGameFoundException.class)
	public void test_startGame_no_game_running() throws NoGameFoundException {
		gameInstance.start();
	}

	@Test
	public void test_startGame_valid() throws GameAlreadyRunningException, NoGameFoundException,
			PlayerNotFoundException, PlayerAlreadyJoinedException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
		playerSession.join("test");
		gameInstance.start();
	}

	@Test
	public void test_stopGame_valid() throws GameAlreadyRunningException, NoGameFoundException, PlayerNotFoundException,
			PlayerAlreadyJoinedException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
		playerSession.join("test");
		gameInstance.start();
		gameInstance.stop();
	}

	@Test(expected = NoGameFoundException.class)
	public void test_stopGame_no_game() throws NoGameFoundException {
		gameInstance.stop();
	}

	@Test
	public void test_stopGame_not_started() throws NoGameFoundException, GameAlreadyRunningException {
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());
		gameInstance.newGame(map);
		gameInstance.stop();
	}

	@After
	public void cleanup() {
		try {
			gameInstance.stop();
		} catch (NoGameFoundException e) {
		}
	}

}
