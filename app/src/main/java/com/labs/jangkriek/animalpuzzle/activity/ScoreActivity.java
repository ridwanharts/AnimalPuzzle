/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.labs.jangkriek.animalpuzzle.BaseActivity;
import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.db.ScoresDataProvider;
import com.labs.jangkriek.animalpuzzle.model.ScoreItem;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;
import com.labs.jangkriek.animalpuzzle.util.SettingsPreferences;

/**
 * The score activity to show score of game
 * 
 * @author vishalbodkhe
 * 
 */
public class ScoreActivity extends BaseActivity {
	private Context mContext;
	private GetHighScoreTask mGetHighScoreTask;
	private ProgressDialog mProgressDialog;
	private ScoreItem mEasyScoreItems;
	private ScoreItem mHardScoreItems;
	private ScoreItem mMediumScoreItems;
	private TextView mEasyScoreTextView;
	private TextView mMediumScoreTextView;
	private TextView mHardScoreTextView;
	private RelativeLayout bottom_ads_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_layout);
		mContext = ScoreActivity.this;
		initViews();
		initAds();
		startGetScoreTask();
	}

	/**
	 * Inits the add
	 */
	private void initAds() {
		if (SettingsPreferences.isAdsRemoved(mContext)) {
			bottom_ads_view.setVisibility(View.GONE);
		} else {
			bottom_ads_view.setVisibility(View.VISIBLE);
			AppUtils.setAdsView(ScoreActivity.this, bottom_ads_view);
		}
	}

	/**
	 * Inits the views and data to show.
	 */
	private void initViews() {
		bottom_ads_view = (RelativeLayout) findViewById(R.id.bottom_ads_view);
		mEasyScoreTextView = (TextView) findViewById(R.id.easy_highscore_textview);
		mMediumScoreTextView = (TextView) findViewById(R.id.medium_highscore_textview);
		mHardScoreTextView = (TextView) findViewById(R.id.hard_highscore_textview);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getResources().getString(
				R.string.loading_high_score));
	}

	/**
	 * Method to get click actions on registered views.
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {
		switch (view.getId()) {
			case R.id.easy_level_layout:
				Intent easyIntent = new Intent(mContext, ScoreDetailActivity.class);
				easyIntent.putExtra(AppConstants.EXTRA_LEVEL_STRING,
						AppConstants.DIFFICULTY_LEVEL_EASY);
				startActivityForResult(easyIntent,
						AppConstants.REQUEST_CODE_SCORE_DETAILS);
				break;
			case R.id.very_easy_level_layout:
				Intent veryEasyIntent = new Intent(mContext, ScoreDetailActivity.class);
				veryEasyIntent.putExtra(AppConstants.EXTRA_LEVEL_STRING,
						AppConstants.DIFFICULTY_LEVEL_VERY_EASY);
				startActivityForResult(veryEasyIntent,
						AppConstants.REQUEST_CODE_SCORE_DETAILS);
				break;
			case R.id.medium_level_layout:
				Intent mediumIntent = new Intent(mContext,
						ScoreDetailActivity.class);
				mediumIntent.putExtra(AppConstants.EXTRA_LEVEL_STRING,
						AppConstants.DIFFICULTY_LEVEL_MEDIUM);
				startActivityForResult(mediumIntent,
						AppConstants.REQUEST_CODE_SCORE_DETAILS);
				break;
			case R.id.hard_level_layout:
				Intent hardIntent = new Intent(mContext, ScoreDetailActivity.class);
				hardIntent.putExtra(AppConstants.EXTRA_LEVEL_STRING,
						AppConstants.DIFFICULTY_LEVEL_HARD);
				startActivityForResult(hardIntent,
						AppConstants.REQUEST_CODE_SCORE_DETAILS);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			if (AppConstants.REQUEST_CODE_SCORE_DETAILS == requestCode) {
				startGetScoreTask();
			}
		}
	}

	/**
	 * Method to get score list item
	 */
	private void startGetScoreTask() {
		if (mGetHighScoreTask != null) {
			mGetHighScoreTask.cancel(true);
			mGetHighScoreTask = null;
		}
		mGetHighScoreTask = new GetHighScoreTask();
		mGetHighScoreTask.execute();
	}

	/**
	 * The class to get high score list and shows the progress dialog.
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	public class GetHighScoreTask extends AsyncTask<Void, Void, Boolean> {

		public GetHighScoreTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				mEasyScoreItems = ScoresDataProvider.getHighScore(mContext,
						AppConstants.LEVEL_EASY);
				mMediumScoreItems = ScoresDataProvider.getHighScore(mContext,
						AppConstants.LEVEL_MEDIUM);
				mHardScoreItems = ScoresDataProvider.getHighScore(mContext,
						AppConstants.LEVEL_HARD);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mContext != null) {
				super.onPostExecute(result);
				mProgressDialog.dismiss();
				populateData();
			}
		}

		/**
		 * Method to populate the data on screen.
		 */
		private void populateData() {
			if (mEasyScoreItems != null) {
				mEasyScoreTextView.setVisibility(View.VISIBLE);
				mEasyScoreTextView.setText(getString(R.string.high_score) + " "
						+ mEasyScoreItems.getTime());
			} else {
				mEasyScoreTextView.setVisibility(View.GONE);
			}
			if (mMediumScoreItems != null) {
				mMediumScoreTextView.setVisibility(View.VISIBLE);
				mMediumScoreTextView.setText(getString(R.string.high_score)
						+ " " + mMediumScoreItems.getTime());
			} else {
				mMediumScoreTextView.setVisibility(View.GONE);
			}
			if (mHardScoreItems != null) {
				mHardScoreTextView.setVisibility(View.VISIBLE);
				mHardScoreTextView.setText(getString(R.string.high_score) + " "
						+ mHardScoreItems.getTime());
			} else {
				mHardScoreTextView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mContext != null) {
			if (mGetHighScoreTask != null) {
				mGetHighScoreTask.cancel(true);
				mGetHighScoreTask = null;
			}
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			mEasyScoreItems = null;
			mMediumScoreItems = null;
			mHardScoreItems = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
