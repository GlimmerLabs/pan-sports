package api;

import java.time.LocalDate;
import java.util.ArrayList;

/*
 * 	based off https://github.com/knoebber/secret-nemesis/blob/master/src/csc207/HWNA/scheduler/Day.java
 */

public class Day {

	// +--------+----------------------------------------------------------
	// | Fields |
	// +--------+
	/**
	 * holds the date of a game
	 */
	LocalDate date;

	/**
	 * holds all the games happening on that date
	 */
	ArrayList<Game> competing;

	// +--------------+---------------------------------------------------
	// | Constructors |
	// +--------------+
	/**
	 * 
	 * @param competing
	 * @param date
	 */

	public Day(ArrayList<Game> competing, LocalDate date) {
		this.competing = competing;
		this.date = date;
	}// Day

}// class Day
