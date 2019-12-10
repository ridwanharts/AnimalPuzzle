/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.util.AppUtils;

/**
 * This images grid adapter class to bind the views on the list.
 * 
 * @author vishalbodkhe
 * 
 */
public class GridViewAdapter extends BaseAdapter {

	private Context mContext;

	/** Holds Layout Inflater to inflate list item. */
	private LayoutInflater mLayoutInflator;

	/** Holds the list */
	private ArrayList<Integer> mListItems;
	private int width = 0;
	private int height = 0;
	private int deviceWidth = 0;
	private float spacing = 0;
	private int gridCount = 0;
	private float padding = 0;
	private RelativeLayout.LayoutParams params;

	public GridViewAdapter(Context context) {
		super();
		mContext = context;
		mLayoutInflator = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		deviceWidth = AppUtils.getScreenWidth(mContext);
		gridCount = mContext.getResources().getInteger(R.integer.grid_columns);
		spacing = mContext.getResources().getDimension(
				R.dimen.grid_view_spacing);
		padding = mContext.getResources().getDimension(
				R.dimen.grid_view_padding);
		width = (int) ((deviceWidth - (((gridCount - 1) * spacing * 1.0) + (padding * 2 * 1.0))) / gridCount);
		height = width;
		params = new RelativeLayout.LayoutParams(width, height);
	}

	/**
	 * Method to set the list.
	 * 
	 * @param list
	 */
	public void setList(ArrayList<Integer> list) {
		mListItems = list;
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.favorite_gridview_item,
					null);
			ViewHolder viewholder = new ViewHolder();
			viewholder.thumbnail_imageview = (ImageView) view
					.findViewById(R.id.thumbnail_imageview);
			viewholder.thumbnail_imageview.setLayoutParams(params);
			view.setTag(viewholder);
		}

		Integer item = getItem(position);
		final ViewHolder holder = (ViewHolder) view.getTag();
		Bitmap bitmap = AppUtils.getScaleBitmap(mContext, item, width, height);
		if (bitmap != null) {
			holder.thumbnail_imageview.setImageBitmap(bitmap);
		} else {
			holder.thumbnail_imageview.setImageResource(R.drawable.ic_launcher);
		}
		return view;
	}

	@Override
	public int getCount() {
		if (mListItems != null) {
			return mListItems.size();
		}
		return 0;
	}

	@Override
	public Integer getItem(int postion) {
		if (mListItems != null) {
			return mListItems.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ViewHolder class to hold the views to bind on listview.
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	static class ViewHolder {
		ImageView thumbnail_imageview;
	}

	/**
	 * 
	 * For Freeing up the resources
	 */
	public void cleanUp() {
		mListItems = null;
		mLayoutInflator = null;
		mContext = null;
	}

}