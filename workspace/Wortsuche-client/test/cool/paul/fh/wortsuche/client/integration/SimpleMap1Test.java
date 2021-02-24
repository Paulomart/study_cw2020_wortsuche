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
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleMap1Test extends AbstractTwoPlayerTest {

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
	public void _06_select_word()
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		String foundWord1 = i1.selectWord(1, 0, 5, 0);
		assertEquals("HELLO", foundWord1);
		latch.await();

		assertEquals(1, i1.getGame().getSolvedWords().size());
		assertEquals(GameState.RUNNING, i1.getGame().getState());
		assertEquals(p2, i1.getGame().getCurrentTurn());
	}

	@Test(expected = WordAlreadySolvedException.class)
	public void _07_selected_already_solved_word()
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException {
		i2.selectWord(1, 0, 5, 0);
	}

	@Test(expected = NotYourTurnException.class)
	public void _08_illegal_turn_order() throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException {
		i1.selectWord(2, 0, 2, 3);
	}

	@Test
	public void _09_select_last_word()
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		String foundWord2 = i2.selectWord(2, 0, 2, 3);
		assertEquals("ESEL", foundWord2);
		latch.await();

		assertEquals(2, i1.getGame().getSolvedWords().size());
		assertEquals(GameState.FINISHED, i1.getGame().getState());
		assertEquals(null, i1.getGame().getCurrentTurn());
	}

	@Test
	public void _10_stop_game() throws NoGameFoundException {
		i1.stopGame();

		assertEquals(null, i1.getGame());
	}
}
