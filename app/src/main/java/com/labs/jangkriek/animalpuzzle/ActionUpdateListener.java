/**
 * 
 */
package com.labs.jangkriek.animalpuzzle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 
 * This listener class is used to receive the action updates which is
 * registered.
 * 
 * @author vishalbodkhe
 * 
 */
public abstract class ActionUpdateListener {

	/** Reference of intent filter */
	private IntentFilter mActionsIntentFilter;

	/** Extra part of intent */
	public static final String INTENT_EXTRA = "intent_extra";

	/** Intent Action for connectivity update. */
	public static final String ACTION_CONNECTIVITY_UPDATE = "android.net.conn.CONNECTIVITY_CHANGE";

	private BroadcastReceiver mActionReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
				onMediaMounted();
			} else if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)) {
				onMediaRemoved();
			} else if (intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)) {
				onMediaRejected();
			} else if (intent.getAction().equals(
					Intent.ACTION_MEDIA_BAD_REMOVAL)) {
				onMediaBadRemoved();
			}
		}
	};

	public void onMediaBadRemoved() {
	}

	public void onMediaRejected() {
	}

	public void onMediaRemoved() {
	}

	public void onMediaMounted() {
	}

	/**
	 * This method is used to registering the receiver.
	 * 
	 * @param context
	 */
	public void registerReceiver(Context context) {
		mActionsIntentFilter = new IntentFilter();
		mActionsIntentFilter.addDataScheme("file");
		mActionsIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mActionsIntentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		mActionsIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		mActionsIntentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		context.registerReceiver(mActionReciever, mActionsIntentFilter);
	}

	/**
	 * This method is used to unregistering the receiver.
	 * 
	 * @param context
	 */
	public void unRegisterReceiver(Context context) {
		context.unregisterReceiver(mActionReciever);
		mActionReciever = null;
		mActionsIntentFilter = null;
	}

}
