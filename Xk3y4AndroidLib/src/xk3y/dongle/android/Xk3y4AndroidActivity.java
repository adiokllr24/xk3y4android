package xk3y.dongle.android;

import java.io.File;

import org.acra.ACRA;

import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.LoadingUtils;
import xk3y.dongle.android.utils.SettingsUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main class
 * @author maloups
 *
 */ 
public class Xk3y4AndroidActivity extends Activity {
	
	private Button btListGames;
	private Button btSettings;
	private Button btDonate;
	private TextView textViewVersion;
	private Xk3y4AndroidController controller;
	private static String txtVersion = "by maloups  -  V";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// The following line triggers the initialization of ACRA
    	ACRA.init((Application) this.getApplication());
        super.onCreate(savedInstanceState);
        
        /*
        - Si lecteur dvd fermé, propose d'ouvrir le lecteur puis lance le jeu
        */
        //dalvik.system.VMRuntime.getRuntime().setMinimumHeapSize(64 * 1024 * 1024);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        // create window
        setContentView(R.layout.main);

        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        
        // Load the user settings
        SettingsUtils.loadSettings(this);
        
        // Get IHM Components
        getScreenComponent();
        
        // Add listener
        addController();
        
        File dir = new File (LoadingUtils.GAME_FOLDER_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		// Auto load if necessary
		if (ConfigUtils.getConfig().isAutoLoad()) {
			controller.onClick(btListGames);
		}
		
		if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();  
		}
		
		Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE) ;
		ConfigUtils.getConfig().setVibrator(vibe);
    }
    
	/**
     * Init ihm component
     */
    private void getScreenComponent() {
        // Button to list games
    	btListGames = (Button)findViewById(R.id.bt_list_games);
    	// Button to open setting
    	btSettings = (Button)findViewById(R.id.bt_settings);
    	// Button to Donate
    	btDonate = (Button)findViewById(R.id.bt_donate);
    	
    	try {
			String app_ver = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
			textViewVersion = (TextView)findViewById(R.id.txt_version);
			textViewVersion.setText(txtVersion + app_ver);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    
    /**
     * Init controller and listener
     */
    private void addController() {
        controller = new Xk3y4AndroidController(this);

        btListGames.setOnClickListener(controller);
        btSettings.setOnClickListener(controller);
        btDonate.setOnClickListener(controller);
    }

   
	
	public Button getBtListGames() {

		return btListGames;
	}

	public Button getBtSettings() {
		
		return btSettings;
	}
    

	public Button getBtDonate() {
		return btDonate;
	}

	@Override
	public void onBackPressed() {
		xk3yExitDialog exit = new xk3yExitDialog(this, R.string.exit, R.string.exit);
		AlertDialog alert = exit.create();
		alert.show();
	}
}