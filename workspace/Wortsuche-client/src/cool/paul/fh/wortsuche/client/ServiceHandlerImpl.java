package cool.paul.fh.wortsuche.client;

import java.util.Observable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import cool.paul.fh.wortsuche.common.beans.GameInstanceRemote;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

public class ServiceHandlerImpl extends Observable implements MessageListener {

	private Context ctx;

	private GameInstanceRemote gameInstance;
	private PlayerSessionRemote playerSession;

	private JMSContext jmsContext;
	private Topic observerTopic;
	private Queue customerRequestQueue;

	public ServiceHandlerImpl() {
		initializeBeans();
		initializeJMSConnections();
	}

	private void initializeBeans() {
		try {
			ctx = new InitialContext();

			gameInstance = (GameInstanceRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/GameInstanceBean!cool.paul.fh.wortsuche.common.beans.GameInstanceRemote");
			playerSession = (PlayerSessionRemote) ctx.lookup(
					"java:global/Wortsuche-ear/Wortsuche-ejb/PlayerSessionBean!cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	private void initializeJMSConnections() {
		try {
			// Common
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();

			// Topic
			observerTopic = (Topic) ctx.lookup("java:global/jms/GameStateUpdates");
			jmsContext.createConsumer(observerTopic).setMessageListener(this);

			// Queue
//			customerRequestQueue = (Queue) ctx.lookup("java:global/jms/CustomerRequestQueue");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessage(Message arg0) {
		setChanged();
		notifyObservers(arg0);
	}

	public Player join(String name) throws NoGameFoundException, PlayerNotFoundException {
		return playerSession.join(name);
	}

	public void newGame(int mapId) throws GameAlreadyRunningException, MapNotFoundException {
		gameInstance.newGame(mapId);
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
