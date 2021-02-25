package cool.paul.fh.wortsuche;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import cool.paul.fh.wortsuche.common.entity.Player;
import cool.paul.fh.wortsuche.common.exception.NotYourTurnException;

public class IllegalExceptionDetector {

	@AroundInvoke
	public Object detectIllegalException(InvocationContext ctx) throws Exception {
		try {
			return ctx.proceed();
		} catch (NotYourTurnException e) {
			Player p = (Player) ctx.getParameters()[0];
			System.out.println("Player \"" + p.getName() + "\" did an illegal move.");
			throw e;
		}
	}

}
