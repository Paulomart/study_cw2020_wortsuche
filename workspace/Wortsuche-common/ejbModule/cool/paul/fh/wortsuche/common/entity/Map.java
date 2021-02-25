package cool.paul.fh.wortsuche.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = "Map.all", query = "SELECT m FROM Map m"),
		@NamedQuery(name = "Map.byId", query = "SELECT m FROM Map m WHERE m.id = :id") })
public class Map implements Serializable {

	private static final long serialVersionUID = -9101563976143461026L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int width;
	private int height;
	/**
	 * Contains width * height characters that make up the map. Encoded first x,
	 * then y. Access a field with characters[width * y + x].
	 */
	private String characters;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Word> words;

	public Map(int id, int width, int height, String characters, List<Word> words) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.characters = characters;
		this.words = words;
	}

	public Map(int width, int height, String characters, List<Word> words) {
		this.width = width;
		this.height = height;
		this.characters = characters;
		this.words = words;
	}

	public Map() {
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getId() {
		return id;
	}

	public List<Word> getWords() {
		return words;
	}

	public String getCharacters() {
		return characters;
	}

	public char getCharacter(int x, int y) throws IllegalArgumentException {
		if (x < 0) {
			throw new IllegalArgumentException("Index out of bounds " + x + " < 0 ");
		}
		if (y < 0) {
			throw new IllegalArgumentException("Index out of bounds " + y + " < 0 ");
		}
		if (x >= width) {
			throw new IllegalArgumentException("Index out of bounds " + x + " => " + width);
		}
		if (y >= height) {
			throw new IllegalArgumentException("Index out of bounds " + y + " => " + height);
		}

		return characters.toCharArray()[width * y + x];
	}

	public String print() {
		StringBuilder s = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				s.append(getCharacter(x, y));
			}
			s.append('\n');
		}
		return s.toString();
	}

	@Override
	public String toString() {
		return "Map [id=" + id + ", width=" + width + ", height=" + height + ", characters=" + characters + ", words="
				+ words + "]";
	}
}
