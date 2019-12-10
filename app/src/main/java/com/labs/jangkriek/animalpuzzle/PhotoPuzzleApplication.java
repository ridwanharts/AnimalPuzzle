package com.labs.jangkriek.animalpuzzle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Application;
import android.content.Context;

import com.labs.jangkriek.animalpuzzle.util.AppConstants;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;

/**
 * This class is title application entry level class .
 * 
 * @author vishalbodkhe
 * 
 */
public class PhotoPuzzleApplication extends Application {

	/** Application context */
	private static Context mContext;
	public static boolean isTablet;
	private static String mAppUrl;

	@Override
	public void onCreate() {
		super.onCreate();
		setContext(getApplicationContext());
		copyDatabaseFromAssetToLocal();
		isTablet = AppUtils.isTabletDevice(getApplicationContext());
		mAppUrl = AppConstants.PLAYSTORE_URL + mContext.getPackageName();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * @return the mAppUrl
	 */
	public static String getAppUrl() {
		return mAppUrl;
	}

	/**
	 * This method used to set the context.
	 * 
	 * @param context
	 */
	private static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * This method used to get the context.
	 * 
	 * @return mContext
	 */
	public static Context getAppContext() {
		return mContext;
	}

	/**
	 * Copy database from assets to local if not exist. (it executes very first
	 * time only)
	 */
	private void copyDatabaseFromAssetToLocal() {
		try {
			File dir = new File("/data/data/" + getPackageName() + "/databases");
			if (!dir.exists()) {
				dir.mkdir();
				File file = new File(dir + "/" + "photopuzzle.db");
				if (!file.exists()) {
					file.createNewFile();
				}
				InputStream source = getResources().getAssets().open(
						"photopuzzle.sqlite");
				String databasePath = file.getAbsolutePath();
				OutputStream out = new FileOutputStream(databasePath);

				byte[] buf = new byte[1024];
				int len;
				while ((len = source.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				source.close();
				out.close();
			}
		} catch (Exception e) {
		}
	}
}
