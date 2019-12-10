/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.model;

import java.io.Serializable;

/**
 * Class to represent Slideshow Selection  item.
 * 
 * @author vishalbodkhe
 * 
 */
public class SlideshowSelectionItem implements Serializable {

	private ContentItem contentItem;
	private boolean isSelected;

	/**
	 * @return the contentItem
	 */
	public ContentItem getContentItem() {
		return contentItem;
	}

	/**
	 * @param contentItem
	 *            the contentItem to set
	 */
	public void setContentItem(ContentItem contentItem) {
		this.contentItem = contentItem;
	}

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

}
