package cool.paul.fh.wortsuche.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "Game.byState", query = "SELECT g FROM Game g WHERE g.state = :state") })
public class Game implements Serializable {

	private static final long serialVersionUID = 2610527497133019081L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private GameState state;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Player> players;
	private Player currentTurn;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Map map;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<SolvedWord> solvedWords;

	public Game(int id, GameState state, List<Player> players, Player currentTurn, Map map,
			Set<SolvedWord> solvedWords) {
		this.id = id;
		this.state = state;
		this.players = players;
		this.currentTurn = currentTurn;
		this.map = map;
		this.solvedWords = solvedWords;
	}

	public Game() {
	}

	public Game(List<Player> players, Map map) {
		this.players = new ArrayList<>(players);
		this.map = map;
		this.state = GameState.LOBBY;
		this.currentTurn = null;
	}

	public int getId() {
		return id;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Player getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Player currentTurn) {
		this.currentTurn = currentTurn;
	}

	public Map getMap() {
		return map;
	}

	public Set<SolvedWord> getSolvedWords() {
		return solvedWords;
	}

	public void setSolvedWords(Set<SolvedWord> solvedWords) {
		this.solvedWords = solvedWords;
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
		Game other = (Game) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", state=" + state + ", players=" + players + ", currentTurn=" + currentTurn
				+ ", map=" + map + ", solvedWords=" + solvedWords + "]";
	}

}