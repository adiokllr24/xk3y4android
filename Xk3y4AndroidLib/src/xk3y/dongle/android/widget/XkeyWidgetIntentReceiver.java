package xk3y.dongle.android.widget;

import xk3y.dongle.android.R;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.WidgetUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class XkeyWidgetIntentReceiver extends BroadcastReceiver {


	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("pl.looksok.intent.action.PREV_GAME")){
			updateWidgetPrevGameListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.NEXT_GAME")){
			updateWidgetNextGameListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.PLAY_GAME")){
			updateWidgetPlayGameListener(context);
		}
	}

	private void updateWidgetPrevGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.previousGame(remoteViews);
		} catch (XkeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetNextGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.nextGame(remoteViews);
		} catch (XkeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetPlayGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.playGame();
		} catch (XkeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

}