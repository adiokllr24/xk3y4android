package xk3y.dongle.android.utils;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.exception.XkeyException;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUtils {
	
	public static void initData(RemoteViews remoteViews) throws XkeyException{
		try {
			List<FullGameInfo> listGames = getListGames();
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			Log.e("Error: ",e.getMessage(), e);
			throw new XkeyException(e.getMessage());
		}
		
	}
	
	public static void nextGame(RemoteViews remoteViews) throws XkeyException{
		try {
			List<FullGameInfo> listGames = getListGames();
			int index = ConfigUtils.getConfig().incrementGameIndex();
			Log.e("Index: ",String.valueOf(index));
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			Log.e("Error: ",e.getMessage(), e);
			throw new XkeyException(e.getMessage());
		}
		
	}


	
	public static void previousGame(RemoteViews remoteViews) throws XkeyException{
		try {
			List<FullGameInfo> listGames = getListGames();
			int index = ConfigUtils.getConfig().decrementGameIndex();
			FullGameInfo fullGameInfo =  listGames.get(index);
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getCover());
			remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
		} catch (Exception e) {
			Log.e("Error: ",e.getMessage(), e);
			throw new XkeyException(e.getMessage());
		}
		
	}
	
	public static void playGame() throws XkeyException{
		try {
			List<FullGameInfo> listGames = getListGames();
			int index = ConfigUtils.getConfig().getWidgetGameindex();
			Log.e("Index: ",String.valueOf(index));
			FullGameInfo fullGameInfo =  listGames.get(index);
			XkeyGamesUtils.launchGame(fullGameInfo.getId());
		} catch (Exception e) {
			Log.e("Error: ",e.getMessage(), e);
			throw new XkeyException(e.getMessage());
		}
		
	}
	
	private static List<FullGameInfo> getListGames() throws XkeyException {
		List<FullGameInfo> listGames = ConfigUtils.getConfig().getListeGames();
		if (listGames == null){
			try{
				XkeyResult result =  XkeyGamesUtils.listGames();
				if (result.isShowError()){
					//TODO gerer erreur
				}
				List<FullGameInfo> listGamesFull = new ArrayList<FullGameInfo>();
				for (Iso game : ConfigUtils.getConfig().getListeIsoToLoad()){
					listGamesFull.add(LoadingUtils.loadGameInfo(game));
				}
				ConfigUtils.getConfig().setListeGames(listGames);
			}catch(Exception e){
				throw new XkeyException(e.getMessage());
			}
		}
		return listGames;
	}
}
