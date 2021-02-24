package cool.paul.fh.wortsuche.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
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

	public static Map fromArray(String[] rows, int... words) {
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

		return new Map(width, height, mapString, wordsSet);
	}

	@Override
	public String toString() {
		return "Map [id=" + id + ", width=" + width + ", height=" + height + ", characters=" + characters + ", words="
				+ words + "]";
	}
}
