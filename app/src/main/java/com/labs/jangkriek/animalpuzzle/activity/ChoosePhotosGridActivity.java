/**
 * 
 */
package com.labs.jangkriek.animalpuzzle.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.adapter.GambarAdapter;
import com.labs.jangkriek.animalpuzzle.adapter.GridViewAdapter;
import com.labs.jangkriek.animalpuzzle.model.Gambar;
import com.labs.jangkriek.animalpuzzle.util.RecyclerTouchListener;
import com.labs.jangkriek.animalpuzzle.util.SettingsPreferences;
import com.roger.catloadinglibrary.CatLoadingView;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * The class to show screen for choosing photos to play puzzle game
 * 
 * @author vishalbodkhe
 * 
 */
public class ChoosePhotosGridActivity extends AppCompatActivity {

	private Context mContext;
	private TextView error_textview;
	private View contents_view;
	private GridView grid_view;
	private ArrayList<Integer> mContentItemList;
	private GridViewAdapter mAllGridAdapter;
	private CatLoadingView catLoadingView;
	private GambarAdapter gambarAdapter;
	private ArrayList<Gambar> gambarArrayList;
	private RecyclerView recyclerView;
	private RelativeLayout loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_favorite_photos_layout);
		setResult(RESULT_CANCELED);
		mContext = ChoosePhotosGridActivity.this;
		recyclerView = findViewById(R.id.recyler_image);
		loading = findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		gambarArrayList = new ArrayList<>();
		gambarArrayList.add(new Gambar(R.drawable.image1, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image21, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image31, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image2, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image22, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image32, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image3, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image23, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image33, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image4, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image24, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image29, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image5, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image25, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image30, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image6, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image26, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image7, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image27, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image8, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image28, "a"));
		gambarArrayList.add(new Gambar(R.drawable.image34, "a"));


		gambarAdapter = new GambarAdapter(gambarArrayList);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.setItemAnimator(new SlideInDownAnimator());
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new AlphaInAnimationAdapter(gambarAdapter));
		gambarAdapter.notifyDataSetChanged();
		initViews();
		//mContentItemList= ResourcesContentManager.getInstance().getImageItemList();
		populateGridViewContents();

		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
				recyclerView, new RecyclerTouchListener.ClickListener() {
			@Override
			public void onClick(View view, final int position) {
				loading.setVisibility(View.VISIBLE);
				mHandler.sendEmptyMessageDelayed(1, 1100);
				SettingsPreferences.setPhotoPosition(mContext, position);
			}

			@Override
			public void onLongClick(View view, int position) {

			}
		}));
	}

	/**
	 * Method to populate the grid view contents
	 */
	private void populateGridViewContents() {
		if (mContentItemList != null && mContentItemList.size() > 0) {
			if (mAllGridAdapter == null) {
				mAllGridAdapter = new GridViewAdapter(mContext);
				//grid_view.setAdapter(mAllGridAdapter);
			}
			mAllGridAdapter.setList(mContentItemList);
			contents_view.setVisibility(View.VISIBLE);
			error_textview.setVisibility(View.GONE);
		}
	}

	private RelativeLayout bottom_ads_view;

	/**
	 * This method to inits components
	 */
	private void initViews() {
		catLoadingView = new CatLoadingView();
		bottom_ads_view = (RelativeLayout) findViewById(R.id.bottom_ads_view);
		contents_view = findViewById(R.id.bottom_container_view);
		error_textview = (TextView) findViewById(R.id.error_textview);
		//grid_view = (GridView) findViewById(R.id.grid_view);
		/*grid_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				loading.setVisibility(View.VISIBLE);
				mHandler.sendEmptyMessageDelayed(1, 1100);
				SettingsPreferences.setPhotoPosition(mContext, position);
			}
		});*/
	}

	public void showDialog() {
		catLoadingView.show(getSupportFragmentManager(), "");
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			Intent intent = new Intent(mContext, SlidePuzzleActivity.class);
			startActivity(intent);
		}
	};

	public interface ItemClickListener {
		void onClick(View view, int position);
	}

	@Override
	public void onDestroy() {
		if (mContext != null) {
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
