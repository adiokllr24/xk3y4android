package xk3y.dongle.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;

public class SettingsUtils {
	
	 /**
     * Load user preferences
     */
	public static void loadSettings(final Activity activity) {
		// Load user preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		ConfigUtils.getConfig().setPreferences(preferences);
		
		// Load ip adress
		String ipAdress = preferences.getString(ConfigUtils.IP_ADRESS, "");
		ConfigUtils.getConfig().setIpAdress(ipAdress);
		
		// Load auto load banner
		String autoLoadBanner = preferences.getString(ConfigUtils.AUTO_LOAD_BANNERS, "true");
		Log.e("Error: ",autoLoadBanner);
		ConfigUtils.getConfig().setAutoLoadBanners(Boolean.valueOf(autoLoadBanner));
		
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
		Display display = activity.getWindowManager().getDefaultDisplay();
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
		
		int heigthDiviser = 2;
		if (SettingsUtils.isTablet(activity)) {
			heigthDiviser = 3;
		}
		//int newHeigth = (int) ((min/1.5) * 0.8);
		int newHeigth = (int) ((min/heigthDiviser));
		ConfigUtils.getConfig().setCoverHeight(newHeigth);
		
		float tmp = ((float)((float)newHeigth / (float)height)) * width;
		int newWidth = (int) tmp;
		ConfigUtils.getConfig().setCoverWidth(newWidth);
	}
	
	
	/**
     * Load user preferences
     */
	public static void loadMinimalSettings(final Context context) {
		// Load user preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		ConfigUtils.getConfig().setPreferences(preferences);
		
		// Load ip adress
		String ipAdress = preferences.getString(ConfigUtils.IP_ADRESS, "");
		ConfigUtils.getConfig().setIpAdress(ipAdress);
		
		// Load auto load banner
		String autoLoadBanner = preferences.getString(ConfigUtils.AUTO_LOAD_BANNERS, "1");
		ConfigUtils.getConfig().setAutoLoadBanners(Boolean.valueOf(autoLoadBanner));
	}
	
	/**
	 * Checks if the device is a tablet or a phone
	 * 
	 * @param activityContext
	 *            The Activity Context.
	 * @return Returns true if the device is a Tablet
	 */
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    boolean isTablet = (xlarge || large);
	    ConfigUtils.getConfig().setTablet(true);
	    return isTablet;
	}

}
