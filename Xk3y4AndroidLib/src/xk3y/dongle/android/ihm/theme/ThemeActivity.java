package xk3y.dongle.android.ihm.theme;

import java.util.ArrayList;
import java.util.List;

import org.acra.ErrorReporter;
import org.donations.DonationsActivity;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.ihm.AlphabeticalListActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.LoadingUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class ThemeActivity extends Activity {

	private static String defaultLoadingMessage = "Please wait...";
	private static String progressLoadingMessage = "Please wait...";
	private int error_to_display = 0;
	private ProgressDialog progressDialog;
	private static final int INIT_STARTED = 0;
	private static final int INIT_FINISHED = 1;
	private static final int UPDATE_PROGRESS = 2;
	private static final int UPDATE_FINISHED = 3;
	private boolean SHOW_ERROR = false;
	private boolean SHOW_DEBUG_MSG = false;
	private String debugMsg = "";
	private String currentGameNameToDebug = "";
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INIT_STARTED:
				progressDialog = ProgressDialog.show(ThemeActivity.this, "",
						"Loading. Please wait...", true, false);
				break;
			case UPDATE_PROGRESS:
				progressDialog.setMessage(progressLoadingMessage);
				break;
			case INIT_FINISHED:
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				
				if (SHOW_ERROR) {
					new DialogBoxUtils(ThemeActivity.this,  error_to_display);
				} else if (SHOW_DEBUG_MSG) {
					new DialogBoxUtils(ThemeActivity.this, debugMsg);
				} else {
					initTheme();
				}
				break;
			}
						
		};
	};
	
	private void sendMessage(int what){
        Message msg = Message.obtain();
        msg.what = what;
        handler.sendMessage(msg);
    }
	
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initActivity();
        
    }
	
	public void initActivity() {
		 new Thread(new Runnable() {
				@Override
				public void run() {
					sendMessage(INIT_STARTED);
					initListGames();
					sendMessage(INIT_FINISHED);
				}
		 }).start();
	}
	

	/**
	 * Init the list of game for the current page.
	 */
	private void initListGames() {
		try {
			System.gc();
			List<FullGameInfo> listGames = new ArrayList<FullGameInfo>();
			int cptLoad = ConfigUtils.getConfig().getFirstGameToLoad();
			int cptEnd = ConfigUtils.getConfig().getLastGameToLoad() + 1;
			for (Iso game : ConfigUtils.getConfig().getListeIsoToLoad()){
				cptLoad++;
				currentGameNameToDebug = game.getTitle();
				progressLoadingMessage = defaultLoadingMessage
						+ "\nLoading game "+ cptLoad + "/" + cptEnd
						+ "\n"+ game.getTitle();
				sendMessage(UPDATE_PROGRESS);
				listGames.add(LoadingUtils.loadGameInfo(game));
	
			}
	
			ConfigUtils.getConfig().setListeGames(listGames);
		
			currentGameNameToDebug = "All Games load...";
		} catch (OutOfMemoryError E) {
			//progressDialog.setMessage("boooom");
			SHOW_ERROR = true;
			error_to_display = R.string.out_of_memory_error;
			//Set autoload banner to false
			ConfigUtils.getConfig().setAutoLoadBanners(false);
		} catch (Exception e) {
			if (currentGameNameToDebug == null || (currentGameNameToDebug != null && currentGameNameToDebug.equals(""))) {
				this.finish();
			} else {
				ErrorReporter.getInstance().putCustomData("\n\nLOADIG_GAME", "\n" + currentGameNameToDebug);
				ErrorReporter.getInstance().putCustomData("\n\nDEBUG_MESSAGE", "\n" + debugMsg);
				ErrorReporter.getInstance().handleException(e);
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Création du menu inflater
		MenuInflater inflater = getMenuInflater();

		// On envoi a la variable menu le fichier xml parsé par l'inflater
		inflater.inflate(R.menu.option_menu, menu);

		return true;

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int selectedId = item.getItemId();
		
		if (selectedId == R.id.itemGoToLetter) {
			// Open alphabetical list view
			Intent myIntent = new Intent(ThemeActivity.this, AlphabeticalListActivity.class);
			ThemeActivity.this.startActivity(myIntent);
			return true;
			
		} else if (selectedId == R.id.itemDonate) {
			Intent myIntent = new Intent(ThemeActivity.this, DonationsActivity.class);
			ThemeActivity.this.startActivity(myIntent);
			return true;
			
		} else if (selectedId == R.id.itemQuitter) {
			// On ferme l'activité
			finish();
			return true;
		}


		return super.onMenuItemSelected(featureId, item);

	}
	
	public abstract void initTheme();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Clear bitmap when press back button
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(ThemeActivity.this, R.string.improve_memory, Toast.LENGTH_SHORT).show();
			
			for (FullGameInfo game : ConfigUtils.getConfig().getListeGames()) {
				/*
				LoadingUtils.clearBitmap(game.getBanner());
				LoadingUtils.clearBitmap(game.getCover());
				LoadingUtils.clearBitmap(game.getOriginalCover());
				*/
				game.setBanner(null);
				game.setCover(null);
				game.setOriginalCover(null);
			}
			System.gc();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
