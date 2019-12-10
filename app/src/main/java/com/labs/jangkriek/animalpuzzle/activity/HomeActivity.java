/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;
import com.labs.jangkriek.animalpuzzle.R;
import com.roger.catloadinglibrary.CatLoadingView;

/**
 * The home screen class where the main features and menus are placed
 * 
 * @author vishalbodkhe
 * 
 */
public class HomeActivity extends AppCompatActivity {
	private Context mContext;
	private RelativeLayout bottom_ads_view;
	private AdView mAdView;
	private CatLoadingView catLoadingView;
	private RelativeLayout loading;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);
		mContext = HomeActivity.this;

		catLoadingView = new CatLoadingView();
		loading = findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
	}


	/**
	 * Method to get click actions on registered views.
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {

		final MediaPlayer mp = MediaPlayer.create(this, R.raw.swap_1);
		switch (view.getId()) {
		case R.id.rlstart:
			mp.start();
			loading.setVisibility(View.VISIBLE);
			mHandler.sendEmptyMessageDelayed(1, 1000);
			break;
		case R.id.setting_layout:
			loading.setVisibility(View.VISIBLE);
			mp.start();
			Intent settingIntent = new Intent(mContext, SettingsActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.slidepuzzle_scores_layout:
			loading.setVisibility(View.VISIBLE);
			mp.start();
			Intent scoreIntent = new Intent(mContext, ScoreActivity.class);
			startActivity(scoreIntent);
			break;
		case R.id.rlexit:
			loading.setVisibility(View.VISIBLE);
			mp.start();
			finish();
			System.exit(0);
			break;

		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			Intent intent = new Intent(mContext, ChoosePhotosGridActivity.class);
			startActivity(intent);
		}
	};

	public void showDialog() {
		catLoadingView.show(getSupportFragmentManager(), "");
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			bottom_ads_view = null;
			mContext = null;
			super.onDestroy();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loading.setVisibility(View.INVISIBLE);
	}

}
