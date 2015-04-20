package api;

import java.util.ArrayList;

public class Team {

	// +--------+----------------------------------------------------------
	// | Fields |
	// +--------+

	/**
	 * holds the name of the team
	 */
	String name;
	/**
	 * Holds the abbreviation for the team name
	 */
	String abrev;
	
	/**
	 * Holds the information on the restrictions a school has
	 */
	Restrictions cantPlay;

	// +--------------+---------------------------------------------------
	// | Constructors |
	// +--------------+

	/**
	 * @param name, a String
	 * @param abrev, a String
	 */
	public Team(String name, String abrev) {
		this.name = name;
		this.abrev = abrev;
	}// team(String, String, ArrayList, ArrayList, ArrayList)

	// +---------+-----------------------------------------------------
	// | Methods |
	// +---------+

}// class Team

