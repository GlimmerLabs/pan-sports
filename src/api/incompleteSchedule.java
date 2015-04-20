package api;

public class incompleteSchedule {
	// +--------+----------------------------------------------------------
	// | Fields |
	// +--------+
	/**
	 * holds the date of a game
	 */
	Day[] playDates;

	/**
	 * the total number of teams
	 */
	int numTeams;

	// +--------------+---------------------------------------------------
	// | Constructors |
	// +--------------+
	/**
	 * 
	 * @param playDates
	 * @param numTeams
	 */
	public incompleteSchedule(Day[] playDates, int numTeams) {
		this.playDates = playDates;
	}// incompeleteSchedule()

	// +---------+-----------------------------------------------------
	// | Methods |
	// +---------+

	/**
	 * Checks if the schedule is complete Unimplemented
	 */
	public static boolean isComplete() {
		return false;
	}// isComplete()

}// class incompleteSchedule
