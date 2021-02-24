package cool.paul.fh.wortsuche.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Word implements Serializable {

	private static final long serialVersionUID = 962815033968087485L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int x1, x2;
	private int y1, y2;

	public Word(int id, int x1, int y1, int x2, int y2) {
		this.id = id;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public Word(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public Word() {
	}

	public String getString(Map map) throws IllegalArgumentException {
		StringBuilder s = new StringBuilder();
		for (int x = x1; x < x2 + 1; x++) {
			for (int y = y1; y < y2 + 1; y++) {
				s.append(map.getCharacter(x, y));
			}
		}
		return s.toString();
	}

	public int getId() {
		return id;
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Word [id=" + id + ", x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + "]";
	}

}
