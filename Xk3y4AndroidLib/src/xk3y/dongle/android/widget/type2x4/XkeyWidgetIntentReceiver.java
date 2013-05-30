package xk3y.dongle.android.widget.type2x4;

import xk3y.dongle.android.R;
import xk3y.dongle.android.enums.TypeSizeWidget;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.SettingsUtils;
import xk3y.dongle.android.utils.WidgetUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class XkeyWidgetIntentReceiver extends BroadcastReceiver {
	
	public static int  WIDGET_LAYOUT = R.layout.xkey_widget_layout_2x4;
	
	public static TypeSizeWidget WIDGET_SIZE = TypeSizeWidget.TYPE_2X4;


	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (ConfigUtils.getConfig().getIpAdress() == null){
			SettingsUtils.loadMinimalSettings(context);
		}
		
		
		if(intent.getAction().equals("xk3y.dongle.android.action.2x4.PREV_GAME")){
			updateWidgetPrevGameListener(context);
		}else if (intent.getAction().equals("xk3y.dongle.android.action.2x4.NEXT_GAME")){
			updateWidgetNextGameListener(context);
		}else if (intent.getAction().equals("xk3y.dongle.android.action.2x4.PLAY_GAME")){
			updateWidgetPlayGameListener(context);
		}else if (intent.getAction().equals("xk3y.dongle.android.action.2x4.RELOAD_GAME")){
			updateWidgetReloadGameListener(context);
		}
		
	}

	private void updateWidgetPrevGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), WIDGET_LAYOUT);
		try {
			WidgetUtils.previousGame(remoteViews,WIDGET_SIZE);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetNextGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), WIDGET_LAYOUT);
		try {
			WidgetUtils.nextGame(remoteViews,WIDGET_SIZE);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetPlayGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), WIDGET_LAYOUT);
		try {
			WidgetUtils.playGame();
			remoteViews.setTextViewText(R.id.NomView, context.getString(R.string.game_in_dvd_drive));
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetReloadGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), WIDGET_LAYOUT);
		remoteViews.setTextViewText(R.id.NomView, context.getString(R.string.load_widget));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		try {
			WidgetUtils.initData(remoteViews,WIDGET_SIZE);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.reloadButton, XkeyWidgetProvider.buildReloadButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

}