package xk3y.dongle.android.utils;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.exception.XkeyException;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUtils {
	
	public static void initData(RemoteViews remoteViews) throws XkeyException{
		List<FullGameInfo> listGames = initListGames();
		int index = ConfigUtils.getConfig().getWidgetGameindex();
		FullGameInfo fullGameInfo =  listGames.get(index);
		updateGameView(remoteViews, fullGameInfo);
	}

	
	public static void nextGame(RemoteViews remoteViews) throws XkeyException{
		List<FullGameInfo> listGames = getListGames();
		int index = ConfigUtils.getConfig().incrementGameIndex();
		FullGameInfo fullGameInfo = listGames.get(index);
		updateGameView(remoteViews, fullGameInfo);
	}

	public static void previousGame(RemoteViews remoteViews) throws XkeyException{
		List<FullGameInfo> listGames = getListGames();
		int index = ConfigUtils.getConfig().decrementGameIndex();
		FullGameInfo fullGameInfo = listGames.get(index);
		updateGameView(remoteViews, fullGameInfo);
	}
	
	public static void playGame() throws XkeyException{
		List<FullGameInfo> listGames = getListGames();
		int index = ConfigUtils.getConfig().getWidgetGameindex();
		FullGameInfo fullGameInfo =  listGames.get(index);
		XkeyResult result =  XkeyGamesUtils.launchGame(fullGameInfo.getId());
		if (result.isShowError()){
			throw new XkeyException(result.getMessageCode());
		}
	}
	
	
	 /**
	  * METHODES PRIVEES
	  */
	
	private static List<FullGameInfo> getListGames() throws XkeyException {
		List<FullGameInfo> listGames = ConfigUtils.getConfig().getListeGames();
		if (listGames == null || listGames.isEmpty()){
			initListGames();
		}
		return listGames;
	}
	
	private static void updateGameView(RemoteViews remoteViews,
			FullGameInfo fullGameInfo) {
		if (fullGameInfo.getOriginalCover() != null){
			remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getOriginalCover());
		}else{
			remoteViews.setImageViewResource(R.id.albumView, R.drawable.default_cover);
		}
		remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
	}
	
	
	private static List<FullGameInfo> initListGames() throws XkeyException {
		List<FullGameInfo> listGames = new ArrayList<FullGameInfo>();	
		
				Xkey xkey = (Xkey)FilesUtils.loadFromSdCard(LoadingUtils.GAME_FOLDER_PATH);
				if (xkey == null){
					XkeyResult result = XkeyGamesUtils.listGames();
					if (!result.isShowError()){
						xkey = ConfigUtils.getConfig().getXkey();
					}else{
						throw new XkeyException(result.getMessageCode());
					}
				}
				for (Iso game : xkey.getListeGames()){
					FullGameInfo fullGameInfo = null;
					fullGameInfo = LoadingUtils.loadGameFromSdCard(game);
					if (fullGameInfo == null){
						fullGameInfo = new FullGameInfo();
						fullGameInfo.setId(game.getId());
						fullGameInfo.setTitle(game.getTitle());
					}
					listGames.add(fullGameInfo);
				}
				ConfigUtils.getConfig().setListeGames(listGames);
			
		return listGames;
	}
}