/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.model;

import java.io.Serializable;

/**
 * Class to represent Score item
 * @author vishalbodkhe
 *
 */
public class ScoreItem implements Serializable {

	private String time;
	private String seconds;
	private String level;
	private int position;
	private String moves;
	private String hinttaken;

	/**
	 * @return the moves
	 */
	public String getMoves() {
		return moves;
	}

	/**
	 * @param moves
	 *            the moves to set
	 */
	public void setMoves(String moves) {
		this.moves = moves;
	}

	/**
	 * @return the hinttaken
	 */
	public String getHinttaken() {
		return hinttaken;
	}

	/**
	 * @param hinttaken
	 *            the hinttaken to set
	 */
	public void setHinttaken(String hinttaken) {
		this.hinttaken = hinttaken;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the seconds
	 */
	public String getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds
	 *            the seconds to set
	 */
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

}
