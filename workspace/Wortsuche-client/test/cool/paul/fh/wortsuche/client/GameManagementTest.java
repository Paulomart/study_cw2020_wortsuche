package cool.paul.fh.wortsuche.client;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.GameManagementRemote;
import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerAlreadyJoinedException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

public class GameManagementTest {

	private Context ctx;
	private GameInstanceRemote gameInstance;
	private MapManagementRemote mapManagement;

	private GameManagementRemote gameManagement;

	@Before
	public void initializeBeans() {
		try {
			ctx = new InitialContext();

			gameManagement = (GameManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameManagementBean!cool.paul.fh.wortsuche.common.beans.GameManagementRemote");

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");
			mapManagement = (MapManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/MapManagementBean!cool.paul.fh.wortsuche.common.beans.MapManagementRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@SuppressWarnings("deprecation")
	public void test_join_vaild() throws NoGameFoundException, PlayerNotFoundException, GameAlreadyRunningException,
			PlayerAlreadyJoinedException {

		List<Player> players = new ArrayList<>();
		Map map = TestHelper.getSmallMap(mapManagement.getAllMaps());

		Game game = new Game(players, map);

		gameManagement.$$createGameForTesting(game);

		gameInstance.resumeGame();

		assertNotNull(gameInstance.getGame());
	}

	@After
	public void cleanup() {
		try {
			gameInstance.stop();
		} catch (NoGameFoundException e) {
		}
	}

}