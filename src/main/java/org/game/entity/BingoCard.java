package org.game.entity;

import jakarta.persistence.*;

@Entity
public class BingoCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
	private User player;
	
	@ManyToOne
	@JoinColumn(name="game_id",nullable=false)
	private GameSession gamesession;
	
	//storing 5*5 random number into string
	//database takes long time to store  big matrix so easy to insert it as string
	private String numbersGrid;
	
	//storing which spot is crossed out
	private String markedGrid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public GameSession getGamesession() {
		return gamesession;
	}

	public void setGamesession(GameSession gamesession) {
		this.gamesession = gamesession;
	}

	public String getNumbersGrid() {
		return numbersGrid;
	}

	public void setNumbersGrid(String numbersGrid) {
		this.numbersGrid = numbersGrid;
	}

	public String getMarkedGrid() {
		return markedGrid;
	}

	public void setMarkedGrid(String markedGrid) {
		this.markedGrid = markedGrid;
	}
	

}
