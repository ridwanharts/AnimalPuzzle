/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.model;

import com.labs.jangkriek.animalpuzzle.activity.Tile;
import java.io.Serializable;

/**
 * The class to represent back step item
 * 
 * @author vishalbodkhe
 * 
 */
public class BackStepItem implements Serializable {

	private int misplaced;
	private int emptyIndex;
	private Tile[] tiles;

	/**
	 * @return the misplaced
	 */
	public int getMisplaced() {
		return misplaced;
	}

	/**
	 * @param misplaced
	 *            the misplaced to set
	 */
	public void setMisplaced(int misplaced) {
		this.misplaced = misplaced;
	}

	/**
	 * @return the emptyIndex
	 */
	public int getEmptyIndex() {
		return emptyIndex;
	}

	/**
	 * @param emptyIndex
	 *            the emptyIndex to set
	 */
	public void setEmptyIndex(int emptyIndex) {
		this.emptyIndex = emptyIndex;
	}

	/**
	 * @return the tiles
	 */
	public Tile[] getTiles() {
		return tiles;
	}

	/**
	 * @param tiles
	 *            the tiles to set
	 */
	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}

}
