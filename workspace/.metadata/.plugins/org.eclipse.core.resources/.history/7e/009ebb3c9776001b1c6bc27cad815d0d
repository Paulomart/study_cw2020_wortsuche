package cool.paul.fh.wortsuche.client.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import cool.paul.fh.wortsuche.common.beans.GameManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.entity.SolvedWord;
import cool.paul.fh.wortsuche.common.entity.Word;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResumeGameTest extends AbstractTwoPlayerTest {

	@Test
	public void _01_ensure_no_game_is_playing() {
		assertEquals(null, i1.getGame());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void _02_seed_game() throws NamingException {
		// in order to simulate a game resume, we need to restore a prepared object to
		// the database.

		p1 = new Player(212, "Eins");
		p2 = new Player(213, "Zwei");

		List<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);

		List<Word> words = new ArrayList<>();
		Word w = new Word(210, 1, 5, 0, 0);
		words.add(w);

		words.add(new Word(211, 2, 0, 2, 3));

		Set<SolvedWord> solvedWords = new HashSet<>();
		solvedWords.add(new SolvedWord(w, p1));

		Map map = new Map(209, 6, 5, "AHELLODESFGNDSEFGNDSLFGNAAAAAA", words);

		Game game = new Game(208, GameState.RUNNING, players, p2, map, solvedWords);

		InitialContext ctx = new InitialContext();
		GameManagementRemote gameManagement = (GameManagementRemote) ctx.lookup(
				"java:global/Wortsuche-ear/Wortsuche-ejb/GameManagementBean!cool.paul.fh.wortsuche.common.beans.GameManagementRemote");

		gameManagement.$$createGameForTesting(game);
	}

	@Test
	public void _03_resume_game()
			throws GameAlreadyRunningException, MapNotFoundException, NoGameFoundException, PlayerNotFoundException {
		i1.resumeGame();

		i1.join("Eins");
		i2.join("Zwei");

		assertEquals(GameState.RUNNING, i1.getGame().getState());
		assertEquals(p2, i1.getGame().getCurrentTurn());
	}

	@Test
	public void _04_select_last_word()
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException, InterruptedException {
		latch = new CountDownLatch(2);

		String foundWord = i2.selectWord(2, 0, 2, 3);
		assertEquals("ESEL", foundWord);
		latch.await();

		assertEquals(2, i2.getGame().getSolvedWords().size());
		assertEquals(GameState.FINISHED, i1.getGame().getState());
		assertEquals(GameState.FINISHED, i2.getGame().getState());
		assertEquals(null, i1.getGame().getCurrentTurn());
	}

	@Test
	public void _05_stop_game() throws NoGameFoundException {
		i1.stopGame();

		assertEquals(null, i1.getGame());
	}
}
