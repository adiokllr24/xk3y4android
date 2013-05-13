/*
 * Copyright (C) 2010 Neil Davies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code is base on the Android Gallery widget and was Created 
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 * 
 * @author Neil Davies
 */
package xk3y.dongle.android.ihm.theme;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.ihm.GameDetailsActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.CoverFlow;
import xk3y.dongle.android.utils.PaginateButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CoverFlowGamesActivity extends ThemeActivity {
	private CoverFlow coverFlow;
	private List<FullGameInfo> listGames = new ArrayList<FullGameInfo>();
	private LinearLayout paginateLayout;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		ConfigUtils.getConfig().setCurrentActivity(this);
	}

	@Override
	public void initTheme() {

		listGames = ConfigUtils.getConfig().getListeGames();
		if (listGames == null) {
			CoverFlowGamesActivity.this.finish();
		}
		//CoverFlow coverFlow;
		coverFlow = new CoverFlow(this);
		coverFlow.setAdapter(new ImageAdapter(this));

		ImageAdapter coverImageAdapter = new ImageAdapter(this);

		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setAlwaysDrawnWithCacheEnabled(true);

		// Set spacing between cover
		int spacing = -50;
		coverFlow.setSpacing(spacing);
		
		// Selected the default game
		int selected = 0;
		if (listGames.size() > 2) {
			selected = 2;
		}
		coverFlow.setSelection(selected, true);
		coverFlow.setAnimationDuration(1000);
		coverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View v, int position,
					long rowID) {
				
				ConfigUtils.getConfig().vivrate();
				FullGameInfo FullGameInfo = ConfigUtils.getConfig().getListeGames().get(position);
				ConfigUtils.getConfig().setSelectedGame(FullGameInfo);
				
				// Open liste games window
				Intent myIntent = new Intent(CoverFlowGamesActivity.this, GameDetailsActivity.class);
				CoverFlowGamesActivity.this.startActivity(myIntent);
			}
		});
		

		//setContentView(coverFlow);
		setContentView(R.layout.cover_flow);
		
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int orientation = display.getOrientation();
		
		// Search if user want to split by page

		int nbPage = ConfigUtils.getConfig().getNbPages();
		if (nbPage != 1) {
			if(orientation ==  0) {
				//portrait
				addContentView(coverFlow, new LayoutParams(LayoutParams.FILL_PARENT, ConfigUtils.getConfig().getScreenWidth() + 100));
			} else {
				//landscape
				addContentView(coverFlow, new LayoutParams(display.getWidth() - 100, LayoutParams.FILL_PARENT));
			}
			// Add button if pagination
			paginateLayout = (LinearLayout)findViewById(R.id.paginate_layout);
			for (int i = 1; i <= nbPage; i++) {
				Button btn = new PaginateButton(this, String.valueOf(i));
				if (i == ConfigUtils.getConfig().getCurrentPage()) {
					btn.setEnabled(false);
				}
				paginateLayout.addView(btn);
			}
		} else {
			addContentView(coverFlow, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	
		// Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);

	}
	
	
	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			if (listGames == null) {
				CoverFlowGamesActivity.this.finish();
			}
			return listGames.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (listGames == null) {
				CoverFlowGamesActivity.this.finish();
			}
			final FullGameInfo FullGameInfo = listGames.get(position);
			// Use this code if you want to load from resources
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(FullGameInfo.getCover());

			int width = FullGameInfo.getCover().getWidth();
			int height = FullGameInfo.getCover().getHeight();
			/*
			int min = ConfigUtils.getConfig().getCoverWidth();
			int max = ConfigUtils.getConfig().getCoverHeight();
			if (min > max) {
				max = min;
				min = ConfigUtils.getConfig().getCoverHeight();
			}
			int width = min;
			int height = max;
			*/
			i.setLayoutParams(new CoverFlow.LayoutParams(width, height));
			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			// Make sure we set anti-aliasing otherwise we get jaggies
			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			drawable.setAntiAlias(true);
			
			return i;

		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

	}

}