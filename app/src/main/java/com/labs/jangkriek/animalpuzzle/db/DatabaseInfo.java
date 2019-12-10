package com.labs.jangkriek.animalpuzzle.db;

import android.provider.BaseColumns;

/**
 * The Database tables information class.
 * 
 * @author vishalbodkhe
 * 
 */
public class DatabaseInfo {

	public final static String SCORES_TABLE = "Scores";

	/**
	 * Interface containing the Scores table columns constants.
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	public interface ScoresColumn extends BaseColumns {
		public static String LEVEL = "level";
		public static String TIME = "time";
		public static String SECONDS = "seconds";
		public static String MOVES = "moves";
		public static String HINTTAKEN = "hint_taken";
	}
}
