package cool.paul.fh.wortsuche.common.beans;

import java.util.List;

import cool.paul.fh.wortsuche.common.entity.Map;

public interface MapManagement {

	List<Map> getAllMaps();

	Map getMapById(int mapId);

}
