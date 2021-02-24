package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Remote;

import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.NoGameFoundException;
import cool.paul.fh.wortsuche.common.exception.PlayerNotFoundException;

@Remote
public interface PlayerSessionRemote extends PlayerSession {

	Player join(String name) throws NoGameFoundException, PlayerNotFoundException;

	Player getPlayer();

}
