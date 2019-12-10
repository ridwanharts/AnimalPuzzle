package com.labs.jangkriek.animalpuzzle.activity;

import java.util.ArrayList;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.labs.jangkriek.animalpuzzle.BaseActivity;
import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.db.ScoresDataProvider;
import com.labs.jangkriek.animalpuzzle.manager.ResourcesContentManager;
import com.labs.jangkriek.animalpuzzle.model.ScoreItem;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;
import com.labs.jangkriek.animalpuzzle.util.SettingsPreferences;

/**
 * The class to show screen where user will play puzzle.
 * 
 * @author vishalbodkhe
 * 
 */
public class SlidePuzzleActivity extends BaseActivity implements OnKeyListener, RewardedVideoAdListener {

	private Context mContext;
	private TileView mTileView;
	private Chronometer mTimerView;
	private Toast mToast;
	private long mTime;
	private SoundPool mSoundPool;
	private int mClickSound;
	private int mApplauseSound;
	private int movesCount;
	private int mCurrentPosition;
	private boolean mIsHintOn;
	private boolean isWin;
	private boolean isHintTaken;
	private TextView mTitleTextView;
	private TextView moves_textview;
	private ImageView mCompleteView;
	private ImageView undo_imageview;
	private ImageView play_pause_imageview;
	private ImageView empty_view;
	private ArrayList<Integer> mPhotosUrlList;
	private ActionBrodcastListener actionBroadcastReceiver;
	private Dialog mCustomDialog;
	private RewardedVideoAd mRewardedVideoAd;
	private RelativeLayout bottom_ads_view;
	public InterstitialAd interstitial;
	private LottieAnimationView lottieReward;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.slide_puzzle);

		MobileAds.initialize(this, "ca-app-pub-2732887939805010~9906522837");
		//ca-app-pub-2732887939805010~9906522837
		//ca-app-pub-2732887939805010~9906522837
		mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		mRewardedVideoAd.setRewardedVideoAdListener(this);
		bottom_ads_view = findViewById(R.id.bottom_ads_view);
		AppUtils.setAdsViewBanner(SlidePuzzleActivity.this, bottom_ads_view);
		bottom_ads_view.setVisibility(View.VISIBLE);

		mContext = SlidePuzzleActivity.this;
		int orientation = getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		mPhotosUrlList = ResourcesContentManager.getInstance().getImageItemList();
		if (mPhotosUrlList != null) {
			initViews();
			if (icicle == null) {
				startNewgame();
			} else {
				Parcelable[] parcelables = icicle.getParcelableArray("tiles");
				Tile[] tiles = null;
				if (parcelables != null) {
					int len = parcelables.length;

					tiles = new Tile[len];
					for (int i = 0; i < len; i++) {
						tiles[i] = (Tile) parcelables[i];
					}
				}
				mTileView.setPhotoUrl(mPhotosUrlList.get(mCurrentPosition));
				mTileView.newGame(tiles, icicle.getInt("blank_first"),
						mTimerView);
				mTime = icicle.getLong("time", 0);
			}
		}
		mIsHintOn = false;
		lottieReward = findViewById(R.id.lottie_reward);
		lottieReward.setVisibility(View.INVISIBLE);
		loadRewardedVideoAd();
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (mPhotosUrlList != null) {
			//mIsHintOn = SettingsPreferences.getHintEnableDisable(mContext);
			mCurrentPosition = SettingsPreferences.getPhotoPosition(mContext);
			//updateShowHintCheckbox();
			updateTitle();
			updateTileView();
			updateArrows();
		}
	}

	private void initInteristitialAds() {
		if (AppUtils.isInternetConnected(mContext)
				&& !SettingsPreferences.isAdsRemoved(mContext)) {
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(AppConstants.ADMOB_ADS_ID);
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitial.loadAd(adRequest);

			interstitial.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (interstitial.isLoaded()) {
						interstitial.show();
					}
				}
			});
		}
	}

	/**
	 * Inits the views and data to show.
	 */
	private void initViews() {
		empty_view = (ImageView) findViewById(R.id.empty_view);
		moves_textview = (TextView) findViewById(R.id.moves_textview);
		undo_imageview = (ImageView) findViewById(R.id.undo_imageview);
		play_pause_imageview = (ImageView) findViewById(R.id.play_pause_imageview);
		tabs_bar2_view2 = (RelativeLayout) findViewById(R.id.tabs_bar2_view2);
		bottom_ads_view = (RelativeLayout) findViewById(R.id.bottom_ads_view);
		actionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_DIFFICULTY_LEVEL);
		registerReceiver(actionBroadcastReceiver, intentFilter);
		tabs_bar_view1 = (RelativeLayout) findViewById(R.id.tabs_bar_view1);
		tabs_bar_view2 = (RelativeLayout) findViewById(R.id.tabs_bar_view2);
		mTitleTextView = (TextView) findViewById(R.id.title_textview);
		mTileView = (TileView) findViewById(R.id.tile_view);
		mTileView.requestFocus();
		mTileView.setOnKeyListener(this);
		mCompleteView = (ImageView) findViewById(R.id.complete_view);
		// mCompleteView.setImageBitmap(mTileView.getCurrentImage());
		AudioAttributes attributes = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			attributes = new AudioAttributes.Builder()
					.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
					.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
					.setUsage(AudioAttributes.USAGE_GAME)
					.build();
		}
		mTimerView = (Chronometer) findViewById(R.id.timer_view);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mSoundPool = new SoundPool.Builder()
					.setAudioAttributes(attributes)
					.setMaxStreams(5)
					.build();
		} else {
			mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
		}

		//mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);

		mClickSound = mSoundPool.load(this, R.raw.swap_1, 1);
		mApplauseSound = mSoundPool.load(this, R.raw.applause, 1);
		mCurrentPosition = SettingsPreferences.getPhotoPosition(mContext);

		empty_view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});




	}

	private void loadRewardedVideoAd() {
		mRewardedVideoAd.loadAd("ca-app-pub-2732887939805010/3895610262",
				//asli    ca-app-pub-2732887939805010/3895610262
				//test    ca-app-pub-3940256099942544/5224354917
				new AdRequest.Builder().build());
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		lottieReward.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
		lottieReward.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {

	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {

	}

	@Override
	public void onRewardedVideoCompleted() {

	}

	public class ActionBrodcastListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action == AppConstants.UPDATE_DIFFICULTY_LEVEL) {
				updateTileView();
				loadNextPhoto();
				startNewgame();
			}
		}
	}

	/**
	 * Handler to post message for show win dialog .
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			// show win dialog
			empty_view.setVisibility(View.GONE);
			String winMessege = " " + movesCount + " "
					+ getString(R.string.moves).toLowerCase() + " "
					+ getString(R.string.and) + " "
					+ mTimerView.getText().toString() + " "
					+ getString(R.string.time).toLowerCase();

			if (mCurrentPosition == mPhotosUrlList.size() - 1) {
				showWinDialog(getString(R.string.win),
						getString(R.string.congrates) + winMessege,
						getString(R.string.next), getString(R.string.retry),
						true);
			} else {
				showWinDialog(getString(R.string.win),
						getString(R.string.congrates) + winMessege,
						getString(R.string.next), getString(R.string.retry),
						false);
			}

		}
	};

	private AnimationListener mCompleteAnimListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			mTileView.setVisibility(View.GONE);
			mHandler.sendEmptyMessageDelayed(1,
					AppConstants.WIN_DIALOG_DURATION);

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (SettingsPreferences.getSoundEnableDisable(mContext)) {
				AppUtils.playSound(mContext, R.raw.applause,
						AppConstants.APPLAUSE_SOUND, true);
			}
		}
	};

	/**
	 * Method to update move text and show in textview.
	 */
	private void updateMovesText() {
		moves_textview.setText(String.valueOf(movesCount));
	}

	/**
	 * Method to load next photo
	 */
	private void loadNextPhoto() {
		mTileView.setVisibility(View.VISIBLE);
		mCompleteView.setVisibility(View.GONE);
		if (mCurrentPosition == mPhotosUrlList.size() - 1) {
			mCurrentPosition = 0;
		} else {
			mCurrentPosition = mCurrentPosition + 1;
		}
		startNewgame();
	}

	/**
	 * Method to start timer.
	 */
	private void updateTileView() {
		mTileView.updateInstantPrefs();
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		if (!mTileView.isSolved()) {
			mTimerView.start();
		}
	}

	/**
	 * Method to set Hint CheckBox check uncheck.
	 */
	/*private void updateShowHintCheckbox() {
		if (mIsHintOn) {
			tabs_bar5_checkbox
					.setBackgroundResource(R.drawable.checkbox_checked);

		} else {
			tabs_bar5_checkbox
					.setBackgroundResource(R.drawable.checkbox_unchecked);

		}
	}*/

	/**
	 * This method change the title
	 */
	private void updateTitle() {
		int level = SettingsPreferences.getDifficultyLevel(mContext);
		if (AppConstants.DIFFICULTY_LEVEL_EASY == level) {
			mTitleTextView.setText(R.string.easy_level_score);
		} else if (AppConstants.DIFFICULTY_LEVEL_MEDIUM == level) {
			mTitleTextView.setText(R.string.medium_level_score);
		} else if (AppConstants.DIFFICULTY_LEVEL_HARD == level) {
			mTitleTextView.setText(R.string.hard_level_score);
		}else if (AppConstants.DIFFICULTY_LEVEL_VERY_EASY == level) {
			mTitleTextView.setText(R.string.very_easy_level_score);
		}
	}

	/**
	 * Method to update Arrows
	 */
	public void updateArrows() {
		if (mPhotosUrlList != null && mPhotosUrlList.size() > 0) {
			if (mPhotosUrlList.size() == 1) {
				tabs_bar_view1.setVisibility(View.INVISIBLE);
				tabs_bar_view2.setVisibility(View.INVISIBLE);
			} else {
				if (mCurrentPosition == 0) {
					tabs_bar_view1.setVisibility(View.INVISIBLE);
					tabs_bar_view2.setVisibility(View.VISIBLE);
				} else if (mCurrentPosition == mPhotosUrlList.size() - 1) {
					tabs_bar_view1.setVisibility(View.VISIBLE);
					tabs_bar_view2.setVisibility(View.INVISIBLE);
				} else {
					tabs_bar_view1.setVisibility(View.VISIBLE);
					tabs_bar_view2.setVisibility(View.VISIBLE);
				}
			}
		} else {
			tabs_bar_view1.setVisibility(View.INVISIBLE);
			tabs_bar_view2.setVisibility(View.INVISIBLE);
		}
	}

	private RelativeLayout tabs_bar_view1;
	private RelativeLayout tabs_bar_view2;
	private RelativeLayout tabs_bar2_view2;
	private boolean isPaused;

	/**
	 * Method to update undo status
	 */
	private void updateUndoStatus() {
		if (movesCount > 0) {
			undo_imageview.setBackgroundResource(R.drawable.arrow_left);
			tabs_bar2_view2.setClickable(true);
			if (movesCount == 31 || movesCount == 131 || movesCount == 181 || movesCount == 251){
				//initInteristitialAds();
			}
			if (movesCount == 81 || movesCount == 151 || movesCount == 201 || movesCount == 291){
				//loadRewardedVideoAd();
			}

		} else {
			undo_imageview
					.setBackgroundResource(R.drawable.arrow_left);
			tabs_bar2_view2.setClickable(false);
		}
	}

	/**
	 * Method to update play pause status
	 */
	private void updatePlayPauseStatus() {
		if (isPaused) {
			play_pause_imageview
					.setBackgroundResource(R.drawable.play);
		} else {
			play_pause_imageview
					.setBackgroundResource(R.drawable.pause);
		}
	}

	/**
	 * Method to register click view
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.tabs_bar2_view2:
			if (SettingsPreferences.getVibration(mContext)) {
				AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
			}
			movesCount = movesCount - 1;
			mTileView.doBackStepGame(mTileView.getBackStep(movesCount),
					SettingsPreferences.getEmptyLocation(mContext), mTimerView);
			updateMovesText();
			updateUndoStatus();
			break;
		case R.id.tabs_bar2_view1:
			isPaused = !isPaused;
			updatePlayPauseStatus();
			if (isPaused) {
				pauseTimer();
			} else {
				updateTileView();
			}
			break;
		case R.id.tabs_bar_view1:
			if (isWin) {
				isWin = false;
				loadNextPhoto();
				updateArrows();
			} else {
				mCurrentPosition = mCurrentPosition - 1;
				SettingsPreferences
						.setPhotoPosition(mContext, mCurrentPosition);
				updateArrows();
				startNewgame();
			}
			break;
		case R.id.tabs_bar_view2:
			if (isWin) {
				isWin = false;
				loadNextPhoto();
				updateArrows();
			} else {
				mCurrentPosition = mCurrentPosition + 1;
				SettingsPreferences
						.setPhotoPosition(mContext, mCurrentPosition);
				updateArrows();
				startNewgame();
			}
			break;
		case R.id.tabs_bar_view3:
			if (isWin) {
				isWin = false;
				loadNextPhoto();
			} else {
				reloadGame();
			}
			break;
            case R.id.tabs_bar_view4:
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.putExtra(AppConstants.EXTRA_PROCESSING_GAME_STRING, true);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_SETTINGS);
                break;
		case R.id.tabs_bar_view5:
			if (isWin) {
				isWin = false;
				mTileView.setVisibility(View.VISIBLE);
				mCompleteView.setVisibility(View.GONE);
				mIsHintOn = !mIsHintOn;
				SettingsPreferences.setHintEnableDisable(mContext, mIsHintOn);
				//updateShowHintCheckbox();
				mTileView.updateHintStatus();
				startNewgame();
			} else {
				mIsHintOn = !mIsHintOn;
				SettingsPreferences.setHintEnableDisable(mContext, mIsHintOn);
				//updateShowHintCheckbox();
				mTileView.updateHintStatus();
				if (!isHintTaken) {
					isHintTaken = SettingsPreferences
							.getHintEnableDisable(mContext);

				}
				if (mRewardedVideoAd.isLoaded()) {
					mRewardedVideoAd.show();
				}
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (isWin) {
			isWin = false;
			loadNextPhoto();
			startNewgame();
		}
	}

	/**
	 * Method to start new game
	 */
	private void startNewgame() {
		//initInteristitialAds();
		empty_view.setVisibility(View.GONE);
		//isHintTaken = SettingsPreferences.getHintEnableDisable(mContext);
		movesCount = 0;
		updateMovesText();
		isPaused = false;
		updatePlayPauseStatus();
		updateUndoStatus();
		int blankLoc = SettingsPreferences.getEmptyLocation(mContext);
		mTime = 0;
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mTileView.setPhotoUrl(mPhotosUrlList.get(mCurrentPosition));
		mTileView.newGame(null, blankLoc, mTimerView);
		mTimerView.start();
	}

	/**
	 * Method to reload game
	 */
	private void reloadGame() {
		movesCount = 0;
		updateMovesText();
		isPaused = false;
		updatePlayPauseStatus();
		updateUndoStatus();
		int blankLoc = SettingsPreferences.getEmptyLocation(mContext);
		mTime = 0;
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mTileView.setPhotoUrl(mPhotosUrlList.get(mCurrentPosition));
		mTileView.reloadGame(mTileView.getDefaultTiles(), blankLoc, mTimerView);
		mTimerView.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		pauseTimer();
	}

	/**
	 * Method to pause timer.
	 */
	private void pauseTimer() {
		if (!mTileView.isSolved()) {
			mTime = (SystemClock.elapsedRealtime() - mTimerView.getBase());
		}
		mTimerView.stop();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (mTileView.isSolved()) {
			return false;
		}
		boolean moved;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN: {
				moved = mTileView.move(TileView.DIR_DOWN);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_UP: {
				moved = mTileView.move(TileView.DIR_UP);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_LEFT: {
				moved = mTileView.move(TileView.DIR_LEFT);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_RIGHT: {
				moved = mTileView.move(TileView.DIR_RIGHT);
				break;
			}
			default:
				return false;
			}

			if (moved) {
				if (SettingsPreferences.getSoundEnableDisable(mContext)) {
				}
				if (SettingsPreferences.getVibration(mContext)) {
					AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
				}
			}
			if (mTileView.checkSolved()) {
				onPuzzleSolved();
			}
			return true;
		}
		return false;
	}

	/**
	 * Method to save score
	 */
	private void postScore() {
		mTime = SystemClock.elapsedRealtime() - mTimerView.getBase();
		mTimerView.stop();
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mTimerView.invalidate();
		String level = AppConstants.LEVEL_EASY;
		switch (SettingsPreferences.getDifficultyLevel(mContext)) {
			case AppConstants.DIFFICULTY_LEVEL_EASY:
				level = AppConstants.LEVEL_EASY;
				break;
			case AppConstants.DIFFICULTY_LEVEL_MEDIUM:
				level = AppConstants.LEVEL_MEDIUM;
				break;
			case AppConstants.DIFFICULTY_LEVEL_HARD:
				level = AppConstants.LEVEL_HARD;
				break;
			case AppConstants.DIFFICULTY_LEVEL_VERY_EASY:
				level = AppConstants.LEVEL_VERY_EASY;
				break;
		}
		ScoreItem item = new ScoreItem();
		item.setLevel(level);
		item.setTime(mTimerView.getText().toString());
		item.setSeconds(String.valueOf(mTime));
		item.setMoves(String.valueOf(movesCount));
		if (isHintTaken) {
			item.setHinttaken(AppConstants.TRUE);
		} else {
			item.setHinttaken(AppConstants.FALSE);
		}
		ScoresDataProvider.addScore(mContext, item);
		ScoreItem highItem = ScoresDataProvider.getHighScore(mContext, level);
		boolean isHighScore = false;

		if (Long.parseLong(item.getSeconds()) < Long.parseLong(highItem
				.getSeconds())) {
			isHighScore = true;
		}
		if (isHighScore) {
			if (SettingsPreferences.getSoundEnableDisable(mContext)) {
				//playSound(mApplauseSound);
			}
			mToast = Toast.makeText(this, "High Score", Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		}
		if (SettingsPreferences.getVibration(mContext)) {
			AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (isPaused) {
			return true;
		}
		// Prevent user from moving tiles if the puzzle has been solved
		if (mTileView.isSolved()) {
			return false;
		}

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mTileView.grabTile(event.getX(), event.getY());
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			mTileView.dragTile(event.getX(), event.getY());
			return true;
		}
		case MotionEvent.ACTION_UP: {
			boolean moved = mTileView.dropTile(event.getX(), event.getY());
			//onPuzzleSolved();
			if (moved) {
				movesCount = movesCount + 1;
				mTileView.onMoved(movesCount);
				updateMovesText();
				updateUndoStatus();
				if (SettingsPreferences.getSoundEnableDisable(mContext)) {
					//playSound(mClickSound);
				}
				if (SettingsPreferences.getVibration(mContext)) {
					AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
				}
			}
			if (AppConstants.TESTING_PUZZLE) {
				onPuzzleSolved();
			} else {
				if (mTileView.checkSolved()) {
					onPuzzleSolved();
				}
			}
			return true;
		}
		}
		return false;
	}

	/**
	 * Method to process after solved puzzle
	 */
	private void onPuzzleSolved() {
		isWin = true;
		mCompleteView.setImageBitmap(mTileView.getCurrentImage());
		mCompleteView.setVisibility(View.VISIBLE);
		empty_view.setVisibility(View.VISIBLE);
		empty_view.bringToFront();
		Animation animation = AnimationUtils
				.loadAnimation(this, R.anim.fade_in);
		animation.setAnimationListener(mCompleteAnimListener);
		mCompleteView.startAnimation(animation);
		postScore();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArray("tiles", mTileView.getTiles());
		outState.putInt("blank_first", mTileView.mBlankLocation);
		outState.putLong("time", mTime);
	}

	@Override
	public void onBackPressed() {
		if (isWin) {
			AppUtils.stopSound();
		}
		if (movesCount > 0) {
			showGameInProgressDialog(getString(R.string.confirmation),
					getString(R.string.difficulty_level_confirmation),
					getString(R.string.yes), getString(R.string.no), false);
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Method to show custom dialog.
	 * 
	 * @param title
	 * @param message
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showGameInProgressDialog(String title, String message,
			String okText, String cancelText, boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.confirmation_dialog_view,
				null);
		TextView titleTextview = (TextView) dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = (TextView) dialogView
				.findViewById(R.id.message_textview);
		Button okButton = (Button) dialogView.findViewById(R.id.ok_button);
		Button cancelButton = (Button) dialogView
				.findViewById(R.id.cancel_button);
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);

		mCustomDialog.setCanceledOnTouchOutside(false);

		titleTextview.setText(title);
		messageTextview.setText(message);
		if (singleButtonEnabled) {
			okButton.setText(okText);
			cancelButton.setVisibility(View.GONE);
		} else {
			okButton.setText(okText);
			cancelButton.setText(cancelText);
		}

		/**
		 * Listener for OK button click.
		 */
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
				mCustomDialog.dismiss();
			}
		});

		/**
		 * Listener for Cancel button click.
		 */
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mCustomDialog.dismiss();
			}
		});

		mCustomDialog.show();
	}

	/**
	 * Method to show custom dialog.
	 * 
	 * @param title
	 * @param message
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showWinDialog(String title, String message, String okText,
			final String cancelText, final boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.confirmation_dialog_view,
				null);
		TextView titleTextview = (TextView) dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = (TextView) dialogView
				.findViewById(R.id.message_textview);
		Button okButton = (Button) dialogView.findViewById(R.id.ok_button);
		Button cancelButton = (Button) dialogView
				.findViewById(R.id.cancel_button);
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setCancelable(false);

		mCustomDialog.setCanceledOnTouchOutside(false);

		titleTextview.setText(title);
		messageTextview.setText(message);
		if (singleButtonEnabled) {
			okButton.setText(okText);
			cancelButton.setVisibility(View.GONE);
		} else {
			okButton.setText(okText);
			cancelButton.setText(cancelText);
		}

		/**
		 * Listener for OK button click.
		 */
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AppUtils.stopSound();
				mCustomDialog.dismiss();
				if (singleButtonEnabled) {
					finish();
				} else {
					loadNextPhoto();
				}
			}
		});

		/**
		 * Listener for Cancel button click.
		 */
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AppUtils.stopSound();
				mTileView.setVisibility(View.VISIBLE);
				mCompleteView.setVisibility(View.GONE);
				reloadGame();
				mCustomDialog.dismiss();
			}
		});

		mCustomDialog.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Method to play sound
	 * 
	 * @param sound
	 */
	private void playSound(int sound) {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(sound, streamVolume, streamVolume, 1, 0, 1f);
	}

	@Override
	protected void onDestroy() {
		if (actionBroadcastReceiver != null) {
			unregisterReceiver(actionBroadcastReceiver);
			actionBroadcastReceiver = null;
		}
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mTitleTextView = null;
		moves_textview = null;
		mCompleteView = null;
		//tabs_bar5_checkbox = null;
		undo_imageview = null;
		play_pause_imageview = null;
		empty_view = null;
		if (mHandler != null) {
			mHandler.removeMessages(1);
			mHandler = null;
		}
		super.onDestroy();
	}
}