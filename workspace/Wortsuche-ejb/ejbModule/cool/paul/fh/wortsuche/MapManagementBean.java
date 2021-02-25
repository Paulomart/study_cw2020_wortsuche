package cool.paul.fh.wortsuche;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cool.paul.fh.wortsuche.common.beans.MapManagementLocal;
import cool.paul.fh.wortsuche.common.beans.MapManagementRemote;
import cool.paul.fh.wortsuche.common.entity.Map;
import cool.paul.fh.wortsuche.common.entity.Word;

@Stateless
public class MapManagementBean implements MapManagementLocal, MapManagementRemote {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Map> getAllMaps() {
		TypedQuery<Map> query = entityManager.createNamedQuery("Map.all", Map.class);

		List<Map> maps = query.getResultList();
		return maps;
	}

	@Override
	public Map getMapById(int mapId) {
		TypedQuery<Map> query = entityManager.createNamedQuery("Map.byId", Map.class);
		query.setParameter("id", mapId);

		List<Map> maps = query.getResultList();

		if (maps.size() != 1) {
			return null;
		}
		return maps.get(0);
	}

	@Override
	public Map fromArray(String[] rows, int... words) {
		if (words.length % 4 != 0) {
			throw new IllegalArgumentException("Words must be multiple of 4.");
		}

		int width = rows[0].length();
		for (int i = 1; i < rows.length; i++) {
			if (rows[i].length() != width) {
				throw new IllegalArgumentException("All rows must be equal sized. Offending row: " + i);
			}
		}

		String mapString = Stream.of(rows).collect(Collectors.joining());
		int height = rows.length;

		int wordCount = words.length / 4;

		List<Word> wordsSet = new ArrayList<>(wordCount);
		for (int i = 0; i < words.length; i += 4) {

			int x1 = words[i];
			int y1 = words[i + 1];
			int x2 = words[i + 2];
			int y2 = words[i + 3];

			Word w = new Word(x1, y1, x2, y2);
			wordsSet.add(w);
		}

		Map map = new Map(width, height, mapString, wordsSet);

		entityManager.persist(map);

		return map;
	}

	@Override
	public void deleteMap(Map map) {
		entityManager.remove(map);
	}
}