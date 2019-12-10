/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.labs.jangkriek.animalpuzzle.BaseActivity;
import com.labs.jangkriek.animalpuzzle.PhotoPuzzleApplication;
import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;
import com.labs.jangkriek.animalpuzzle.util.SettingsPreferences;

/**
 * The class to show different settings
 * 
 * @author vishalbodkhe
 * 
 */
public class SettingsActivity extends BaseActivity {
	private Context mContext;
	private Dialog mCustomDialog;
	private RadioButton mFirstRadioButton;
	private RadioButton mLastRadioButton;
	private RadioButton mRandomRadioButton;
	private RadioButton mVeryEasyRadioButton;
	private RadioButton mEasyRadioButton;
	private RadioButton mMediumRadioButton;
	private RadioButton mHardRadioButton;
	private CheckBox mSoundCheckBox;
	private CheckBox mVibrationCheckBox;
	private CheckBox mHintCheckBox;
	private TextView mEmptyLocationTextview;
	private TextView mDifficultySelectedTextview;
	private int mCurrentValue = 0;
	private int mDifficultyCurrentValue = 0;
	private int mPreviousDifficulyLevel;
	private boolean isSoundOn;
	private boolean isVibrationOn;
	private boolean isHintOn;
	private boolean isGameRunning;
	private boolean isDifficultyDialogShown;
	private RelativeLayout bottom_ads_view;
	public InterstitialAd interstitial;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		MobileAds.initialize(this);
		AdView adView = findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder().build());

		mContext = SettingsActivity.this;
		Intent intent = getIntent();
		if (intent != null) {
			isGameRunning = intent.getBooleanExtra(
					AppConstants.EXTRA_PROCESSING_GAME_STRING, false);
		}
		initViews();
		mPreviousDifficulyLevel = SettingsPreferences
				.getDifficultyLevel(mContext);
		if (savedInstanceState != null) {
			isDifficultyDialogShown = savedInstanceState
					.getBoolean(AppConstants.EXTRA_IS_DIFFICULTY_DIALOG_SHOWN);
			if (isDifficultyDialogShown) {
				showDifficultyCustomDialog(
						getString(R.string.difficulti_level), null, null, null,
						false);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AppConstants.EXTRA_IS_DIFFICULTY_DIALOG_SHOWN,
				isDifficultyDialogShown);
	}

	/**
	 * Inits the add
	 */
	private void initAds() {
		AppUtils.setAdsViewBanner(SettingsActivity.this, bottom_ads_view);
		bottom_ads_view.setVisibility(View.VISIBLE);
	}

	/**
	 * This method show the inits components
	 */
	private void initViews() {
		bottom_ads_view = (RelativeLayout) findViewById(R.id.banner_ads_view);
		mSoundCheckBox = (CheckBox) findViewById(R.id.sound_checkbox);
		mVibrationCheckBox = (CheckBox) findViewById(R.id.vibration_checkbox);
		mHintCheckBox = (CheckBox) findViewById(R.id.show_hint_checkbox);
		mEmptyLocationTextview = (TextView) findViewById(R.id.choose_location_textview);
		mDifficultySelectedTextview = (TextView) findViewById(R.id.difficulty_selected_textview);
		//initInteristitialAds();
		initAds();
		populateSoundContents();
		populateVibrationContents();
		populateHintContents();
		setLocationTextContents();
		setDifficultyLevelContents();
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
	 * Method to Show other App from Developer
	 */
	private void moreAppClicked() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://search?q=Jangkriek Labs")));
		} catch (ActivityNotFoundException anfe) {
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse(String
							.format("https://play.google.com/store/apps/dev?id=8458094076149466380",
									AppConstants.PLAYSTORE_ACCOUNT_NAME))));
		}
	}

	/**
	 * This method set the radio button check uncheck in empty location.
	 */
	private void switchEmptyRadioButtons() {
		mCurrentValue = SettingsPreferences.getEmptyLocation(mContext);
		int position = mCurrentValue;
		if (AppConstants.EMPTY_LOCATION_FIRST == mCurrentValue) {
			populateEmptyContents(position);
		} else if (AppConstants.EMPTY_LOCATION_LAST == mCurrentValue) {
			populateEmptyContents(position);
		} else if (AppConstants.EMPTY_LOCATION_RANDOM == mCurrentValue) {
			populateEmptyContents(position);
		}
	}

	/**
	 * This method set the radio button check uncheck in difficulty level.
	 */
	private void switchDifficultyRadioButtons() {
		mDifficultyCurrentValue = SettingsPreferences
				.getDifficultyLevel(mContext);
		int position = mDifficultyCurrentValue;
		if (AppConstants.DIFFICULTY_LEVEL_EASY == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		} else if (AppConstants.DIFFICULTY_LEVEL_MEDIUM == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		} else if (AppConstants.DIFFICULTY_LEVEL_HARD == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		}else if (AppConstants.DIFFICULTY_LEVEL_VERY_EASY == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		}
	}

	/**
	 * This method set the checkbox check uncheck for sound.
	 */
	private void switchSoundCheckbox() {
		isSoundOn = !isSoundOn;
		SettingsPreferences.setSoundEnableDisable(mContext, isSoundOn);
		populateSoundContents();
	}

	/**
	 * This method set the checkbox check uncheck for vibration.
	 */
	private void switchVibrationCheckbox() {
		isVibrationOn = !isVibrationOn;
		SettingsPreferences.setVibration(mContext, isVibrationOn);
		populateVibrationContents();
	}

	/**
	 * This method set the checkbox check uncheck for show hint.
	 */
	private void switchHintCheckbox() {
		isHintOn = !isHintOn;
		if (isHintOn) {
			SettingsPreferences.setHintEnableDisable(mContext, true);
		} else {
			SettingsPreferences.setHintEnableDisable(mContext, false);
		}
		populateHintContents();
	}

	/**
	 * This method is used to set Radio button check uncheck for empty location
	 */
	protected void populateEmptyContents(int position) {
		if (position == AppConstants.EMPTY_LOCATION_FIRST) {
			mFirstRadioButton.setChecked(true);
			mLastRadioButton.setChecked(false);
			mRandomRadioButton.setChecked(false);
		} else if (position == AppConstants.EMPTY_LOCATION_LAST) {
			mLastRadioButton.setChecked(true);
			mFirstRadioButton.setChecked(false);
			mRandomRadioButton.setChecked(false);
		} else if (position == AppConstants.EMPTY_LOCATION_RANDOM) {
			mRandomRadioButton.setChecked(true);
			mFirstRadioButton.setChecked(false);
			mLastRadioButton.setChecked(false);
		}
		mCurrentValue = position;
		SettingsPreferences.setEmptyLocation(mContext, mCurrentValue);
	}

	/**
	 * This method is used to set Radio button check uncheck for difficulty
	 * level.
	 */
	protected void populateDifficultyContents(int position) {
		if (position == AppConstants.DIFFICULTY_LEVEL_EASY) {
			mEasyRadioButton.setChecked(true);
			mMediumRadioButton.setChecked(false);
			mHardRadioButton.setChecked(false);
			mVeryEasyRadioButton.setChecked(false);
		} else if (position == AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
			mMediumRadioButton.setChecked(true);
			mEasyRadioButton.setChecked(false);
			mHardRadioButton.setChecked(false);
			mVeryEasyRadioButton.setChecked(false);
		} else if (position == AppConstants.DIFFICULTY_LEVEL_HARD) {
			mHardRadioButton.setChecked(true);
			mEasyRadioButton.setChecked(false);
			mMediumRadioButton.setChecked(false);
			mVeryEasyRadioButton.setChecked(false);
		} else if (position == AppConstants.DIFFICULTY_LEVEL_VERY_EASY) {
			mHardRadioButton.setChecked(false);
			mEasyRadioButton.setChecked(false);
			mMediumRadioButton.setChecked(false);
			mVeryEasyRadioButton.setChecked(true);
		}
		mDifficultyCurrentValue = position;
		SettingsPreferences.setDifficultyLevel(mContext,
				mDifficultyCurrentValue);
	}

	/**
	 * This method is used to set CheckBox check uncheck for sound .
	 */
	protected void populateSoundContents() {
		if (SettingsPreferences.getSoundEnableDisable(mContext)) {
			mSoundCheckBox.setChecked(true);
		} else {
			mSoundCheckBox.setChecked(false);
		}
		isSoundOn = SettingsPreferences.getSoundEnableDisable(mContext);
	}

	/**
	 * This method is used to set CheckBox check uncheck for vibration .
	 */
	protected void populateVibrationContents() {
		if (SettingsPreferences.getVibration(mContext)) {
			mVibrationCheckBox.setChecked(true);
		} else {
			mVibrationCheckBox.setChecked(false);
		}
		isVibrationOn = SettingsPreferences.getVibration(mContext);
	}

	/**
	 * This method is used to set CheckBox check uncheck for hint.
	 */

	protected void populateHintContents() {
		if (SettingsPreferences.getHintEnableDisable(mContext)) {
			mHintCheckBox.setChecked(true);
		} else {
			mHintCheckBox.setChecked(false);
		}
		isHintOn = SettingsPreferences.getHintEnableDisable(mContext);
	}

	/**
	 * This method is used to set Text to empty location.
	 */
	protected void setLocationTextContents() {
		int setTextValue = SettingsPreferences.getEmptyLocation(mContext);
		if (setTextValue == AppConstants.EMPTY_LOCATION_FIRST) {
			mEmptyLocationTextview.setText(getString(R.string.first_location));
		} else if (setTextValue == AppConstants.EMPTY_LOCATION_LAST) {
			mEmptyLocationTextview.setText(getString(R.string.last_location));
		} else if (setTextValue == AppConstants.EMPTY_LOCATION_RANDOM) {
			mEmptyLocationTextview.setText(getString(R.string.random_location));
		}
	}

	/**
	 * This method is used to set Text to Difficulty level selected.
	 */
	protected void setDifficultyLevelContents() {
		int setLevelValue = SettingsPreferences.getDifficultyLevel(mContext);
		if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_EASY) {
			mDifficultySelectedTextview.setText(getString(R.string.easy_level));
		} else if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
			mDifficultySelectedTextview
					.setText(getString(R.string.medium_level));
		} else if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_HARD) {
			mDifficultySelectedTextview.setText(getString(R.string.hard_level));
		} else if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_VERY_EASY){
			mDifficultySelectedTextview.setText(getString(R.string.very_easy_level));
		}

	}

	@Override
	public void onBackPressed() {
		if (mPreviousDifficulyLevel == SettingsPreferences
				.getDifficultyLevel(mContext)) {
			super.onBackPressed();
		} else {
			setResult(RESULT_OK);
			finish();
		}
	}

	/**
	 * Method to rate app
	 */
	private void updateClicked() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id="
							+ mContext.getPackageName())));
		} catch (ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ mContext.getPackageName())));
		}
	}

	/**
	 * Method to share app
	 */
	private void shareClicked(String subject, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent,
				getString(R.string.share_via)));
	}

	/**
	 * Method to get click actions on registered views.
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.share_layout:
			shareClicked(getString(R.string.share_subject),
					PhotoPuzzleApplication.getAppUrl());
			break;
		case R.id.difficulty_layout:
			showDifficultyCustomDialog(getString(R.string.difficulti_level),
					null, null, null, false);
			isDifficultyDialogShown = true;
			break;
		case R.id.sound_layout:
			switchSoundCheckbox();
			break;
		case R.id.sound_checkbox:
			switchSoundCheckbox();
			break;
		case R.id.vibration_layout:
			switchVibrationCheckbox();
			break;
		case R.id.vibration_checkbox:
			switchVibrationCheckbox();
			break;
		case R.id.show_hint_layout:
			switchHintCheckbox();
			break;
		case R.id.show_hint_checkbox:
			switchHintCheckbox();
			break;
		case R.id.empty_location_layout:
			showEmptyCustomDialog(getString(R.string.empty_location), null,
					null, null, false);
			break;
		case R.id.moreapp_layout:
			moreAppClicked();
			break;
		case R.id.help_layout:
			updateClicked();
			break;
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
	private void showCustomDialog(String title, String message, String okText,
			String cancelText, boolean singleButtonEnabled, final int position) {
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
		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});

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
				Intent intent = new Intent(AppConstants.UPDATE_DIFFICULTY_LEVEL);
				sendBroadcast(intent);
				populateDifficultyContents(position);
				setDifficultyLevelContents();
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
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showDifficultyCustomDialog(String title, String messege,
			String okText, String cancelText, boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.difficulty_custem_dialog,
				null);
		TextView titleTextView = (TextView) dialogView
				.findViewById(R.id.title_textview);
		titleTextView.setText(title);
		mVeryEasyRadioButton = (RadioButton) dialogView
				.findViewById(R.id.very_easy_radiobutton);
		mEasyRadioButton = (RadioButton) dialogView
				.findViewById(R.id.easy_radiobutton);
		mMediumRadioButton = (RadioButton) dialogView
				.findViewById(R.id.medium_radiobutton);
		mHardRadioButton = (RadioButton) dialogView
				.findViewById(R.id.hard_radiobutton);
		RelativeLayout easyRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.easy);
		RelativeLayout mediumRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.medium);
		RelativeLayout hardRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.hard);
		mVeryEasyRadioButton.setOnClickListener(mViewClickListener);
		mEasyRadioButton.setOnClickListener(mViewClickListener);
		mMediumRadioButton.setOnClickListener(mViewClickListener);
		mHardRadioButton.setOnClickListener(mViewClickListener);
		easyRelativeLayout.setOnClickListener(mViewClickListener);
		mediumRelativeLayout.setOnClickListener(mViewClickListener);
		hardRelativeLayout.setOnClickListener(mViewClickListener);
		// Radio Button check uncheck method
		switchDifficultyRadioButtons();
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setCanceledOnTouchOutside(false);
		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});
		mCustomDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isDifficultyDialogShown = false;
			}
		});
		mCustomDialog.show();
	}

	/**
	 * Method to show custom dialog.
	 * 
	 * @param title
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showEmptyCustomDialog(String title, String messege,
			String okText, String cancelText, boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(
				R.layout.empty_location_custem_dialog, null);
		TextView titleTextView = (TextView) dialogView
				.findViewById(R.id.title_textview);
		titleTextView.setText(title);
		mFirstRadioButton = (RadioButton) dialogView
				.findViewById(R.id.first_radiobutton);
		mLastRadioButton = (RadioButton) dialogView
				.findViewById(R.id.last_radiobutton);
		mRandomRadioButton = (RadioButton) dialogView
				.findViewById(R.id.random_radiobutton);
		RelativeLayout firstRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.first);
		RelativeLayout lastRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.last);
		RelativeLayout ramdomRelativeLayout = (RelativeLayout) dialogView
				.findViewById(R.id.random);
		mFirstRadioButton.setOnClickListener(mViewClickListener);
		mLastRadioButton.setOnClickListener(mViewClickListener);
		mRandomRadioButton.setOnClickListener(mViewClickListener);
		firstRelativeLayout.setOnClickListener(mViewClickListener);
		lastRelativeLayout.setOnClickListener(mViewClickListener);
		ramdomRelativeLayout.setOnClickListener(mViewClickListener);
		// Radio Button check uncheck method
		switchEmptyRadioButtons();

		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setCanceledOnTouchOutside(false);

		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});
		mCustomDialog.show();
	}

	/**
	 * Click listener
	 */
	private OnClickListener mViewClickListener = new OnClickListener() {

		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.first_radiobutton:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_FIRST);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.first:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_FIRST);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.last_radiobutton:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_LAST);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.last:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_LAST);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.random_radiobutton:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_RANDOM);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.random:
				populateEmptyContents(AppConstants.EMPTY_LOCATION_RANDOM);
				setLocationTextContents();
				mCustomDialog.dismiss();
				break;
			case R.id.easy_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.easy:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.medium_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.medium:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.hard_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_HARD) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_HARD);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_HARD);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.hard:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_HARD) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_HARD);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_HARD);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.very_easy_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_VERY_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_VERY_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_VERY_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.very_easy:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_VERY_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_VERY_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_VERY_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			}
		}
	};

	@Override
	public void onDestroy() {
		if (mContext != null) {
			if (mCustomDialog != null) {
				mCustomDialog.dismiss();
				mCustomDialog = null;
			}
			interstitial = null;
			mFirstRadioButton = null;
			mLastRadioButton = null;
			mRandomRadioButton = null;
			mVeryEasyRadioButton = null;
			mEasyRadioButton = null;
			mMediumRadioButton = null;
			mHardRadioButton = null;
			mSoundCheckBox = null;
			mVibrationCheckBox = null;
			mHintCheckBox = null;
			mEmptyLocationTextview = null;
			mSoundCheckBox = null;
			mHintCheckBox = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
