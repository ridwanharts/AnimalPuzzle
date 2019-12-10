/**
 * 
 */
package com.labs.jangkriek.animalpuzzle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.labs.jangkriek.animalpuzzle.util.AppConstants;

/**
 * The class to customize textview for fonts.
 * 
 * @author vishalbodkhe
 * 
 */
public class CustomTitleTextview extends TextView {

	/** The Context for loading assets */
	private Context mContext;

	/** The string to hold ttf name */
	private String mTTFName;

	public CustomTitleTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTTFName = "title.otf";
		setTypeface();
	}

	/**
	 * This method used to set the typeface.
	 */
	private void setTypeface() {
		Typeface font = Typeface.createFromAsset(mContext.getAssets(), AppConstants.FONTS + mTTFName);
		setTypeface(font);
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}
}
