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

	/*
	 * Vor Beginn des Tests darf kein anderes Spiel laufen.
	 */
	@Test
	public void _01_ensure_no_game_is_playing() {
		assertEquals(null, h1.getGame());
	}

	/*
	 * Es wird eine Karte ausgewählt. Dann ein neues Spiel auf der Karte erzeugt.
	 * 
	 * Bei beiden Clieten sollte das Spiel in den Zustand LOBBY sein.
	 */
	@Test
	public void _02_start_new_game() throws GameAlreadyRunningException {
		Map map = TestHelper.getBigMap(h2.getAllMaps());
		h1.newGame(map);
		assertEquals(GameState.LOBBY, h1.getGame().getState());
		assertEquals(GameState.LOBBY, h2.getGame().getState());
	}

	/*
	 * Der erste Spieler kann dem Spiel ohne Fehler betreiten.
	 */
	@Test
	public void _03_join_player1() throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		p1 = h1.join("Eins");
	}

	/*
	 * Der zweite Spieler kann dem Spiel ohne Fehler betreiten.
	 */
	@Test
	public void _04_join_player4() throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		p2 = h2.join("Zwei");
	}

	/*
	 * Das Spiel wird gestartet.
	 * 
	 * Bei beiden Clienten sollte das Spiel den Zustand RUNNING haben. Der erste
	 * Spieler sollte dran sein.
	 */
	@Test
	public void _05_start_game() throws NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		h1.startGame();
		latch.await();

		assertEquals(GameState.RUNNING, h1.getGame().getState());
		assertEquals(GameState.RUNNING, h2.getGame().getState());
		assertEquals(p1, h1.getGame().getCurrentTurn());
	}

	/*
	 * Alle Wörter werden der Reihne nach gefunden. Es wird geprüft ob, der korrekte
	 * Spieler an der Reihne ist und das Spiel in dem richtigen Zustand ist.
	 * 
	 * Nach dieser Methode ist das Spiel beendet, der Zustand sollte FINISHED sein.
	 */
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

	/*
	 * Das Spiel wird gestoppt, danach sollte es nicht mehr über die Clienten
	 * abrufbar sein.
	 */
	@Test
	public void _07_stop_game() throws NoGameFoundException {
		h1.stopGame();

		assertEquals(null, h1.getGame());
		assertEquals(null, h2.getGame());
	}

}
