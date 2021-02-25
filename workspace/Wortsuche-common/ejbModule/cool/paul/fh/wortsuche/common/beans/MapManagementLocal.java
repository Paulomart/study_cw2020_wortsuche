package cool.paul.fh.wortsuche.common.beans;

import javax.ejb.Local;

import cool.paul.fh.wortsuche.common.entity.Map;

@Local
public interface MapManagementLocal extends MapManagement {

	Map fromArray(String[] rows, int... words);

	void deleteMap(Map map);

}
