package cool.paul.fh.wortsuche.seeder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import cool.paul.fh.wortsuche.common.beans.MapManagementLocal;

@Singleton
@Startup
public class MapSeederBean {

	@EJB
	private MapManagementLocal mapManagement;

	@PostConstruct
	private void init() {
		if (mapManagement.getAllMaps().isEmpty())
			createDefaultMaps();
	}

	private void createDefaultMaps() {
		mapManagement.fromArray(new String[] { //
				"AHELLO", //
				"DESFGN", //
				"DSEFGN", //
				"DSLFGN", //
				"AAAAAA", //
		}, //
				1, 0, 5, 0, //
				2, 0, 2, 3 //
		);

		mapManagement.fromArray(new String[] { //
				"GEXCEPTIONXPFW", //
				"FWIJAYIYKQYCQY", //
				"FACHHOCHSCHULE", //
				"IBEANNGOWALTYT", //
				"LDZKWHCZBLOZHA", //
				"TGVLNHGLOFCWQR", //
				"RYWARCTOCTAOXG", //
				"YLHSJGLYPALAKQ", //
				"QEUSJYENTITYKI", //
				"INTERFACEGPCGJ", //
				"COMPONENTWAREX", //
				"JAVABIBGJVLDTY", //
				"REMOTEACBNGUEG", //
				"XDORTMUNDPNTGN", //
		}, //
				1, 0, 9, 0, //
				0, 2, 13, 2, //
				1, 3, 4, 3, //
				3, 4, 3, 9, //
				10, 3, 10, 7, //
				6, 8, 11, 8, //
				0, 9, 8, 9, //
				0, 10, 12, 10, //
				0, 11, 3, 11, //
				0, 12, 5, 12, //
				1, 13, 8, 13 //
		);

	}

}
