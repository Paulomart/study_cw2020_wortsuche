package cool.paul.fh.wortsuche.client;

import java.util.List;

import cool.paul.fh.wortsuche.common.entity.Map;

public class TestHelper {

	public static Map getSmallMap(List<Map> maps) {
		return maps.stream().filter(x -> x.getWidth() == "AHELLO".length()).findFirst().orElse(null);
	}

	public static Map getBigMap(List<Map> maps) {
		return maps.stream().filter(x -> x.getWidth() == "GEXCEPTIONXPFW".length()).findFirst().orElse(null);
	}

}