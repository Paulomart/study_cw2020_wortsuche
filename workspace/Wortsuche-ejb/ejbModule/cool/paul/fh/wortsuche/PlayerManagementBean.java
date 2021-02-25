package cool.paul.fh.wortsuche;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cool.paul.fh.wortsuche.common.beans.PlayerManagementLocal;
import cool.paul.fh.wortsuche.common.beans.PlayerManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Player;

@Stateless
public class PlayerManagementBean implements PlayerManagementRemote, PlayerManagementLocal {

	@PersistenceContext
	private EntityManager entityManager;

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

}
