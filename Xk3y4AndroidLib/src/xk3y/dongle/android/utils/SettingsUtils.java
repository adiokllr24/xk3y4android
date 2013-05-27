package xk3y.dongle.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
		
		//int newHeigth = (int) ((min/1.5) * 0.8);
		int newHeigth = (int) ((min/2));
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
	}

}
