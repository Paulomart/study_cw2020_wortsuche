package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Local;

import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

@Local
public interface GameInstanceLocal extends GameInstance {

	Player join(String name) throws NoGameFoundException, PlayerNotFoundException;

	void quit(Player player) throws NoGameFoundException;

}
