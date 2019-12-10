/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The class to read/write settings values in shared preference
 * 
 * @author vishalbodkhe
 * 
 */
public class SettingsPreferences {

	private static final String SETTING_PUZZLE_PREF = "setting_puzzle_pref";
	private static final String EMPTY_LOCATION = "empty_location";
	private static final String DIFFICULTY_LEVEL = "difficulty_level";
	private static final String SELECT_PHOTOS_DIRECTORY = "selet_photos_directory";
	private static final String SOUND_ONOFF = "sound_enable_disable";
	private static final String SHOW_HINT_ONOFF = "showhint_enable_disable";
	private static final String BACKGROUND_CHANGE = "background_change";
	private static final String PHOTO_POSITION = "photo_position";
	private static final String VIBRATION = "vibrate_status";
	private static final String ADS_REMOVED = "ads_removed";

	/**
	 * Method to set Remove ads from app.
	 * 
	 * @param context
	 * @param result
	 */
	public static void setAdsRemoved(Context context, boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ADS_REMOVED, result);
		prefEditor.commit();
	}

	/**
	 * Method to get Remove ads.
	 * 
	 * @param context
	 */
	public static boolean isAdsRemoved(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(ADS_REMOVED, false);
	}

	/**
	 * Method to set the vibrate on/off .
	 * 
	 * @param context
	 * @param result
	 */
	public static void setVibration(Context context, Boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(VIBRATION, result);
		prefEditor.commit();
	}

	/**
	 * Method to get the vibrate on/off .
	 * 
	 * @param context
	 * @return result value true false
	 */
	public static boolean getVibration(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(VIBRATION, AppConstants.DEFAULT_SOUND_SETTING);
	}

	/**
	 * Method to set photo position
	 * 
	 * @param context
	 * @param position
	 */
	public static void setPhotoPosition(Context context, int position) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(PHOTO_POSITION, position);
		prefEditor.commit();
	}

	/**
	 * Method to get photo position
	 * 
	 * @param context
	 * @return position from key PHOTO_POSITION
	 */
	public static int getPhotoPosition(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs
				.getInt(PHOTO_POSITION, AppConstants.DEFAULT_PHOTO_POSITION);
	}

	/**
	 * Method to set the Background Image .
	 * 
	 * @param context
	 */
	public static void setIndexBackground(Context context, int position) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(BACKGROUND_CHANGE, position);
		prefEditor.commit();
	}

	/**
	 * Method to get the Background Image.
	 * 
	 * @param context
	 * @return result value
	 */
	public static int getIndexBackground(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(BACKGROUND_CHANGE,
				AppConstants.DEFAULT_BACKGROUND_IMAGE);
	}

	/**
	 * Method to set the Empty Location .
	 * 
	 * @param context
	 */
	public static void setEmptyLocation(Context context, int location) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(EMPTY_LOCATION, location);
		prefEditor.commit();
	}

	/**
	 * Method to get the Empty Location.
	 * 
	 * @param context
	 * @return result value
	 */
	public static int getEmptyLocation(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(EMPTY_LOCATION, AppConstants.EMPTY_LOCATION_FIRST);
	}

	/**
	 * Method to set the Difficulty Level .
	 * 
	 * @param context
	 */
	public static void setDifficultyLevel(Context context, int level) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(DIFFICULTY_LEVEL, level);
		prefEditor.commit();
	}

	/**
	 * Method to get the DifficultyLevel.
	 * 
	 * @param context
	 * @return result value
	 */
	public static int getDifficultyLevel(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(DIFFICULTY_LEVEL,
				AppConstants.DIFFICULTY_LEVEL_EASY);
	}

	/**
	 * Method to set the Difficulty Level .
	 * 
	 * @param context
	 */
	public static void setPhotoDirectory(Context context, int path) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(SELECT_PHOTOS_DIRECTORY, path);
		prefEditor.commit();
	}

	/**
	 * Method to get the DifficultyLevel.
	 * 
	 * @param context
	 * @return result value
	 */
	public static int getPhotoDirectory(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(SELECT_PHOTOS_DIRECTORY, 0);
	}

	/**
	 * Method to set the Sound on/off .
	 * 
	 * @param context
	 * @param result
	 */
	public static void setSoundEnableDisable(Context context, Boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(SOUND_ONOFF, result);
		prefEditor.commit();
	}

	/**
	 * Method to get the Sound on/off .
	 * 
	 * @param context
	 * @return result value true false
	 */
	public static boolean getSoundEnableDisable(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs
				.getBoolean(SOUND_ONOFF, AppConstants.DEFAULT_SOUND_SETTING);
	}

	/**
	 * Method to set the Hint on/off .
	 * 
	 * @param context
	 * @param result
	 */
	public static void setHintEnableDisable(Context context, Boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(SHOW_HINT_ONOFF, result);
		prefEditor.commit();
	}

	/**
	 * Method to get the Hint on/off .
	 * 
	 * @param context
	 * @return result value true false
	 */
	public static boolean getHintEnableDisable(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(SHOW_HINT_ONOFF,
				AppConstants.DEFAULT_HINT_SETTING);
	}
}
