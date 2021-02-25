package cool.paul.fh.wortsuche.client.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.CountDownLatch;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import cool.paul.fh.wortsuche.client.ServiceHandlerImpl;
import cool.paul.fh.wortsuche.client.TestHelper;
import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.entity.Word;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerAlreadyJoinedException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComplexMap2Test extends AbstractTwoPlayerTest {

	@Test
	public void _01_ensure_no_game_is_playing() {
		assertEquals(null, h1.getGame());
	}

	@Test
	public void _02_start_new_game() throws GameAlreadyRunningException {
		Map map = TestHelper.getBigMap(h2.getAllMaps());
		h1.newGame(map);
		assertEquals(GameState.LOBBY, h1.getGame().getState());
	}

	@Test
	public void _03_join_player1() throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		p1 = h1.join("Eins");
	}

	@Test
	public void _04_join_player4() throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		p2 = h2.join("Zwei");
	}

	@Test
	public void _05_start_game() throws NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		h1.startGame();
		latch.await();
		assertEquals(GameState.RUNNING, h1.getGame().getState());
		assertEquals(p1, h1.getGame().getCurrentTurn());
	}

	@Test
	public void _06_auto_play_all_words()
			throws InterruptedException, NotYourTurnException, WordAlreadySolvedException, NoGameFoundException {

		Player turnExpected = p1;
		ServiceHandlerImpl handlerImplExpected = h1;

		for (int i = 0; i < h1.getGame().getMap().getWords().size(); i++) {
			Word w = handlerImplExpected.getGame().getMap().getWords().get(i);

			assertEquals(GameState.RUNNING, h1.getGame().getState());
			assertEquals(turnExpected, handlerImplExpected.getGame().getCurrentTurn());

			latch = new CountDownLatch(2);
			String expectedWord = w.getString(handlerImplExpected.getGame().getMap());
			String foundWord = handlerImplExpected.selectWord(w.getX1(), w.getY1(), w.getX2(), w.getY2());

			assertNotNull(foundWord);
			assertEquals(expectedWord, foundWord);

			latch.await();

			assertEquals(i + 1, h1.getGame().getSolvedWords().size());

			if (turnExpected == p1) {
				turnExpected = p2;
				handlerImplExpected = h2;
			} else {
				turnExpected = p1;
				handlerImplExpected = h1;
			}
		}

		assertEquals(GameState.FINISHED, h1.getGame().getState());
	}

	@Test
	public void _07_stop_game() throws NoGameFoundException {
		h1.stopGame();

		assertEquals(null, h1.getGame());
	}

}
