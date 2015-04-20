package api;

import java.time.LocalDate;
import java.util.ArrayList;

public class Restrictions {

	/*
	 * Should hold the restrictions that are possible
	 * 
	 */

	// +--------+----------------------------------------------------------
	// | Fields |
	// +--------+

	Team team;
	
	ArrayList<LocalDate> noPlayDates ;
	
	int maxDistance ;
	
	
	// +--------------+---------------------------------------------------
	// | Constructors |
	// +--------------+



	// +---------+-----------------------------------------------------
	// | Methods |
	// +---------+
	
	/**
	 * checks if the restrictions are met.
	 * currently unimplemented
	 * @return
	 */
	public boolean checkRestrictions (Team opponent)
	{
		return false;
	}//checkRestrictions
	
	
}
