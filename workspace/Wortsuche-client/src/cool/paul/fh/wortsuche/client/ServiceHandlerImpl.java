package cool.paul.fh.wortsuche.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.entity.Word;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerAlreadyJoinedException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

public class ServiceHandlerImpl extends Observable implements MessageListener {

	private Context ctx;

	private GameInstanceRemote gameInstance;
	private MapManagementRemote mapManagement;
	private PlayerSessionRemote playerSession;

	private JMSContext jmsContext;
	private Topic observerTopic;
	private Queue mapCreationQueue;

	public ServiceHandlerImpl() {
		initializeBeans();
		initializeJMSConnections();
	}

	private void initializeBeans() {
		try {
			ctx = new InitialContext();

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");
			mapManagement = (MapManagementRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/MapManagementBean!cool.paul.fh.wortsuche.common.beans.MapManagementRemote");
			playerSession = (PlayerSessionRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/PlayerSessionBean!cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	private void initializeJMSConnections() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();

			observerTopic = (Topic) ctx.lookup("java:global/jms/GameStateUpdates");
			jmsContext.createConsumer(observerTopic).setMessageListener(this);

			mapCreationQueue = (Queue) ctx.lookup("java:global/jms/MapCreationQueue");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessage(Message arg0) {
		setChanged();
		notifyObservers(arg0);
	}

	public void createNewMap(String[] rows, List<Word> words) throws JMSException {
		int width = rows[0].length();
		int height = rows.length;

		for (int i = 0; i < rows.length; i++) {
			if (rows[i].length() != width) {
				throw new IllegalArgumentException(
						"All rows must be of equal length. Offending row " + i + ", expected " + width);
			}
		}

		for (Word word : words) {
			if (word.getX1() >= width || word.getX1() < 0) {
				throw new IllegalArgumentException(word + " has illegal x1 coordinate " + word.getX1());
			}
			if (word.getX2() >= width || word.getX2() < 0) {
				throw new IllegalArgumentException(word + " has illegal x2 coordinate " + word.getX2());
			}
			if (word.getY1() >= height || word.getY1() < 0) {
				throw new IllegalArgumentException(word + " has illegal y1 coordinate " + word.getY1());
			}
			if (word.getY2() >= height || word.getY2() < 0) {
				throw new IllegalArgumentException(word + " has illegal y2 coordinate " + word.getY2());
			}
		}

		String joined = Stream.of(rows).collect(Collectors.joining());

		ObjectMessage objectMessage = jmsContext.createObjectMessage(new ArrayList<>(words));
		objectMessage.setIntProperty("width", width);
		objectMessage.setStringProperty("map", joined);

		jmsContext.createProducer().send(mapCreationQueue, objectMessage);
	}

	public Player join(String name) throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		return playerSession.join(name);
	}

	public Map getMapById(int mapId) {
		return mapManagement.getMapById(mapId);
	}

	public List<Map> getAllMaps() {
		return mapManagement.getAllMaps();
	}

	public void newGame(Map map) throws GameAlreadyRunningException {
		gameInstance.newGame(map);
	}

	public void startGame() throws NoGameFoundException {
		gameInstance.start();
	}

	public Game getGame() {
		return gameInstance.getGame();
	}

	public String selectWord(int x1, int y1, int x2, int y2)
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException {
		return gameInstance.selectWord(playerSession.getPlayer(), x1, y1, x2, y2);
	}

	public void stopGame() throws NoGameFoundException {
		gameInstance.stop();
	}

	public void resumeGame() throws GameAlreadyRunningException {
		gameInstance.resumeGame();
	}
}
