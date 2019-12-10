/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.model;

import java.io.Serializable;

/**
 * Class to represent favorite item
 * @author vishalbodkhe
 *
 */
@SuppressWarnings("serial")
public class FavoriteItem implements Serializable {

	private boolean isSelected;
	private String photoUrl;

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * @param photoUrl
	 *            the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
