package xk3y.dongle.android.widget.type4x4;

import xk3y.dongle.android.R;
import xk3y.dongle.android.enums.TypeSizeWidget;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.SettingsUtils;
import xk3y.dongle.android.utils.WidgetUtils;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

public class XkeyWidgetIntentReceiver extends BroadcastReceiver {


	public static int  WIDGET_LAYOUT = R.layout.xkey_widget_layout_4x4;
	
	public static TypeSizeWidget WIDGET_SIZE = TypeSizeWidget.TYPE_4X4;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (ConfigUtils.getConfig().getIpAdress() == null){
			SettingsUtils.loadMinimalSettings(context);
			ConfigUtils.getConfig().setDefaultBanner(
					BitmapFactory.decodeResource(context.getResources(), R.drawable.default_banner));
		}
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), WIDGET_LAYOUT);
		
		if(intent.getAction().equals(XkeyWidgetProvider.ACTION_PREV)){
			WidgetUtils.updateWidgetPrevGameListener(context,remoteViews,WIDGET_SIZE);
		}else if (intent.getAction().equals(XkeyWidgetProvider.ACTION_NEXT)){
			WidgetUtils.updateWidgetNextGameListener(context,remoteViews,WIDGET_SIZE);
		}else if (intent.getAction().equals(XkeyWidgetProvider.ACTION_PLAY)){
			WidgetUtils.updateWidgetPlayGameListener(context,remoteViews,WIDGET_SIZE);
		}else if (intent.getAction().equals(XkeyWidgetProvider.ACTION_RELOAD)){
			remoteViews.setTextViewText(R.id.NomView, context.getString(R.string.load_widget));
			pushWidgetUpdate(context.getApplicationContext(), remoteViews);
			WidgetUtils.updateWidgetReloadGameListener(context,remoteViews,WIDGET_SIZE);
		}
		pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, XkeyWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}

	

}