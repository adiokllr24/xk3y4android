package xk3y.dongle.android;

import org.acra.ErrorReporter;
import org.donations.DonationsActivity;

import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.ihm.SettingsActivity;
import xk3y.dongle.android.ihm.theme.CoverFlowGamesActivity;
import xk3y.dongle.android.ihm.theme.ListGamesActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.XkeyGamesUtils;
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
	 * Evenement realise lors d'un click sur un bouton
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
		} else if (v == view.getBtDonate()) {
			ConfigUtils.getConfig().vivrate();
			donate();
		}
	}


	/**
	 * Event to load games view
	 */
	private void listGames() {
		try {
			
			XkeyResult result =  XkeyGamesUtils.listGames();
			sendMessage(UPDATE_FINISHED);
			if (result.isShowError()){
				SHOW_ERROR = true;
				error_to_display = result.getMessageCode();
			}else{
				Intent myIntent = null;
				if (ConfigUtils.getConfig().addCoverTitle()) {
					myIntent = new Intent(view, CoverFlowGamesActivity.class);
				} else {
					myIntent = new Intent(view, ListGamesActivity.class);
				}
				view.startActivity(myIntent);
			}
			
		
		} catch (XkeyException e) {
			ErrorReporter.getInstance().putCustomData("\n\nDEBUG_MESSAGE", "\n" + e.getMessage());
			ErrorReporter.getInstance().handleException(e);
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
	
	/**
	 * Action permettant de faire un don
	 */
	private void donate() {
		Intent myIntent = new Intent(view, DonationsActivity.class);
		view.startActivity(myIntent);
	}
}
