package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Local;

import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Map;

@Local
public interface GameManagementLocal extends GameManagement {

	Game newGame(Map map);

	Game updateGame(Game game);

	void deleteGame(Game game);

	Game resumeGame();
}
