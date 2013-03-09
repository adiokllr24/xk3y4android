package xk3y.dongle.android;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.acra.ErrorReporter;

import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.ihm.SettingsActivity;
import xk3y.dongle.android.ihm.theme.CoverFlowGamesActivity;
import xk3y.dongle.android.ihm.theme.ListGamesActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.HttpServices;
import xk3y.dongle.android.utils.LoadingUtils;
import xk3y.dongle.android.utils.Xk3yParserUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Listener for the main view
 * @author maloups
 *
 */
public class Xk3y4AndroidController implements OnClickListener {
	
	public String urlToListGames = "http://" + ConfigUtils.getConfig().getIpAdress() + "/data.xml";
	
	/** the view */
	private Xk3y4AndroidActivity view;
	
	private static String defaultLoadingMessage = "Please wait...";
	private static String progressLoadingMessage = "Please wait...";
	private int error_to_display = 0;
	private ProgressDialog progressDialog;
	private static final int UPDATE_STARTED = 0;
	private static final int UPDATE_FINISHED = 1;
	private static final int UPDATE_PROGRESS = 2;
	private boolean SHOW_ERROR = false;
	private boolean SHOW_DEBUG_MSG = false;
	private String debugMsg = "";
	private String currentGameNameToDebug = "";
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_STARTED:
				progressDialog = ProgressDialog.show(view, "",
						"Loading. Please wait...", true, false);
				break;
			case UPDATE_PROGRESS:
				progressDialog.setMessage(progressLoadingMessage);
				break;
			case UPDATE_FINISHED:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				
				if (SHOW_ERROR) {
					new DialogBoxUtils(view,  error_to_display);
				} else if (SHOW_DEBUG_MSG) {
					new DialogBoxUtils(view, debugMsg);
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
	
	/** Contructor */
	public Xk3y4AndroidController (Xk3y4AndroidActivity maVue) {
		this.view = maVue;
	}

	/**
	 * Evenement r�lalis� lors d'un click sur un bouton
	 */
	public void onClick(View v) {
		SHOW_ERROR = false;
		SHOW_DEBUG_MSG = false;
		if(v == view.getBtListGames()) {
			ConfigUtils.getConfig().vivrate();
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendMessage(UPDATE_STARTED);
					listGames();
					
				}

			}).start();
			
		} else if (v == view.getBtSettings()) {
			ConfigUtils.getConfig().vivrate();
			settings();
		}
	}


	/**
	 * Event to load games view
	 */
	private void listGames() {
		try {
			SHOW_ERROR = false;
			debugMsg = "List Games...";
			String ipAdress = ConfigUtils.getConfig().getIpAdress();
			if (ipAdress == null || (ipAdress != null && ipAdress.length() == 0)) {
				SHOW_ERROR = true;
				error_to_display = R.string.no_ip;
				sendMessage(UPDATE_FINISHED);
			}
			urlToListGames = "http://" + ipAdress
					+ "/data.xml";
			String xkeyInfo = HttpServices.getInstance().getResponseFromUrl(
					urlToListGames);
			
			ErrorReporter.getInstance().putCustomData("\n\nTHEME", String.valueOf(ConfigUtils.getConfig().getTheme()));
			ErrorReporter.getInstance().putCustomData("\n\nDATA_XML", "\n" + xkeyInfo);
			
			debugMsg += "\nList Games OK";
			if (xkeyInfo == null
					|| (xkeyInfo != null && xkeyInfo.equals(""))) {
				
				debugMsg += "\nNo Xkey Connexion";
				Xkey xkey = LoadingUtils.loadXkeyFromSdCard();
				if (xkey == null) {
					SHOW_ERROR = true;
					error_to_display = R.string.not_connected;
				} else {
					ConfigUtils.getConfig().setXkey(xkey);
				}
			} else {
				// Generate xkey object from xml flow
				debugMsg += "\nConvert XML to Object...";
				Reader reader = new StringReader(xkeyInfo);
				Xkey xkey = Xk3yParserUtils.getXkey(reader);
				ConfigUtils.getConfig().setXkey(xkey);
				debugMsg += "\nConvert XML to Object OK";
			}	
			
			if (!SHOW_ERROR) {
				List<Iso> listIsos = ConfigUtils.getConfig().getXkey().getListeGames();
				if (listIsos == null || (listIsos != null && listIsos.isEmpty())) {
					debugMsg += "\nNo Game detected";
					SHOW_ERROR = true;
					error_to_display = R.string.no_games;
					
					sendMessage(UPDATE_FINISHED);
				} else {
					Collections.sort(listIsos, Iso.TitleComparator);
					ConfigUtils.getConfig().setListeAllGames(listIsos);
					ConfigUtils.getConfig().setCurrentPage(1);
					
					/*
					// Load game info
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
					 */
					
					sendMessage(UPDATE_FINISHED);
					
					// Open list games window
					debugMsg += "\nLaunch CoverFlow...";
					
					Intent myIntent = null;
					if (ConfigUtils.getConfig().addCoverTitle()) {
						myIntent = new Intent(view, CoverFlowGamesActivity.class);
					} else {
						myIntent = new Intent(view, ListGamesActivity.class);
					}
					view.startActivity(myIntent);
				}
			}
			
			
		} catch (OutOfMemoryError E) {
			//progressDialog.setMessage("boooom");
			SHOW_ERROR = true;
			error_to_display = R.string.out_of_memory_error;
		} catch (Exception e) {
			
			ErrorReporter.getInstance().putCustomData("\n\nDEBUG_MESSAGE", "\n" + debugMsg);
			ErrorReporter.getInstance().handleException(e);

			//progressDialog.setMessage("boooom");
			//SHOW_DEBUG_MSG = true;
			//debugMsg += "\n" + e.getMessage();
			//error_to_display = R.string.error;
		}
		
	}

	/**
	 * Action permettant de creer un compte
	 */
	private void settings() {
		Intent myIntent = new Intent(view, SettingsActivity.class);
		view.startActivity(myIntent);
		
		/*
		PromptDialog dlg = new PromptDialog(view, R.string.title_settings,
				R.string.comment_setting, InputType.TYPE_CLASS_TEXT,
				ConfigUtils.getConfig().getIpAdress()) {
			
			@Override
			public boolean onOkClicked(String input) {
				Toast.makeText(view, "IP Save : " + input, Toast.LENGTH_SHORT).show();
				
				ConfigUtils.getConfig().setIpAdress(input);
				
				return true; // true = close dialog
			}
		};
		dlg.show();
		*/
	}
	
}
