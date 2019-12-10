package com.labs.jangkriek.animalpuzzle.manager;

import com.labs.jangkriek.animalpuzzle.R;

import java.util.ArrayList;

/**
 * The content manager class to store and provide the resources data to the
 * required activity.
 * 
 * @author vishalbodkhe
 * 
 */
public class ResourcesContentManager {

	private static ArrayList<Integer> mImageItemList;

	/** Singleton object of ContentManager */
	private static volatile ResourcesContentManager contentManager;

	private int[] imageResIds = new int[] {
			R.drawable.image1,
			R.drawable.image21,
			R.drawable.image31,
			R.drawable.image2,
			R.drawable.image22,
			R.drawable.image32,
			R.drawable.image3,
			R.drawable.image23,
			R.drawable.image33,
			R.drawable.image4,
			R.drawable.image24,
			R.drawable.image29,
			R.drawable.image5,
			R.drawable.image25,
			R.drawable.image30,
			R.drawable.image6,
			R.drawable.image26,
			R.drawable.image7,
			R.drawable.image27,
			R.drawable.image8,
			R.drawable.image28,
			R.drawable.image34
			/*R.drawable.image_2, R.drawable.image_3,
			R.drawable.image_4, R.drawable.image_5,
			R.drawable.image_6, R.drawable.image_7,
			R.drawable.image_8, R.drawable.image_9,
			R.drawable.image_10, R.drawable.image_11,
			R.drawable.image_12, R.drawable.image_13,
			R.drawable.image_14, R.drawable.image_15,
			R.drawable.image_16, R.drawable.image_17,
			R.drawable.image_18, R.drawable.image_19,
			R.drawable.image_20, R.drawable.image_21,
			R.drawable.image_22, R.drawable.image_23,
			R.drawable.image_24, R.drawable.image_25,*/
			 };

	/**
	 * Static block to init the resources content manager.
	 */
	static {
		contentManager = new ResourcesContentManager();
	}

	/**
	 * Method to get the instance of content manager
	 * 
	 * @return contentManager
	 */
	public static ResourcesContentManager getInstance() {
		if (contentManager == null) {
			contentManager = new ResourcesContentManager();
		}
		return contentManager;
	}

	/**
	 * Hidden constructor.
	 */
	private ResourcesContentManager() {
	}

	/**
	 * Method to get the background list
	 * 
	 * @return mBackgroundItemList
	 */
	public ArrayList<Integer> getImageItemList() {
		if (mImageItemList == null || mImageItemList.size() == 0) {
			mImageItemList=new ArrayList<Integer>();
			for (int i = 0; i < imageResIds.length; i++) {
				mImageItemList.add(imageResIds[i]);
			}
		}
		return mImageItemList;
	}

}
