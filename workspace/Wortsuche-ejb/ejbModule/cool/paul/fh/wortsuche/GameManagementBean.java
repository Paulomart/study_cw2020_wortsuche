package cool.paul.fh.wortsuche;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cool.paul.fh.wortsuche.common.beans.GameManagementLocal;
import cool.paul.fh.wortsuche.common.beans.GameManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;

@Stateless
public class GameManagementBean implements GameManagementLocal, GameManagementRemote {

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	private void init() {

//		createDefaultProducts();
	}

	@Override
	public Game newGame(int mapId) throws MapNotFoundException {
//		players = Collections.unmodifiableList(new ArrayList<>(players));

		Map map;
		if (mapId == 1) {
			map = Map.fromArray(new String[] { "AHELLO", "DESFGN", "DSEFGN", "DSLFGN", "AAAAAA", }, 1, 0, 5, 0, 2, 0, 2,
					3);

		} else if (mapId == 2) {
			map = Map.fromArray(new String[] { //
					"GEXCEPTIONXPFW", //
					"FWIJAYIYKQYCQY", //
					"FACHHOCHSCHULE", //
					"IBEANNGOWALTYT", //
					"LDZKWHCZBLOZHA", //
					"TGVLNHGLOFCWQR", //
					"RYWARCTOCTAOXG", //
					"YLHSJGLYPALAKQ", //
					"QEUSJYENTITYKI", //
					"INTERFACEGPCGJ", //
					"COMPONENTWAREX", //
					"JAVABIBGJVLDTY", //
					"REMOTEACBNGUEG", //
					"XDORTMUNDPNTGN", //
			}, //
					1, 0, 9, 0, //
					0, 2, 13, 2, //
					1, 3, 4, 3, //
					3, 4, 3, 9, //
					10, 3, 10, 7, //
					6, 8, 11, 8, //
					0, 9, 8, 9, //
					0, 10, 12, 10, //
					0, 11, 3, 11, //
					0, 12, 5, 12, //
					1, 13, 8, 13 //
			);
		} else {
			throw new MapNotFoundException();
		}

		Game game = new Game(Arrays.asList(), map);
		entityManager.persist(game);

		return game;
	}

	@Override
	public Game resumeGame() {
		TypedQuery<Game> query = entityManager.createNamedQuery("Game.byState", Game.class);
		query.setParameter("state", GameState.RUNNING);

		List<Game> runningGames = query.getResultList();
		if (!runningGames.isEmpty()) {
			return runningGames.get(0);
		}

		query = entityManager.createNamedQuery("Game.byState", Game.class);
		query.setParameter("state", GameState.LOBBY);

		List<Game> lobbyGames = query.getResultList();
		if (!lobbyGames.isEmpty()) {
			return lobbyGames.get(0);
		}

		return null;
	}

	@Override
	public Game updateGame(Game game) {
		game = entityManager.merge(game);
		entityManager.flush();
		return game;
	}

	@Override
	public void deleteGame(Game game) {
		entityManager.remove(game);
	}

	@Override
	public void deletePlayer(Player player) {
		entityManager.remove(player);
	}

	@Override
	public Player newPlayer(String name) {
		Player player = new Player(name);

		entityManager.persist(player);

		return player;
	}

	@Override
	public void $$createGameForTesting(Game game) {
		entityManager.persist(game);
	}
}