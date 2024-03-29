package com.labs.jangkriek.animalpuzzle.activity;

import java.util.HashMap;
import java.util.Random;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.model.BackStepItem;
import com.labs.jangkriek.animalpuzzle.util.AppConstants;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;
import com.labs.jangkriek.animalpuzzle.util.SettingsPreferences;

public class TileView extends View {
	private Context mContext;
	private static final String LOG_TAG = TileView.class.getName();
	private static final boolean DEBUG = false;
	public static final int DIR_UP = 0;
	public static final int DIR_DOWN = 1;
	public static final int DIR_LEFT = 2;
	public static final int DIR_RIGHT = 3;
	public static final int BLANK_FIRST = 0;
	public static final int BLANK_LAST = 1;
	public static final int BLANK_RANDOM = 2;
	private static final int SHADOW_RADIUS = 5;
	private static final int SHADOW_DX = 3;
	private static final int SHADOW_DY = 3;
	float mOffsetX;
	float mOffsetY;
	float mX;
	float mY;
	int mEmptyIndex;
	int mSelected;
	int mSize;
	int mSizeSqr;
	Tile mTiles[];
	boolean mShowNumbers;
	boolean mShowOutlines;
	boolean mShowImage;
	Bitmap mBitmap;
	int mNumberSize;
	private boolean mSolved;
	int mBlankLocation;
	int mNumberColor;
	int mOutlineColor;
	Paint mPaint;
	Chronometer mTimer;
	int mMisplaced; 

	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TileView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setFocusable(true);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
	}

	public void updateInstantPrefs() {
		mShowNumbers = SettingsPreferences.getHintEnableDisable(mContext);
		mNumberColor = mContext.getResources().getColor(R.color.white);
		mOutlineColor = mContext.getResources().getColor(R.color.border_color);
		mNumberSize = (int) mContext.getResources().getDimension(
				R.dimen.hint_text_size);
		mShowOutlines = true;
		mShowImage = true;
		mTimer.setTextColor(mNumberColor);
		mTimer.setVisibility(View.VISIBLE);
		requestLayout();
		invalidate();
	}

	public void updateHintStatus() {
		mShowNumbers = SettingsPreferences.getHintEnableDisable(mContext);
		requestLayout();
		invalidate();
	}

	private int imageresourceId;

	public void setPhotoUrl(int url) {
		imageresourceId = url;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		Context context = getContext();
		if (w <= 0 || h <= 0) {
			return;
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = getImageFromResource(context, imageresourceId,
					w, h);
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = getImageFromResource(context, R.drawable.default_image,
					w, h);
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (getWidth() > 0 && getHeight() > 0) {
				newGame(tiles, mBlankLocation, mTimer);
				handler.removeMessages(1);
			} else {
				handler.sendEmptyMessageDelayed(1, 5);
			}
		}
	};

	private HashMap<Integer, BackStepItem> backStackMap;

	public void addBackStep(int move, BackStepItem item) {
		if (backStackMap == null) {
			backStackMap = new HashMap<Integer, BackStepItem>();
		}
		backStackMap.put(move, item);
	}

	public void clearBackStack() {
		if (backStackMap != null) {
			backStackMap.clear();
			backStackMap = null;
		}
	}

	public BackStepItem getBackStep(int position) {
		BackStepItem item = null;
		if (backStackMap != null) {
			item = new BackStepItem();
			item.setEmptyIndex(backStackMap.get(position).getEmptyIndex());
			item.setMisplaced(backStackMap.get(position).getMisplaced());
			item.setTiles(backStackMap.get(position).getTiles());
			backStackMap.remove(position + 1);
		}
		return item;
	}

	Tile[] tiles;
	Tile[] mDefaultTiles;
	int mPreviousMisplaced = 0;
	int mPreviousEmptyIndex;

	public Tile[] getDefaultTiles() {
		return mDefaultTiles;
	}

	public void doBackStepGame(BackStepItem item, int blankLocation,
			Chronometer chronometer) {
		mMisplaced = item.getMisplaced();
		mEmptyIndex = item.getEmptyIndex();
		mTimer = chronometer;
		mSelected = -1; 
		mSolved = false;
		mBlankLocation = blankLocation;
		Tile[] tiles = item.getTiles();
		mTiles = new Tile[tiles.length];
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];
			if (tile == null) {
				mTiles[i] = null;
			} else {
				mTiles[i] = new Tile(tile.mNumber, tile.mColor);
			}
		}
		requestLayout();
		invalidate();
	}

	public void reloadGame(Tile[] tiles, int blankLocation,
			Chronometer chronometer) {
		mMisplaced = mPreviousMisplaced;
		mEmptyIndex = mPreviousEmptyIndex;
		mTimer = chronometer;
		mSelected = -1; 
		mSolved = false;
		mBlankLocation = blankLocation;
		mTiles = new Tile[tiles.length];
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];
			if (tile == null) {
				mTiles[i] = null;
			} else {
				mTiles[i] = new Tile(tile.mNumber, tile.mColor);
			}
		}
		requestLayout();
		invalidate();
	}

	public void newGame(Tile[] tiles, int blankLocation, Chronometer chronometer) {
		mMisplaced = 0;
		mTimer = chronometer;
		mSelected = -1; 
		mSolved = false;
		mBlankLocation = blankLocation;
		this.tiles = tiles;

		if (getWidth() == 0 || getHeight() == 0) {
			handler.sendEmptyMessageDelayed(1, 5);
			return;
		}

		if (tiles == null) {
			mBitmap = getImageFromResource(getContext(), imageresourceId,
					getWidth(), getHeight());
			mSize = (int) SettingsPreferences.getDifficultyLevel(mContext);
			mSizeSqr = mSize * mSize;
			Random random = new Random();
			mTiles = new Tile[mSizeSqr];
			for (int i = 0; i < mSizeSqr; ++i) {
				mTiles[i] = new Tile(i, random.nextInt() | 0xff000000);
			}
			if (mBlankLocation == AppConstants.EMPTY_LOCATION_FIRST) {
				mEmptyIndex = 0;
			} else if (mBlankLocation == AppConstants.EMPTY_LOCATION_LAST) {
				mEmptyIndex = mSizeSqr - 1;
			} else {
				mEmptyIndex = random.nextInt(mSizeSqr);
			}
			mTiles[mEmptyIndex] = null;
			for (int i = 0; i < 100 * mSize; ++i) {
				move(random.nextInt(4));
			}
			mDefaultTiles = null;
			mDefaultTiles = new Tile[mTiles.length];
			for (int i = 0; i < mTiles.length; i++) {
				Tile tile = mTiles[i];
				if (tile == null) {
					mDefaultTiles[i] = null;
				} else {
					mDefaultTiles[i] = new Tile(tile.mNumber, tile.mColor);
				}
			}
			mPreviousMisplaced = mMisplaced;
			mPreviousEmptyIndex = mEmptyIndex;
			clearBackStack();
			BackStepItem item = new BackStepItem();
			item.setEmptyIndex(mEmptyIndex);
			item.setMisplaced(mMisplaced);
			item.setTiles(mDefaultTiles);
			addBackStep(0, item);
		} else {
			mTiles = tiles;
			mSizeSqr = tiles.length;
			mSize = (int) Math.sqrt(mSizeSqr);
			countMisplaced();
			for (int i = 0; i < mSizeSqr; i++) {
				if (tiles[i] == null) {
					mEmptyIndex = i;
					break;
				}
			}
		}

		if (mMisplaced == 0) {
			onSolved();
		}
	}

	private void countMisplaced() {
		for (int i = 0; i < mSizeSqr; ++i) {
			if (null != mTiles[i] && mTiles[i].mNumber != i) {
				mMisplaced++;
			}
		}
	}

	private float getTileWidth() {
		return getWidth() / mSize;
	}

	private float getTileHeight() {
		return getHeight() / mSize;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = getImageFromResource(getContext(),
					imageresourceId, getWidth(), getHeight());
		}
		if (mBitmap == null || mBitmap.isRecycled()) {
			mBitmap = getImageFromResource(getContext(),
					R.drawable.default_image, getWidth(), getHeight());
		}

		float tileWidth = getTileWidth();
		float tileHeight = getTileHeight();

		for (int index = 0; index < mSizeSqr; ++index) {
			int i = index / mSize;
			int j = index % mSize;
			float x = tileWidth * j;
			float y = tileHeight * i;
			if (mTiles[index] == null) {
				continue;
			}

			if (mSelected != -1) {
				int min = Math.min(mSelected, mEmptyIndex);
				int max = Math.max(mSelected, mEmptyIndex);
				int minX = min % mSize;
				int minY = min / mSize;
				int maxX = max % mSize;
				int maxY = max / mSize;

				if (i >= minY && i <= maxY && j == minX) {
					y += mOffsetY;
				}
				if (j >= minX && j <= maxX && i == minY) {
					x += mOffsetX;
				}
			}
			if (mShowImage) {
				int xCropOffset = (mBitmap.getWidth() - getWidth()) / 2;
				int yCropOffset = (mBitmap.getHeight() - getHeight()) / 2;
				int tileNumber = mTiles[index].mNumber;
				int xSrc = (int) ((tileNumber % mSize) * tileWidth)
						+ xCropOffset;
				int ySrc = (int) ((tileNumber / mSize) * tileHeight)
						+ yCropOffset;
				Rect src = new Rect(xSrc, ySrc, (int) (xSrc + tileWidth),
						(int) (ySrc + tileHeight));
				Rect dst = new Rect((int) x, (int) y, (int) (x + tileWidth),
						(int) (y + tileHeight));

				canvas.drawBitmap(mBitmap, src, dst, mPaint);
			} else {
				mPaint.setColor(mTiles[index].mColor);
				canvas.drawRect(x, y, x + tileWidth, y + tileHeight, mPaint);
			}
			
			if (mShowNumbers) {
				mPaint.setShadowLayer(SHADOW_RADIUS, SHADOW_DX, SHADOW_DY,
						0xff000000);
				mPaint.setColor(mNumberColor);
				mPaint.setTextSize(mNumberSize);
				canvas.drawText(String.valueOf(mTiles[index].mNumber + 1), x
						+ (tileWidth / 2), y + (tileHeight / 2), mPaint);
			}
			if (mShowOutlines) {
				Paint mPaint= new Paint();
				float x2 = x + tileWidth - 1;
				float y2 = y + tileHeight - 1;
				float lines[] = { x, y, x2, y, x, y, x, y2, x2, y, x2, y2, x,
						y2, x2, y2 };
				mPaint.setColor(mOutlineColor);
				canvas.drawLines(lines, mPaint);
			}
			mPaint.setShadowLayer(0, 0, 0, 0);
		}
	}

	private int getCellIndex(float x, float y) {
		float tileWidth = getTileWidth();
		float tileHeight = getTileHeight();

		int loc[] = new int[2];
		getLocationOnScreen(loc);

		if (DEBUG) {
			Log.v(LOG_TAG, "Index: " + (int) ((y - loc[1]) / tileHeight)
					* mSize + (int) ((x - loc[0]) / tileWidth));
		}

		int xIndex = (int) ((x - loc[0]) / tileWidth);
		int yIndex = (int) ((y - loc[1]) / tileHeight);
		if (xIndex >= mSize) {
			xIndex = mSize - 1;
		} else if (xIndex < 0) {
			xIndex = 0;
		}

		if (yIndex >= mSize) {
			yIndex = mSize - 1;
		} else if (yIndex < 0) {
			yIndex = 0;
		}

		return mSize * yIndex + xIndex;
	}

	private boolean isSelectable(int index) {
		return (index / mSize == mEmptyIndex / mSize || index % mSize == mEmptyIndex
				% mSize)
				&& index != mEmptyIndex;
	}

	public boolean move(int dir) {
		if (mSelected >= 0) {
			return false;
		}
		int index;
		switch (dir) {
		case DIR_UP:
			index = mEmptyIndex + mSize;
			if ((index) < mSizeSqr) {
				update(index);
				return true;
			}
			return false;
		case DIR_DOWN:
			index = mEmptyIndex - mSize;
			if ((index) >= 0) {
				update(index);
				return true;
			}
			return false;
		case DIR_LEFT:
			index = mEmptyIndex + 1;
			if ((index % mSize) != 0) {
				update(index);
				return true;
			}
			return false;
		case DIR_RIGHT:
			index = mEmptyIndex - 1;
			if ((mEmptyIndex % mSize) != 0) {
				update(index);
				return true;
			}
			return false;
		}
		return false;
	}

	private void redrawRow() {
		int h = (int) getTileHeight();
		int tileY = h * (mEmptyIndex / mSize);
		invalidate(0, tileY - SHADOW_RADIUS, getRight(), tileY + h
				+ SHADOW_RADIUS);
	}

	private void redrawColumn() {
		int w = (int) getTileWidth();
		int tileX = w * (mEmptyIndex % mSize);
		invalidate(tileX - SHADOW_RADIUS, 0, tileX + w + SHADOW_RADIUS,
				getBottom());
	}

	private void update(int index) {
		if (index / mSize == mEmptyIndex / mSize) {
			if (mEmptyIndex < index) {
				while (mEmptyIndex < index) {
					mTiles = (Tile[]) AppUtils.swap(mTiles, mEmptyIndex,
							mEmptyIndex + 1);
					if (mTiles[mEmptyIndex].mNumber == mEmptyIndex) {
						mMisplaced--;
					} else if (mTiles[mEmptyIndex].mNumber == mEmptyIndex + 1) {
						mMisplaced++;
					}
					++mEmptyIndex;
				}
			} else {
				while (mEmptyIndex > index) {
					mTiles = (Tile[]) AppUtils.swap(mTiles, mEmptyIndex,
							mEmptyIndex - 1);
					if (mTiles[mEmptyIndex].mNumber == mEmptyIndex) {
						mMisplaced--;
					} else if (mTiles[mEmptyIndex].mNumber == mEmptyIndex - 1) {
						mMisplaced++;
					}
					--mEmptyIndex;
				}
			}
			redrawRow();
		} else if (index % mSize == mEmptyIndex % mSize) {
			if (mEmptyIndex < index) {
				while (mEmptyIndex < index) {
					mTiles = (Tile[]) AppUtils.swap(mTiles, mEmptyIndex,
							mEmptyIndex + mSize);
					if (mTiles[mEmptyIndex].mNumber == mEmptyIndex) {
						mMisplaced--;
					} else if (mTiles[mEmptyIndex].mNumber == mEmptyIndex
							+ mSize) {
						mMisplaced++;
					}
					mEmptyIndex += mSize;
				}
			} else {
				while (mEmptyIndex > index) {
					mTiles = (Tile[]) AppUtils.swap(mTiles, mEmptyIndex,
							mEmptyIndex - mSize);
					if (mTiles[mEmptyIndex].mNumber == mEmptyIndex) {
						mMisplaced--;
					} else if (mTiles[mEmptyIndex].mNumber == mEmptyIndex
							- mSize) {
						mMisplaced++;
					}
					mEmptyIndex -= mSize;
				}
			}
			redrawColumn();
		}
	}

	public void grabTile(float x, float y) {
		int index = getCellIndex(x, y);
		mSelected = isSelectable(index) ? index : -1;
		mX = x;
		mY = y;
		mOffsetX = 0;
		mOffsetY = 0;
	}

	public boolean dropTile(float x, float y) {
		if (mSelected != -1
				&& (Math.abs(mOffsetX) > getTileWidth() / 2 || Math
						.abs(mOffsetY) > getTileHeight() / 2)) {
			update(mSelected);
			mSelected = -1;
			return true;
		} else if (mSelected % mSize == mEmptyIndex % mSize) {
			redrawColumn();
		} else if (mSelected / mSize == mEmptyIndex / mSize) {
			redrawRow();
		}
		mSelected = -1;
		return false;
	}

	public void onMoved(int position) {
		BackStepItem item = new BackStepItem();
		item.setEmptyIndex(mEmptyIndex);
		item.setMisplaced(mMisplaced);
		Tile[] tiles = new Tile[mTiles.length];
		for (int i = 0; i < mTiles.length; i++) {
			Tile tile = mTiles[i];
			if (tile == null) {
				tiles[i] = null;
			} else {
				tiles[i] = new Tile(tile.mNumber, tile.mColor);
			}
		}
		item.setTiles(tiles);
		addBackStep(position, item);
	}

	public void dragTile(float x, float y) {
		if (mSelected < 0)
			return;
		int w = (int) getTileWidth();
		int h = (int) getTileHeight();
		if (mSelected % mSize == mEmptyIndex % mSize) {
			if (mSelected > mEmptyIndex) {
				mOffsetY += y - mY;
				if (mOffsetY > 0) {
					mOffsetY = 0;
				} else if (Math.abs(mOffsetY) > h) {
					mOffsetY = -h;
				}
				mY = y;

			} else {
				mOffsetY += y - mY;
				if (mOffsetY < 0) {
					mOffsetY = 0;
				} else if (mOffsetY > h) {
					mOffsetY = h;
				}
				mY = y;
			}
			redrawColumn();
		} else if (mSelected / mSize == mEmptyIndex / mSize) {
			if (mSelected > mEmptyIndex) {
				mOffsetX += x - mX;
				if (mOffsetX > 0) {
					mOffsetX = 0;
				} else if (Math.abs(mOffsetX) > w) {
					mOffsetX = -w;
				}
				mX = x;
			} else {
				mOffsetX += x - mX;
				if (mOffsetX < 0) {
					mOffsetX = 0;
				} else if (mOffsetX > w) {
					mOffsetX = w;
				}
				mX = x;
			}
			redrawRow();
		}
	}

	public static Bitmap getImageFromUri(Context context, Uri uri, int width,
			int height) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uri.toString(), opts);
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = Math.max(opts.outWidth / width, opts.outHeight
					/ height);
			Bitmap bitmap = BitmapFactory.decodeFile(uri.toString(), opts);
			if (bitmap == null) {
				return null;
			}

			int scaledWidth = bitmap.getWidth();
			int scaledHeight = bitmap.getHeight();
			if (scaledWidth < scaledHeight) {
				float scale = width / (float) scaledWidth;
				scaledWidth = width;
				scaledHeight = (int) Math.ceil(scaledHeight * scale);
				if (scaledHeight < height) {
					scale = height / (float) scaledHeight;
					scaledHeight = height;
					scaledWidth = (int) Math.ceil(scaledWidth * scale);
				}
			} else {
				float scale = height / (float) scaledHeight;
				scaledHeight = height;
				scaledWidth = (int) Math.ceil(scaledWidth * scale);
				if (scaledWidth < width) {
					scale = width / (float) scaledWidth;
					scaledWidth = width;
					scaledHeight = (int) Math.ceil(scaledHeight * scale);
				}
			}
			bmp = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight,
					false);
			bitmap.recycle();
			bitmap = null;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return bmp;
	}

	public static Bitmap getImageFromResource(Context context, int resId,
			int width, int height) {
		Resources resources = context.getResources();
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, opts);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = Math.max(opts.outWidth / width, opts.outHeight
				/ height);
		int scaledWidth = opts.outWidth;
		int scaledHeight = opts.outHeight;
		if (scaledWidth < scaledHeight) {
			float scale = width / (float) scaledWidth;
			scaledWidth = width;
			scaledHeight = (int) Math.ceil(scaledHeight * scale);
			if (scaledHeight < height) {
				scale = height / (float) scaledHeight;
				scaledHeight = height;
				scaledWidth = (int) Math.ceil(scaledWidth * scale);
			}
		} else {
			float scale = height / (float) scaledHeight;
			scaledHeight = height;
			scaledWidth = (int) Math.ceil(scaledWidth * scale);
			if (scaledWidth < width) {
				scale = width / (float) scaledWidth;
				scaledWidth = width;
				scaledHeight = (int) Math.ceil(scaledHeight * scale);
			}
		}
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				resources, resId, opts), scaledWidth,
				scaledHeight, false);
	}

	public boolean checkSolved() {
		if (mSolved) {
			return true;
		}
		if (mMisplaced == 0) {
			onSolved();
			return true;
		}
		return false;
	}

	private void onSolved() {
		mSolved = true;
		mTiles[mEmptyIndex] = new Tile(mEmptyIndex,
				new Random().nextInt() | 0xff000000);
		invalidate();
	}

	public boolean isSolved() {
		return mSolved;
	}

	public Tile[] getTiles() {
		return mTiles;
	}

	public Bitmap getCurrentImage() {
		return mBitmap;
	}
}
