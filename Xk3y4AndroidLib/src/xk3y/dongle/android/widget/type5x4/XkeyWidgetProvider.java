package xk3y.dongle.android.widget.type5x4;


import xk3y.dongle.android.R;
import xk3y.dongle.android.utils.WidgetUtils;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class XkeyWidgetProvider extends AppWidgetProvider {
	
	public static String ACTION_PLAY = "xk3y.dongle.android.action.5x4.PLAY_GAME";
	public static String ACTION_PREV = "xk3y.dongle.android.action.5x4.PREV_GAME";
	public static String ACTION_NEXT = "xk3y.dongle.android.action.5x4.NEXT_GAME";
	public static String ACTION_RELOAD= "xk3y.dongle.android.action.5x4.RELOAD_GAME";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),XkeyWidgetIntentReceiver.WIDGET_LAYOUT);
		remoteViews.setOnClickPendingIntent(R.id.prevButton,WidgetUtils.buildButtonPendingIntent(context,ACTION_PREV));
		remoteViews.setOnClickPendingIntent(R.id.nextButton, WidgetUtils.buildButtonPendingIntent(context,ACTION_NEXT));
		remoteViews.setOnClickPendingIntent(R.id.playButton, WidgetUtils.buildButtonPendingIntent(context,ACTION_PLAY));
		remoteViews.setOnClickPendingIntent(R.id.reloadButton, WidgetUtils.buildButtonPendingIntent(context,ACTION_RELOAD));
		WidgetUtils.initWidget(context, remoteViews, XkeyWidgetIntentReceiver.WIDGET_SIZE);
		XkeyWidgetIntentReceiver.pushWidgetUpdate(context, remoteViews);
		
	}

	
}
