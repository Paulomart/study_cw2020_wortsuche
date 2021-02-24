package cool.paul.fh.wortsuche.client;

import static org.junit.Assert.assertEquals;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.GameManagementRemote;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

public class PlayerSessionTest {

	private Context ctx;

	private GameInstanceRemote gameInstance;
	private PlayerSessionRemote playerSession;
	private GameManagementRemote gameManagement;

	@Before
	public void initializeBeans() {
		try {
			ctx = new InitialContext();

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");
			playerSession = (PlayerSessionRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/PlayerSessionBean!cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote");
			gameManagement = (GameManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameManagementBean!cool.paul.fh.wortsuche.common.beans.GameManagementRemote");

		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_join_vaild() throws NoGameFoundException, PlayerNotFoundException {
		playerSession.join("test");

		assertEquals("test", playerSession.getPlayer().getName());
	}

	@Test(expected = GameAlreadyRunningException.class)
	public void test_newGame_already_running() throws GameAlreadyRunningException, MapNotFoundException {
		gameInstance.newGame(1);
		gameInstance.newGame(1);
	}

	@Test(expected = MapNotFoundException.class)
	public void test_newGame_invalid_map() throws GameAlreadyRunningException, MapNotFoundException {
		gameInstance.newGame(-1);
	}

	@Test
	public void test_resumeGame_vaild() throws GameAlreadyRunningException {
		gameInstance.resumeGame();
	}

	@Test(expected = GameAlreadyRunningException.class)
	public void test_resumeGame_already_running() throws GameAlreadyRunningException, MapNotFoundException {
		gameInstance.newGame(1);
		gameInstance.resumeGame();
	}

	@Test(expected = NoGameFoundException.class)
	public void test_startGame_no_game_running() throws NoGameFoundException {
		gameInstance.start();
	}

	@Test
	public void test_startGame_valid() throws GameAlreadyRunningException, MapNotFoundException, NoGameFoundException {
		gameInstance.newGame(1);
		gameInstance.start();
	}

	@Test
	public void test_stopGame_valid() throws GameAlreadyRunningException, MapNotFoundException, NoGameFoundException {
		gameInstance.newGame(1);
		gameInstance.start();
		gameInstance.stop();
	}

	@Test(expected = NoGameFoundException.class)
	public void test_stopGame_no_game() throws NoGameFoundException {
		gameInstance.stop();
	}

	@Test
	public void test_stopGame_not_started()
			throws NoGameFoundException, GameAlreadyRunningException, MapNotFoundException {
		gameInstance.newGame(1);
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