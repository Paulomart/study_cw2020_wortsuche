package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Local;

import cool.paul.fh.wortsuche.common.entity.Player;

@Local
public interface PlayerManagementLocal extends PlayerManagement {

	Player newPlayer(String name);

	void deletePlayer(Player player);

}
