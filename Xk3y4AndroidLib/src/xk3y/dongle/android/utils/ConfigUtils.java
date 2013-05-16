package xk3y.dongle.android.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.ihm.theme.ThemeActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Vibrator;



/**
 * Load settings app
 *
 * @author maloups
 *
 */
public final class ConfigUtils implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final String IP_ADRESS = "IP_ADRESSS";
    public static final String CACHE_DATA = "CACHE_DATA";
    public static final String AUTO_LOAD = "AUTO_LOAD";
    public static final String THEME = "THEME";
    public static final String NB_SPLIT = "NB_SPLIT";
    
    public static final int THEME_COVER_FLOW = 0;
    public static final int THEME_COVER_FLOW_LIGHT = 1;
    public static final int THEME_BANNER_LIST = 2;
    public static final int THEME_COVER_LIST = 3;
    
    public static final int NB_GAME_INCREMENT = 3;
    
	/** The Xkey IP adress */
	private String ipAdress;
	/** The default Cover */
	private Bitmap defaultCover;
	/** The default Banner */
	private Bitmap defaultBanner;
	/** The settings file */
	SharedPreferences preferences;
	/** The xkey object with list games and infos */
	private Xkey xkey;
	/** The list of games to display*/
	private List<FullGameInfo> listeGames;
	/** The list of games In all partitions allready sort */
	private List<Iso> listeAllGames;
	/** The list of games to display */
	private List<Iso> listeIsoToLoad;
	/** The game selected by the user */
	private FullGameInfo selectedGame;
	/** The screen with */
	private int screenWidth;
	/** The screen with */
	private int screenHeight;
	/** The cover with */
	private int coverWidth;
	/** The cover height */
	private int coverHeight;
	/** Avoid to reload All Cover */
	private boolean alreadyLoad = false;
	/** Light theme without reflection */
	//private boolean lightTheme = false;
	/** Cache data to start more quicly */
	private boolean cacheData = true;
	/** The selected theme */
	private int theme;
	/** The selected nb split by page */
	private int nbSplit;
	/** Index of the current page */
	private int currentPage = 1;
	/** Games auto loading */
	private boolean autoLoad = false;
	/** The current theme activity */
	private ThemeActivity currentActivity;
	/** The vibrator when click a button */
	Vibrator vibrator;
	
	private int widgetGameindex= 0;
	
	private static ConfigUtils instance;


	private ConfigUtils() {
		
	}
	
	public static ConfigUtils getConfig() {
		if (instance == null) {
			instance = new ConfigUtils();
		}
		return instance;
	}


	private void savePreferences(String key, String value) {
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public Xkey getXkey() {
		return xkey;
	}

	public void setXkey(Xkey xkey) {
		this.xkey = xkey;
		LoadingUtils.saveXkeyOnSdCard(xkey);
	}

	
	public List<FullGameInfo> getListeGames() {
		return listeGames;
	}

	public void setListeGames(List<FullGameInfo> listeGames) {
		this.listeGames = listeGames;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
		savePreferences(IP_ADRESS, ipAdress);
	}

	public Bitmap getDefaultCover() {
		return defaultCover;
	}

	public void setDefaultCover(Bitmap defaultCover) {
		this.defaultCover = defaultCover;
	}

	
	public Bitmap getDefaultBanner() {
		return defaultBanner;
	}

	public void setDefaultBanner(Bitmap defaultBanner) {
		this.defaultBanner = defaultBanner;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public int getCoverWidth() {
		return coverWidth;
	}

	public void setCoverWidth(int coverWidth) {
		this.coverWidth = coverWidth;
	}

	public int getCoverHeight() {
		return coverHeight;
	}

	public void setCoverHeight(int coverHeight) {
		this.coverHeight = coverHeight;
	}

	public boolean isAlreadyLoad() {
		return alreadyLoad;
	}

	public void setAlreadyLoad(boolean alreadyLoad) {
		this.alreadyLoad = alreadyLoad;
	}

	public FullGameInfo getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(FullGameInfo selectedGame) {
		this.selectedGame = selectedGame;
	}
	
	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
		savePreferences(THEME, String.valueOf(theme));
	}

	
	public int getNbSplit() {
		return nbSplit;
	}

	public void setNbSplit(int nbSplit) {
		this.nbSplit = nbSplit;
		savePreferences(NB_SPLIT, String.valueOf(nbSplit));
	}

	public boolean isAutoLoad() {
		return autoLoad;
	}

	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
		savePreferences(AUTO_LOAD, String.valueOf(autoLoad));
	}

	public boolean isCacheData() {
		return cacheData;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public List<Iso> getListeAllGames() {
		return listeAllGames;
	}

	public void setListeAllGames(List<Iso> listeAllGames) {
		this.listeAllGames = listeAllGames;
	}

	public List<Iso> getListeIsoToLoad() {
		return listeIsoToLoad;
	}

	public void setListeIsoToLoad(List<Iso> listeIsoToLoad) {
		this.listeIsoToLoad = listeIsoToLoad;
	}
	
	public ThemeActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(ThemeActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public Vibrator getVibrator() {
		return vibrator;
	}

	public void setVibrator(Vibrator vibrator) {
		this.vibrator = vibrator;
	}

	/**
	 * Vibrate the device
	 */
	public void vivrate() {
		if (vibrator != null) {
			vibrator.vibrate(30);
		}
	}
	
	/** Switch theme, load the banner */
	public boolean loadBanner() {
		boolean res = false;
		if (getTheme() == THEME_BANNER_LIST) {
			res = true;
		}
		return res;
	}
	
	/** Switch theme, add title over cover */
	public boolean addCoverTitle() {
		boolean res = false;
		if (getTheme() == THEME_COVER_FLOW || getTheme() == THEME_COVER_FLOW_LIGHT) {
			res = true;
		}
		return res;
	}
	
	/**
	 * Get the number of pages to display
	 * @return the number of pages to display
	 */
	public int getNbPages() {
		return nbSplit + 1;
	}
	
	
	/**
	 * Get the number of games to load
	 * @return the number of games to load
	 */
	public int getNbGamesToLoad() {
		int nbGames = getListeAllGames().size();
		if (getNbPages() > 1) {
			nbGames = (getListeAllGames().size() / getNbPages()) + 1;
		}
		return nbGames;
	}
	
	/**
	 * Index of the first game to load
	 * @param numPage number of the page to load
	 * @return Index of the first game to load
	 */
	public int getFirstGameToLoad() {
		return (getCurrentPage() - 1) * getNbGamesToLoad();
	}
	
	/**
	 * Index of the last game to load
	 * @param numPage number of the page to load
	 * @return Index of the last game to load
	 */
	public int getLastGameToLoad() {
		int end = getCurrentPage() *  getNbGamesToLoad();
		if (end >= getListeAllGames().size()) {
			end = getListeAllGames().size();
		}
		return end - 1;
	}

	/**
	 * Index if the current page
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Set the index of the current page
	 * <br />Load liste of game associate of this page
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		listeIsoToLoad = new ArrayList<Iso>();
		int start = getFirstGameToLoad();
		int end = getLastGameToLoad();
		for (int i = start; i <= end; i++) {
			listeIsoToLoad.add(listeAllGames.get(i));
		}
	}

	public int getWidgetGameindex() {
		return widgetGameindex;
	}

	public void setWidgetGameindex(int widgetGameindex) {
		this.widgetGameindex = widgetGameindex;
	}
	
	public void incrementGameIndex(){
		if (this.widgetGameindex < listeGames.size()-1){
		this.widgetGameindex++;
		}else{
			this.widgetGameindex = 0;
		}
	}
	
	public void decrementGameIndex(){
		if (this.widgetGameindex > 0){
		this.widgetGameindex--;
		}else{
			this.widgetGameindex = listeGames.size()-1;
		}
	}
	
	

	
	
	/*
	public void setCacheData(boolean cacheData) {
		this.cacheData = cacheData;
		savePreferences(CACHE_DATA, String.valueOf(cacheData));
	}
*/
	
	
}
