package api;

import java.time.LocalDate;

/*
 * Taken from 
 * https://github.com/harrietpz/Project/blob/master/src/grinnell/edu/project/Game.java
 */
public class Game {
	// +--------+----------------------------------------------------------
	// | Fields |
	// +--------+
	/**
	 * holds the date of a game
	 */
	LocalDate date;
	/**
	 * holds the first team
	 */
	Team away;
	/**
	 * holds the second team, the location of the game
	 */
	Team home;

	// +--------------+---------------------------------------------------
	// | Constructors |
	// +--------------+

	public Game(LocalDate date, Team team1, Team team2) {
		this.date = date;
		this.away = team1;
		this.home = team2;
	}// Game()

}// class Game
