package cool.paul.fh.wortsuche.common.beans;

import cool.paul.fh.wortsuche.common.entity.Game;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.GameAlreadyRunningException;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;
import cool.paul.fh.wortsuche.common.exception.WordAlreadySolvedException;

public interface GameInstance {

	void newGame(Map map) throws GameAlreadyRunningException;

	boolean resumeGame() throws GameAlreadyRunningException;

	void start() throws NoGameFoundException;

	void stop() throws NoGameFoundException;

	Game getGame();

	String selectWord(Player player, int x1, int y1, int x2, int y2)
			throws NotYourTurnException, WordAlreadySolvedException, NoGameFoundException;

}
