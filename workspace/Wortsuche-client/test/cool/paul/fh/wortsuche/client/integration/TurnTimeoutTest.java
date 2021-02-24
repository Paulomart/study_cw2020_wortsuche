package cool.paul.fh.wortsuche.client.integration;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TurnTimeoutTest extends AbstractTwoPlayerTest {

	@Test
	public void _01_ensure_no_game_is_playing() {
		assertEquals(null, i1.getGame());
	}

	@Test
	public void _02_start_new_game() throws GameAlreadyRunningException, MapNotFoundException {
		i1.newGame(1);
		assertEquals(GameState.LOBBY, i1.getGame().getState());
	}

	@Test
	public void _03_join_player1() throws NoGameFoundException, PlayerNotFoundException {
		p1 = i1.join("Eins");
	}

	@Test
	public void _04_join_player4() throws NoGameFoundException, PlayerNotFoundException {
		p2 = i2.join("Zwei");
	}

	@Test
	public void _05_start_game() throws NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		i1.startGame();
		latch.await();
		assertEquals(GameState.RUNNING, i1.getGame().getState());
		assertEquals(p1, i1.getGame().getCurrentTurn());
	}

	@Test
	public void _06_sleep_to_long() {
		try {
			Thread.sleep(10_500);
		} catch (Exception e) {
		}

		assertEquals(GameState.RUNNING, i1.getGame().getState());
		assertEquals(p2, i1.getGame().getCurrentTurn());
	}

	@Test
	public void _07_stop_game() throws NoGameFoundException {
		i1.stopGame();

		assertEquals(null, i1.getGame());
	}

}