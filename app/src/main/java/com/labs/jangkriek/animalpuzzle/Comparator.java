package com.labs.jangkriek.animalpuzzle;

import android.content.Context;

import com.labs.jangkriek.animalpuzzle.model.ScoreItem;

/**
 * The class to provide comparator features
 * @author vishalbodkhe
 *
 */
public class Comparator {
	private static Comparator comparator;

	/**
	 * Single instance of Comparator.
	 * 
	 * @param context
	 * @return
	 */
	public static Comparator getInstance(Context context) {
		if (comparator == null) {
			comparator = new Comparator();
		}

		return comparator;
	}

	@SuppressWarnings("rawtypes")
	public static class SortCompare implements java.util.Comparator {

		boolean ascending;

		public SortCompare(boolean mode) {
			ascending = mode;
		}

		@Override
		public int compare(Object lhs, Object rhs) {
			Long seconds1 = Long.parseLong(((ScoreItem) lhs).getSeconds());
			Long seconds2 = Long.parseLong(((ScoreItem) rhs).getSeconds());
			if (ascending) {
				return seconds1.compareTo(seconds2);
			} else {
				return seconds2.compareTo(seconds1);
			}
		}

	}

}