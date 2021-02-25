package cool.paul.fh.wortsuche;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import cool.paul.fh.wortsuche.common.beans.GameInstanceLocal;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionLocal;
import cool.paul.fh.wortsuche.common.beans.PlayerSessionRemote;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerAlreadyJoinedException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

@Stateful
public class PlayerSessionBean implements PlayerSessionRemote, PlayerSessionLocal {

	private Player player;

	@EJB
	private GameInstanceLocal gameBean;

	public Player join(String name) throws NoGameFoundException, PlayerNotFoundException, PlayerAlreadyJoinedException {
		if (player != null) {
			throw new PlayerAlreadyJoinedException();
		}

		player = gameBean.join(name);

		return player;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Remove
	private void preDestory() {
		if (player == null) {
			return;
		}

		try {
			gameBean.quit(player);
		} catch (NoGameFoundException ignored) {
			// ignore
		}
	}

}
