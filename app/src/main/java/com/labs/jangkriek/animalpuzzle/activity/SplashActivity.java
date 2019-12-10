package com.labs.jangkriek.animalpuzzle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;

/**
 * The class to show splash screen
 * 
 * @author vishalbodkhe
 * 
 */
public class SplashActivity extends Activity {

	private Context mContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		mContext = SplashActivity.this;
		mHandler.sendEmptyMessageDelayed(1, AppConstants.SPLASH_DURATION);
	}

	/**
	 * Handler to post message for splash duration and launch the home screen
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			Intent intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mHandler != null) {
				mHandler.removeMessages(1);
				mHandler = null;
			}
			mContext = null;
			super.onDestroy();
		}
	}
}