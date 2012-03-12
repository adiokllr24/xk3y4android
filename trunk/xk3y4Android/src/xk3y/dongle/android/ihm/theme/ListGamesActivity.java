package xk3y.dongle.android.ihm.theme;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.ihm.GameDetailsActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.LoadingUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View vith the games
 * @author maloups
 *
 */
public class ListGamesActivity extends Activity {

	private ListView lvListe;
	List<Iso> maBibliotheque = new ArrayList<Iso>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        setContentView(R.layout.list_games);
        

        lvListe = (ListView)findViewById(R.id.listviewperso);
        
        initData();
       
        ListGamesAdapter adapter = new ListGamesAdapter(this, maBibliotheque);
        
        lvListe.setAdapter(adapter);
        
        adapter.notifyDataSetChanged();
        
        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        
        lvListe.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View v, int position,
					long rowID) {
				
				Iso iso = ConfigUtils.getConfig().getListeGames().get(position);
				ConfigUtils.getConfig().setSelectedGame(iso);
				
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

		List<Iso> biblio;
		LayoutInflater inflater;
		
		public ListGamesAdapter(Context context,List<Iso> biblio) {
			inflater = LayoutInflater.from(context);
			this.biblio = biblio;
		}
		
		@Override
		public int getCount() {
			return biblio.size();
		}

		@Override
		public Object getItem(int position) {
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
			/*
			if (biblio.get(position).getBanner() == null) {
				convertView = getViewWithCover(position, convertView, parent);
			} else {
				convertView = getViewWithBanner(position, convertView, parent);
			}*/
			convertView = getViewWithCover(position, convertView, parent);

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
				holder.tvBanner.setImageBitmap(LoadingUtils.resizeBanner(biblio.get(position).getBanner()));
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
				holder.tvBanner.setImageBitmap(LoadingUtils.resizeCoverForList(biblio.get(position).getOriginalCover()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

	}

}