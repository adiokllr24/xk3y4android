package xk3y.dongle.android.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.RemoteViews;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;

public class WidgetUtils {
	
	public static void initData(RemoteViews remoteViews){
		try {
			XkeyGamesUtils.listGames();
			List<FullGameInfo> listGames = new ArrayList<FullGameInfo>();
			for (Iso game : ConfigUtils.getConfig().getListeIsoToLoad()){
				listGames.add(LoadingUtils.loadGameInfo(game));
			}
			ConfigUtils.getConfig().setListeGames(listGames);
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void nextGame(RemoteViews remoteViews){
		try {
			List<FullGameInfo> listGames = ConfigUtils.getConfig().getListeGames();
			ConfigUtils.getConfig().incrementGameIndex();
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			Log.e("Index: ",String.valueOf(index));
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void previousGame(RemoteViews remoteViews){
		try {
			List<FullGameInfo> listGames = ConfigUtils.getConfig().getListeGames();
			ConfigUtils.getConfig().decrementGameIndex();
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			Log.e("Index: ",String.valueOf(index));
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void playGame(RemoteViews remoteViews){
		try {
			List<FullGameInfo> listGames = ConfigUtils.getConfig().getListeGames();
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			Log.e("Index: ",String.valueOf(index));
			FullGameInfo fullGameInfo =  listGames.get(index);
			XkeyGamesUtils.launchGame(fullGameInfo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
