package xk3y.dongle.android;

import java.util.Locale;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import xk3y.dongle.android.utils.ConfigUtils;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;

@ReportsCrashes(formKey = "", // will not be used
mailTo = "maloups.dev@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class Xk3y4AndroidApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// Load default cover
		ConfigUtils.getConfig().setDefaultCover(
				BitmapFactory.decodeResource(getResources(), R.drawable.default_cover));
		
		ConfigUtils.getConfig().setDefaultBanner(
				BitmapFactory.decodeResource(getResources(), R.drawable.default_banner));
		
		
		// changeLanguage("en");
		
		Intent myIntent = new Intent(this, Xk3y4AndroidActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra("EXIT", true);
		this.startActivity(myIntent);
	}

	/**
	 * Change the language of the app
	 * @param local
	 */
	public void changeLanguage(String local) {
		String languageToLoad = local; // your language
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

	}

}
