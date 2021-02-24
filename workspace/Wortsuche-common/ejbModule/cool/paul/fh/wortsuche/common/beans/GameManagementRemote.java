package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Remote;

import cool.paul.fh.wortsuche.common.entity.Game;

@Remote
public interface GameManagementRemote extends GameManagement {

	@Deprecated
	public void $$createGameForTesting(Game game);
}
