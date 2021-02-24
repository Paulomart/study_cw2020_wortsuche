package cool.paul.fh.wortsuche.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SolvedWord implements Serializable {

	private static final long serialVersionUID = 8369638521875804165L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private Word word;
	private Player byPlayer;

	public SolvedWord() {
	}

	public SolvedWord(Word word, Player byPlayer) {
		this.word = word;
		this.byPlayer = byPlayer;
	}

	public Player getByPlayer() {
		return byPlayer;
	}

	public int getId() {
		return id;
	}

	public Word getWord() {
		return word;
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
		SolvedWord other = (SolvedWord) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SolvedWord [id=" + id + ", word=" + word.getId() + ", byPlayer=" + byPlayer + "]";
	}

}