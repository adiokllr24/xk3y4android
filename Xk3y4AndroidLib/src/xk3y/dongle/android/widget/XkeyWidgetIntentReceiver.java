package xk3y.dongle.android.widget;

import xk3y.dongle.android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class XkeyWidgetIntentReceiver extends BroadcastReceiver {


	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("pl.looksok.intent.action.PREV_GAME")){
			updateWidgetPictureAndButtonListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.NEXT_GAME")){
			updateWidgetPictureAndButtonListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.PLAY_GAME")){
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		remoteViews.setImageViewResource(R.id.albumView, R.drawable.ic_launcher);

		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));

		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

}