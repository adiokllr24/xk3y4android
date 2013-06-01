package xk3y.dongle.android.widget;


import xk3y.dongle.android.R;
import xk3y.dongle.android.enums.TypeSizeWidget;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.WidgetUtils;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AbstractXkeyWidgetProvider extends AppWidgetProvider {
	
	
	private int widgetLayout;
	
	private TypeSizeWidget typeSizeWidget;
	
	
	public AbstractXkeyWidgetProvider(int pWidgetLayout, TypeSizeWidget pTypeSizeWidget) {
		super();
		widgetLayout = pWidgetLayout;
		typeSizeWidget = pTypeSizeWidget;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),widgetLayout);
		remoteViews.setOnClickPendingIntent(R.id.prevButton, buildPrevButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.nextButton, buildNextButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.playButton, buildPlayButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.reloadButton, buildReloadButtonPendingIntent(context));

		try {
			if (ConfigUtils.getConfig().getListeAllGames() == null || ConfigUtils.getConfig().getListeAllGames().isEmpty()){
				WidgetUtils.initData(remoteViews, typeSizeWidget);
			}else{
				WidgetUtils.loadData(remoteViews, typeSizeWidget);
			}
		} catch (XkeyException e) {
			if (e.getCode() != 0 && e.getCode() != R.string.no_ip){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		pushWidgetUpdate(context, remoteViews);
	}

	public PendingIntent buildPrevButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("xk3y.dongle.android.action."+typeSizeWidget.name()+".PREV_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public PendingIntent buildNextButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("xk3y.dongle.android.action."+typeSizeWidget.name()+".NEXT_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public PendingIntent buildPlayButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("xk3y.dongle.android.action."+typeSizeWidget.name()+".PLAY_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public PendingIntent buildReloadButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction("xk3y.dongle.android.action."+typeSizeWidget.name()+".RELOAD_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, AbstractXkeyWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
}
