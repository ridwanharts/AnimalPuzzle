package com.labs.jangkriek.animalpuzzle.model;

import java.io.Serializable;

/**
 * Class to represent Background item.
 * 
 * @author vishalbodkhe
 * 
 */
public class BackgroundItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String backgroundTitleName;
	private int backgroundResId;

	/**
	 * @return the backgroundTitleName
	 */
	public String getBackgroundTitle() {
		return backgroundTitleName;
	}

	/**
	 * @param backgroundTitleName
	 *            the backgroundTitleName to set
	 */
	public void setBackgroundTitle(String backgroundTitleName) {
		this.backgroundTitleName = backgroundTitleName;
	}

	/**
	 * @return the backgroundResId
	 */
	public int getBackgroundResId() {
		return backgroundResId;
	}

	/**
	 * @param backgroundResId
	 *            the backgroundResId to set
	 */
	public void setBackgroundResId(int backgroundResId) {
		this.backgroundResId = backgroundResId;
	}

}
