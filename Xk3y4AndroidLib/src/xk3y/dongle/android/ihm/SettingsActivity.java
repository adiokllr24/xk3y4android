package xk3y.dongle.android.ihm;

import xk3y.dongle.android.R;
import xk3y.dongle.android.utils.ConfigUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Main class
 * @author maloups
 *
 */
public class SettingsActivity extends Activity {
	
	//private CheckBox ckbCacheData;
	private Spinner spinnerTheme;
	private Spinner spinnerSplit;
	//private CheckBox ckbLightTheme;
	private CheckBox ckbClearCache;
	//private CheckBox ckbAutoLoad;
	private CheckBox ckbGetBanner;
	private EditText textIp;
	private Button btSave;
	private Button btBack;
	private SettingsController controller;
	private int saveTheme;
	private int saveNbSplit;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        
        // create window
        setContentView(R.layout.settings);

        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        
        // Get IHM Components
        getScreenComponent();
        
        // Add listener
        addController();
        
    }
    
	/**
     * Init ihm component
     */
    private void getScreenComponent() {
    	try {
	        // Button to launch the game
	    	btSave = (Button)findViewById(R.id.bt_save);
	    	// Button to back
	    	btBack = (Button)findViewById(R.id.bt_back);
	    	
	    	textIp = (EditText)findViewById(R.id.txt_ip);
	    	textIp.setText(ConfigUtils.getConfig().getIpAdress());
	    	
	    	//ckbCacheData = (CheckBox)findViewById(R.id.ckb_cache_info);
	    	//ckbCacheData.setChecked(ConfigUtils.getConfig().isCacheData());
	    	
	    	//ckbLightTheme = (CheckBox)findViewById(R.id.ckb_light_theme);
	    	//ckbLightTheme.setChecked(ConfigUtils.getConfig().isLightTheme());
	    	
	    	//ckbAutoLoad = (CheckBox)findViewById(R.id.ckb_auto_start);
	    	//ckbAutoLoad.setChecked(ConfigUtils.getConfig().isAutoLoad());
	    	
	    	ckbClearCache = (CheckBox)findViewById(R.id.ckb_clear_cache);
	    	ckbClearCache.setChecked(false);
	    	
	    	ckbGetBanner = (CheckBox)findViewById(R.id.ckb_get_banner);
	    	ckbGetBanner.setChecked(ConfigUtils.getConfig().isAutoLoadBanners());
	    	
	    	spinnerTheme = (Spinner) findViewById(R.id.spinner_theme);
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	                this, R.array.theme_array, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerTheme.setAdapter(adapter);
	        saveTheme = ConfigUtils.getConfig().getTheme();
	        spinnerTheme.setSelection(saveTheme);
	        
	        spinnerSplit = (Spinner) findViewById(R.id.spinner_split);
	        ArrayAdapter<CharSequence> adapterSplit = ArrayAdapter.createFromResource(
	                this, R.array.split_array, android.R.layout.simple_spinner_item);
	        adapterSplit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerSplit.setAdapter(adapterSplit);
	        saveNbSplit = ConfigUtils.getConfig().getNbSplit();
	        spinnerSplit.setSelection(saveNbSplit);
	        //spinnerSplit.setEnabled(false);
	        
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

    /**
     * Init controller and listener
     */
    private void addController() {
        controller = new SettingsController(this);

        btSave.setOnClickListener(controller);
        btBack.setOnClickListener(controller);
    }
/*
	public CheckBox getCkbCacheData() {
		return ckbCacheData;
	}

	public CheckBox getCkbLightTheme() {
		return ckbLightTheme;
	}
*/
	public EditText getTextIp() {
		return textIp;
	}

	public Button getBtSave() {
		return btSave;
	}

	public Button getBtBack() {
		return btBack;
	}

	public CheckBox getCkbClearCache() {
		return ckbClearCache;
	}

	public Spinner getSpinnerTheme() {
		return spinnerTheme;
	}

	/*
	public CheckBox getCkbAutoLoad() {
		return ckbAutoLoad;
	}*/

	public int getSaveTheme() {
		return saveTheme;
	}

	public Spinner getSpinnerSplit() {
		return spinnerSplit;
	}

	public CheckBox getCkbGetBanner() {
		return ckbGetBanner;
	}

	public void setCkbGetBanner(CheckBox ckbGetBanner) {
		this.ckbGetBanner = ckbGetBanner;
	}
	
	

	
	
	
	
    
}