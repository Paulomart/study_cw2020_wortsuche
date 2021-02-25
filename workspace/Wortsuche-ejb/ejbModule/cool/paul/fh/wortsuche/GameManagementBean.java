package cool.paul.fh.wortsuche;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cool.paul.fh.wortsuche.common.beans.GameManagementLocal;
import cool.paul.fh.wortsuche.common.beans.GameManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.GameState;
import cool.paul.fh.wortsuche.common.entity.Map;

@Stateless
public class GameManagementBean implements GameManagementLocal, GameManagementRemote {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Game newGame(Map map) {
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
	public void $$createGameForTesting(Game game) {
		entityManager.persist(game);
	}
}
