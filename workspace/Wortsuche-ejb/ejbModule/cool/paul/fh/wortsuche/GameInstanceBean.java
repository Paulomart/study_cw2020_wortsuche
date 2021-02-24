package cool.paul.fh.wortsuche;

import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;

import cool.paul.fh.wortsuche.common.beans.GameInstanceLocal;
import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.GameManagementLocal;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.entity.SolvedWord;
import cool.paul.fh.wortsuche.common.entity.Word;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

@Singleton
public class GameInstanceBean implements GameInstanceRemote, GameInstanceLocal {

	public static final String TURN_TIMEOUT_TIMER_INFO = GameInstanceBean.class.getName() + "#TURN_TIMEOUT_TIMER_INFO";

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/GameStateUpdates")
	private Topic observerTopic;
	@Resource
	private TimerService timerService;

	@EJB
	private GameManagementLocal gameManagement;

	private boolean resumeGame = false;
	private Game game;
	private Timer turnTimeoutTimer;

	@Override
	public boolean resumeGame() throws GameAlreadyRunningException {
		if (game != null) {
			throw new GameAlreadyRunningException();
		}

		Game resueableGame = gameManagement.resumeGame();

		if (resueableGame == null) {
			return false;
		}

		resumeGame = true;
		game = resueableGame;

		return true;
	}

	@Override
	public void newGame(int mapId) throws GameAlreadyRunningException, MapNotFoundException {
		if (game != null) {
			throw new GameAlreadyRunningException();
		}

		resumeGame = false;
		game = gameManagement.newGame(mapId);
	}

	@Override
	public Player join(String name) throws NoGameFoundException, PlayerNotFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		if (resumeGame) {
			Player player = game.getPlayers().stream().filter(x -> x.getName().equals(name)).findAny().orElse(null);

			if (player == null) {
				throw new PlayerNotFoundException();
			}
			
			return player;
		}

		Player player = gameManagement.newPlayer(name);

		game.getPlayers().add(player);
		game = gameManagement.updateGame(game);

		notifyViaObserverTopic();

		return player;
	}

	@Override
	public void quit(Player player) throws NoGameFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		game.getPlayers().remove(player);
		gameManagement.deletePlayer(player);
		game = gameManagement.updateGame(game);
		notifyViaObserverTopic();
	}

	@Override
	public void start() throws NoGameFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		game.setState(GameState.RUNNING);
		nextTurn();
		game = gameManagement.updateGame(game);

		notifyViaObserverTopic();
	}

	@Override
	public void stop() throws NoGameFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		game = gameManagement.updateGame(game);
		gameManagement.deleteGame(game);
		game = null;

		notifyViaObserverTopic();
	}

	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public String selectWord(Player player, int x1, int y1, int x2, int y2)
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		if (!player.equals(game.getCurrentTurn())) {
			throw new NotYourTurnException();
		}

		Optional<Word> optWord = game.getMap().getWords().stream().filter((w) -> {
			return (w.getX1() == x1 && w.getY1() == y1 && w.getX2() == x2 && w.getY2() == y2)
					|| (w.getX1() == x2 && w.getY1() == y2 && w.getX2() == x1 && w.getY2() == y1);
		}).findAny();

		if (!optWord.isPresent()) {
			return null;
		}

		Word word = optWord.get();

		// is the word already solved?
		boolean wordWasAlreadySolved = game.getSolvedWords().stream().filter(x -> x.getWord().equals(word)).findAny()
				.isPresent();
		if (wordWasAlreadySolved) {
			throw new WordAlreadySolvedException();
		}

		game.getSolvedWords().add(new SolvedWord(word, player));
		game = gameManagement.updateGame(game);

		String wordAsString = word.getString(game.getMap());

		nextTurn();

		return wordAsString;
	}

	private void nextTurn() throws NoGameFoundException {
		if (game == null) {
			throw new NoGameFoundException();
		}

		Player current = game.getCurrentTurn();

		int nextPlayerIndex = 0;
		if (current != null) {
			int index = game.getPlayers().indexOf(current);
			nextPlayerIndex = index + 1;
		}
		if (nextPlayerIndex >= game.getPlayers().size()) {
			nextPlayerIndex = 0;
		}

		Player nextPlayer = game.getPlayers().get(nextPlayerIndex);
		game.setCurrentTurn(nextPlayer);

		if (game.getSolvedWords().size() >= game.getMap().getWords().size()) {
			game.setCurrentTurn(null);
			game.setState(GameState.FINISHED);
		}

		if (game.getCurrentTurn() != null) {
			setTurnTimeoutTimer();
		}

		game = gameManagement.updateGame(game);
		notifyViaObserverTopic();
	}

	private void resetTurnTimeoutTimer() {
		if (turnTimeoutTimer != null) {
			try {
				turnTimeoutTimer.cancel();
			} catch (Exception ignored) {
			}
			turnTimeoutTimer = null;
		}
	}

	private void setTurnTimeoutTimer() {
		resetTurnTimeoutTimer();

		turnTimeoutTimer = timerService.createTimer(10_000, TURN_TIMEOUT_TIMER_INFO);
	}

	@Timeout
	public void programmaticTimeout(Timer timer) {
		if (!timer.getInfo().equals(TURN_TIMEOUT_TIMER_INFO)) {
			return;
		}

		try {
			nextTurn();
		} catch (NoGameFoundException e) {
		}
	}

	private void notifyViaObserverTopic() {
		try {
			Message message = jmsContext.createMessage();
			message.setIntProperty("OBSERVER_TYPE", 1);

			jmsContext.createProducer().send(observerTopic, message);
		} catch (JMSException ex) {
			System.err.println("Error while notify observers via topic " + ex.getMessage());
		}
	}
}
