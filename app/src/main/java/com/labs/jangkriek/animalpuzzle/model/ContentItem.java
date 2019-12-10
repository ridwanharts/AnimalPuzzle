/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to represent content item
 * 
 * @author vishalbodkhe
 * 
 */
@SuppressWarnings("serial")
public class ContentItem implements Serializable {

	private int count;
	private String folderName;
	private String folderPath;
	private String thumbnailUrl;
	private ArrayList<String> photosUrlList;

	/**
	 * @return the thumbnailUrl
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	/**
	 * @param thumbnailUrl
	 *            the thumbnailUrl to set
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	/**
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param folderPath
	 *            the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * @return the photosUrlList
	 */
	public ArrayList<String> getPhotosUrlList() {
		return photosUrlList;
	}

	/**
	 * @param photosUrlList
	 *            the photosUrlList to set
	 */
	public void setPhotosUrlList(ArrayList<String> photosUrlList) {
		this.photosUrlList = photosUrlList;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the folderName
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * @param folderName
	 *            the folderName to set
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

}
