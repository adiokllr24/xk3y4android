package xk3y.dongle.android.ihm;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.ihm.theme.ListGamesActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AlphabeticalListActivity extends Activity {

	ListView lvListe;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		lvListe = (ListView) findViewById(R.id.lvListAlphabetical);

		String[] listeStrings = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };

		lvListe.setAdapter(new AlphabeticalAdapter(this,
				android.R.layout.simple_list_item_1, listeStrings));
		
		lvListe.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View v, int position,
					long rowID) {
				
				ConfigUtils.getConfig().vivrate();
				
				
			}
		});

	}

}
