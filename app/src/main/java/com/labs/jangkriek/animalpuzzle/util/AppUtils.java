/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.util;

import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.labs.jangkriek.animalpuzzle.R;

/**
 * The utility class to provide utility method
 * 
 * @author vishalbodkhe
 * 
 */
public class AppUtils {

	private static Vibrator sVibrator;
	private static Toast sToast;
	private static MediaPlayer sPlayer = null;
	private static AssetFileDescriptor sPlaySoundDescriptor = null;
	private static AssetFileDescriptor sApplauseSoundDescriptor = null;

	/**
	 * Method to get scale bitmap
	 * 
	 * @param width
	 * @param height
	 * @return photoBitmap
	 */
	public static Bitmap getScaleBitmap(Context context, int resId, int width,
			int height) {
		Bitmap photoBitmap = null;
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory
					.decodeResource(context.getResources(), resId, options);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;
			photoBitmap = BitmapFactory.decodeResource(context.getResources(),
					resId, options);
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError) {
				System.gc();
			}
		}
		return photoBitmap;
	}

	/**
	 * Method to calculate the in sample size of bitmap.
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// title final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * Method to show custom toast message
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		if (context != null) {
			if (sToast == null) {
				sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			}
			sToast.setText(message);
			sToast.show();
		}
	}

	/**
	 * Method to check the is landscape mode.
	 * 
	 * @param context
	 * @return true if landscape mode otherwise false.
	 */
	public static boolean isLandscapeMode(Context context) {
		Display getOrient = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if (getOrient.getWidth() < getOrient.getHeight()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method used to get the result that the given device is tablet or not
	 * on basis of screen layout.
	 * 
	 * @param context
	 * @return result
	 */
	public static boolean isTabletDevice(Context context) {
		if (context != null) {
			return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
		}
		return false;
	}

	/**
	 * Returns the screen width of the device.
	 * 
	 * @return Screen width of the device.
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * Returns the screen height of the device.
	 * 
	 * @return Screen height of the device.
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * Check SDCard is available or not.
	 * 
	 * @return true if title external SDCard is available
	 */
	public static boolean isSDCardAvailable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public static Object[] permuteArray(Object array[]) {
		int size = array.length;
		Random rand = new Random();

		for (int i = 0; i < size; ++i) {
			int j = rand.nextInt(size);
			swap(array, i, j);
		}

		return array;
	}

	public static Object[] swap(Object array[], int i, int j) {
		// Prevent attempts to swap with elements outside the array
		if (i >= array.length || j >= array.length || i < 0 || j < 0) {
			return array;
		}

		Object temp = array[i];

		temp = array[i];
		array[i] = array[j];
		array[j] = temp;

		return array;
	}

	/**
	 * Method to get formatted url
	 * 
	 * @param url
	 * @return url
	 */
	public static String getFormattedUrl(String url) {
		try {
			if (url.contains("'")) {
				url = url.replaceAll("'", "''");
			}
		} catch (Exception ex) {
		}
		return url;
	}

	public static void setAdsView(Context context, RelativeLayout view) {
		if (isInternetConnected(context)) {
			AdRequest adRequest = new AdRequest.Builder().build();
			AdView name = new AdView((Activity) context);
			name.setAdSize(AdSize.FULL_BANNER);
			name.setAdUnitId(AppConstants.ADMOB_ADS_ID);
			name.loadAd(adRequest);
			view.removeAllViews();
			view.addView(name);
		} else {
			//view.setVisibility(View.GONE);
		}
	}

	public static void setAdsViewBanner(Context context, RelativeLayout view) {
		if (isInternetConnected(context)) {
			AdRequest adRequest = new AdRequest.Builder().build();
			AdView name = new AdView((Activity) context);
			name.setAdSize(AdSize.BANNER);
			name.setAdUnitId(AppConstants.ADMOB_ADS_ID_BANNER);
			name.loadAd(adRequest);
			view.removeAllViews();
			view.addView(name);
		} else {
			//view.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to check if internet available or not.
	 * 
	 * @param context
	 * @return true if available otherwise false
	 */
	public static boolean isInternetConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if (info.getState() != State.CONNECTED) {
			return false;
		}
		return true;
	}

	/**
	 * This method used to play title sound.
	 * 
	 * @param context
	 * @param soundType
	 * @param needLoop
	 */
	public static void playSound(Context context, int soundResId,
			int soundType, final boolean needLoop) {
		AssetFileDescriptor soundFileDescriptor = null;
		if (context != null) {
			try {
				if (sPlayer == null) {
					sPlayer = new MediaPlayer();
				}
				if (soundResId != 0) {
					sPlaySoundDescriptor = context.getResources()
							.openRawResourceFd(soundResId);
				}
				if (sApplauseSoundDescriptor == null) {
					sApplauseSoundDescriptor = context.getResources()
							.openRawResourceFd(R.raw.applause);
				}

				switch (soundType) {
				case AppConstants.PLAY_SOUND:
					soundFileDescriptor = sPlaySoundDescriptor;
					break;
				case AppConstants.APPLAUSE_SOUND:
					soundFileDescriptor = sApplauseSoundDescriptor;
					break;
				}
				sPlayer.reset();
				sPlayer.setDataSource(soundFileDescriptor.getFileDescriptor(),
						soundFileDescriptor.getStartOffset(),
						soundFileDescriptor.getDeclaredLength());
				sPlayer.prepare();
				sPlayer.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer player) {
						player.seekTo(0);
						player.start();
					}
				});

				sPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer player) {
						if (needLoop) {
							player.seekTo(0);
							player.start();
						}
					}
				});
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Method to stop the sound.
	 */
	public static void stopSound() {
		if (sPlayer != null) {
			sPlayer.reset();
		}
	}

	/**
	 * Method to do vibrate phone
	 * 
	 * @param context
	 * @param duration
	 */
	public static void vibrate(Context context, long duration) {
		if (sVibrator == null) {
			sVibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}
		if (sVibrator != null) {
			if (duration == 0) {
				duration = 50;
			}
			sVibrator.vibrate(duration);
		}
	}

}
