package xk3y.dongle.android;

import java.io.File;

import org.acra.ACRA;

import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.LoadingUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
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
        loadSettings();
        
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
    }

    /**
     * Load user preferences
     */
	private void loadSettings() {
		// Load user preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		ConfigUtils.getConfig().setPreferences(preferences);
		
		// Load ip adress
		String ipAdress = preferences.getString(ConfigUtils.IP_ADRESS, "");
		ConfigUtils.getConfig().setIpAdress(ipAdress);
		
		// Load theme
		/*
		String lightTheme = preferences.getString(ConfigUtils.LIGHT_THEME, "0");
		ConfigUtils.getConfig().setLightTheme(Boolean.valueOf(lightTheme));
		*/
		/*
		// Load cache system
		String cacheData = preferences.getString(ConfigUtils.CACHE_DATA, "1");
		ConfigUtils.getConfig().setCacheData(Boolean.valueOf(cacheData));
		*/
		
		String theme = preferences.getString(ConfigUtils.THEME, String.valueOf(ConfigUtils.THEME_COVER_FLOW));
		ConfigUtils.getConfig().setTheme(Integer.valueOf(theme));
		
		String nbSplit = preferences.getString(ConfigUtils.NB_SPLIT, "0");
		if (Integer.valueOf(nbSplit) > 4) {
			nbSplit = "0";
		}
		ConfigUtils.getConfig().setNbSplit(Integer.valueOf(nbSplit));
		
		String autoLoad = preferences.getString(ConfigUtils.AUTO_LOAD, "0");
		ConfigUtils.getConfig().setAutoLoad(Boolean.valueOf(autoLoad));
		
		// Init cover size
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		int min = width;
		int max = height;
		if (width > height) {
			min = height;
			max = width;
		}
		ConfigUtils.getConfig().setScreenWidth(min);
		ConfigUtils.getConfig().setScreenHeight(max);
		
		//int newHeigth = (int) ((min/1.5) * 0.8);
		int newHeigth = (int) ((min/2));
		ConfigUtils.getConfig().setCoverHeight(newHeigth);
		
		float tmp = ((float)((float)newHeigth / (float)height)) * width;
		int newWidth = (int) tmp;
		ConfigUtils.getConfig().setCoverWidth(newWidth);
	}
	
	public Button getBtListGames() {

		return btListGames;
	}

	public Button getBtSettings() {
		
		return btSettings;
	}
    

	@Override
	public void onBackPressed() {
		xk3yExitDialog exit = new xk3yExitDialog(this, R.string.exit, R.string.exit);
		AlertDialog alert = exit.create();
		alert.show();
	}
}