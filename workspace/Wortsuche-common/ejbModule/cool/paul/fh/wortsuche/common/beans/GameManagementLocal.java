package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Local;

import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.MapNotFoundException;

@Local
public interface GameManagementLocal extends GameManagement {

	Game newGame(int mapId) throws MapNotFoundException;

	Game updateGame(Game game);

	Player newPlayer(String name);

	void deletePlayer(Player player);

	void deleteGame(Game game);

	Game resumeGame();
}