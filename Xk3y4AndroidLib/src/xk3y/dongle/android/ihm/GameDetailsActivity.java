package xk3y.dongle.android.ihm;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.ImageUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main class
 * @author maloups
 *
 */
public class GameDetailsActivity extends Activity {
	
	private static final int SPIT_SIZE = 200;
	private ImageView coverView;
	private TextView textGameTitle;
	private TextView textGenre;
	private TextView textSummary;
	private Button btLaunchGame;
	private Button btBack;
	private Button btTrailer;
	private GameDetailsController controller;
	private FullGameInfo currentGame;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        
        // create window
        setContentView(R.layout.game_details);

        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);


        // Get the selected game
        currentGame = ConfigUtils.getConfig().getSelectedGame();
        
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
	    	btLaunchGame = (Button)findViewById(R.id.bt_launch);
	    	// Button to back
	    	btBack = (Button)findViewById(R.id.bt_back);
	    	// Button trailer
	    	btTrailer =  (Button)findViewById(R.id.bt_trailer);
	    	if (currentGame.getTrailer() == null || 
	    			(currentGame.getTrailer() != null 
	    				&& (currentGame.getTrailer().equals("")
	    				|| currentGame.getTrailer().equals(" ")))) {
	    		btTrailer.setVisibility(View.INVISIBLE);
	    	}

	    	// The cover
	    	coverView = (ImageView)findViewById(R.id.img_cover_view);
	    	coverView.setImageBitmap(
	    			ImageUtils.resizeCoverForGameDetails(currentGame.getOriginalCover()));
	    	
	    	// The title
	    	textGameTitle = (TextView)findViewById(R.id.txt_title);
	    	textGameTitle.setText(currentGame.getTitle());
	    	
	    	// The Genre
	    	textGenre = (TextView)findViewById(R.id.txt_genre);
	    	if (currentGame.getGender() != null) {
	    		textGenre.setText(currentGame.getGender());
	    	} else {
	    		textGenre.setText(" ");
	    	}
	
	    	// The Genre
	    	textSummary = (TextView)findViewById(R.id.txt_summary);
	    	if (currentGame.getSummary() != null) {
	    		String txtSummary = currentGame.getSummary();
	    		if (txtSummary.length() >= SPIT_SIZE) {
	    			txtSummary = txtSummary.substring(0, 200) + "...";
	    		}
	    		
	    		textSummary.setText(txtSummary);
	    	} else {
	    		textSummary.setText(" ");
	    	}
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Init controller and listener
     */
    private void addController() {
        controller = new GameDetailsController(this);

        btLaunchGame.setOnClickListener(controller);
        btBack.setOnClickListener(controller);
        btTrailer.setOnClickListener(controller);
        textSummary.setOnClickListener(controller);
    }

	public Button getBtLaunchGame() {
		return btLaunchGame;
	}

	public Button getBtBack() {
		return btBack;
	}

	
	public Button getBtTrailer() {
		return btTrailer;
	}

	public FullGameInfo getCurrentGame() {
		return currentGame;
	}

	public TextView getTextSummary() {
		return textSummary;
	}


    
    
}