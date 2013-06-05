package xk3y.dongle.android.ihm.theme;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.ihm.GameDetailsActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.PaginateButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * View vith the games
 * @author maloups
 *
 */
public class ListGamesActivity extends ThemeActivity {

	private ListView lvListe;
	List<FullGameInfo> maBibliotheque = new ArrayList<FullGameInfo>();
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
        setContentView(R.layout.list_games);
        

        lvListe = (ListView)findViewById(R.id.listviewperso);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        
        int nbPage = ConfigUtils.getConfig().getNbPages();
		if (nbPage != 1) {
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, ConfigUtils.getConfig().getScreenHeight() - 150);
			// Add button if pagination
			paginateLayout = (LinearLayout)findViewById(R.id.paginate_layout_list);
			
			for (int i = 1; i <= nbPage; i++) {
				Button btn = new PaginateButton(this, String.valueOf(i));
				if (i == ConfigUtils.getConfig().getCurrentPage()) {
					btn.setEnabled(false);
				}
				paginateLayout.addView(btn);
			}
		}
		lvListe.setLayoutParams(params);
		
        initData();
       
        ListGamesAdapter adapter = new ListGamesAdapter(this, maBibliotheque);
        
        lvListe.setAdapter(adapter);
        
        //adapter.notifyDataSetChanged();
        
        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        
		
		
        lvListe.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View v, int position,
					long rowID) {
				
				ConfigUtils.getConfig().vivrate();
				
				FullGameInfo FullGameInfo = ConfigUtils.getConfig().getListeGames().get(position);
				ConfigUtils.getConfig().setSelectedGame(FullGameInfo);
				
				// Open liste games window
				Intent myIntent = new Intent(ListGamesActivity.this, GameDetailsActivity.class);
				ListGamesActivity.this.startActivity(myIntent);
			}
		});
	}
	
	private void initData() {
		maBibliotheque.clear();
		maBibliotheque = ConfigUtils.getConfig().getListeGames();
	}

	public class ListGamesAdapter extends BaseAdapter {

		List<FullGameInfo> biblio;
		LayoutInflater inflater;
		
		public ListGamesAdapter(Context context,List<FullGameInfo> biblio) {
			inflater = LayoutInflater.from(context);
			this.biblio = biblio;
		}
		
		@Override
		public int getCount() {
			if (biblio == null) {
				ListGamesActivity.this.finish();
			}
			return biblio.size();
		}

		@Override
		public Object getItem(int position) {
			if (biblio == null) {
				ListGamesActivity.this.finish();
			}
			return biblio.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private class ViewHolder {
			TextView tvTitre;
			ImageView tvBanner;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (biblio == null) {
				ListGamesActivity.this.finish();
			}
			
			if (ConfigUtils.getConfig().getTheme() == ConfigUtils.THEME_BANNER_LIST) {
				convertView = getViewWithBanner(position, convertView, parent);
			} else {
				convertView = getViewWithCover(position, convertView, parent);
			}

			return convertView;
		}
		
		/** Return the view with Banner */
		private View getViewWithBanner(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_with_banner, null);
				//holder.tvTitre = (TextView)convertView.findViewById(R.id.tvTitre);
				holder.tvBanner = (ImageView)convertView.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			//holder.tvTitre.setText(biblio.get(position).getTitre());
			try {
				Bitmap img = biblio.get(position).getBanner();
				if (img == null) {
					img = ConfigUtils.getConfig().getDefaultBanner();
				}
				holder.tvBanner.setImageBitmap(img);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}
		
		/** Return the view with Cover */
		private View getViewWithCover(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_with_cover, null);
				holder.tvTitre = (TextView)convertView.findViewById(R.id.titre);
				holder.tvBanner = (ImageView)convertView.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tvTitre.setText(biblio.get(position).getTitle());
			try {
				holder.tvBanner.setImageBitmap(biblio.get(position).getCover());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

	}


}