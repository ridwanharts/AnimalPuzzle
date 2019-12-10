/**
 * 
 */
package com.labs.jangkriek.animalpuzzle;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.labs.jangkriek.animalpuzzle.activity.SlidePuzzleActivity;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;

/**
 * The base activity class to manage things
 * 
 * @author vishalbodkhe
 * 
 */
public class BaseActivity extends Activity {

	private Context mContext;
	private AlertDialog mErrorDialog;
	private boolean isDialogShown;
	private static ArrayList<Activity> mActivitiesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!(this instanceof SlidePuzzleActivity)) {
			if (!PhotoPuzzleApplication.isTablet) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
		if (mActivitiesList == null) {
			mActivitiesList = new ArrayList<Activity>();
		}
		mActivitiesList.add(this);
		mContext = BaseActivity.this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AppConstants.EXTRA_IS_DIALOG_SHOWN, isDialogShown);
	}

	/**
	 * Method to show the error dialog.
	 */
	public void showErrorDialog(String message) {
		if (mErrorDialog == null || !mErrorDialog.isShowing()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(message).setTitle(R.string.error);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							isDialogShown = false;
							finishAllActivity();
						}
					});
			mErrorDialog = builder.create();
			mErrorDialog.setCancelable(false);
			mErrorDialog.show();
			isDialogShown = true;
		}
	}

	/**
	 * Method to finish all activities
	 */
	public static void finishAllActivity() {
		if (mActivitiesList != null && mActivitiesList.size() > 0) {
			for (Activity activity : mActivitiesList) {
				activity.finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mErrorDialog != null) {
			mErrorDialog.cancel();
			mErrorDialog = null;
		}
		if (mActivitiesList != null && mActivitiesList.contains(this)) {
			mActivitiesList.remove(this);
		}
		mContext = null;
		super.onDestroy();
	}

}
