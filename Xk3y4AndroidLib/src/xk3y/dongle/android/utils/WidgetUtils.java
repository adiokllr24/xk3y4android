package xk3y.dongle.android.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.enums.TypeSizeWidget;
import xk3y.dongle.android.exception.XkeyException;
import android.widget.RemoteViews;

public class WidgetUtils {
	
	public static void initData(RemoteViews remoteViews, TypeSizeWidget typeSizeWidget) throws XkeyException{
		List<Iso> listGames = initListGames();
		int index = ConfigUtils.getConfig().getWidgetGameindex();
		FullGameInfo fullGameInfo =  getFullGame(listGames.get(index));
		updateGameView(remoteViews, fullGameInfo,typeSizeWidget);
	}
	
	public static void loadData(RemoteViews remoteViews, TypeSizeWidget typeSizeWidget) throws XkeyException{
		List<Iso> listGames = getListGames();
		int index = ConfigUtils.getConfig().getWidgetGameindex();
		FullGameInfo fullGameInfo =  getFullGame(listGames.get(index));
		updateGameView(remoteViews, fullGameInfo,typeSizeWidget);
	}

	
	public static void nextGame(RemoteViews remoteViews, TypeSizeWidget typeSizeWidget) throws XkeyException{
		List<Iso> listGames = getListGames();
		int index = ConfigUtils.getConfig().incrementGameIndex();
		FullGameInfo fullGameInfo = getFullGame(listGames.get(index));
		updateGameView(remoteViews, fullGameInfo,typeSizeWidget);
	}

	public static void previousGame(RemoteViews remoteViews, TypeSizeWidget typeSizeWidget) throws XkeyException{
		List<Iso> listGames = getListGames();
		int index = ConfigUtils.getConfig().decrementGameIndex();
		FullGameInfo fullGameInfo = getFullGame(listGames.get(index));
		updateGameView(remoteViews, fullGameInfo,typeSizeWidget);
	}
	
	public static void playGame() throws XkeyException{
		List<Iso> listGames = getListGames();
		int index = ConfigUtils.getConfig().getWidgetGameindex();
		FullGameInfo fullGameInfo =  getFullGame(listGames.get(index));
		XkeyResult result =  XkeyGamesUtils.launchGame(fullGameInfo.getId());
		if (result.isShowError()){
			throw new XkeyException(result.getMessageCode());
		}
	}
	
	
	 /**
	  * METHODES PRIVEES
	  */
	
	private static List<Iso> getListGames() throws XkeyException {
		List<Iso> listGames = ConfigUtils.getConfig().getListeAllGames();
		if (listGames == null || listGames.isEmpty()){
			initListGames();
		}
		return listGames;
	}
	
	private static void updateGameView(RemoteViews remoteViews,
			FullGameInfo fullGameInfo, TypeSizeWidget typeSizeWidget) {
		
		switch (typeSizeWidget) {
		case TYPE_1X4:
			if (fullGameInfo.getOriginalCover() != null){
				remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getOriginalCover());
			}else{
				remoteViews.setImageViewResource(R.id.albumView, R.drawable.default_cover);
			}
			break;
		case TYPE_2X4:
			if (fullGameInfo.getBanner() != null){
				remoteViews.setImageViewBitmap(R.id.albumView, fullGameInfo.getBanner());
			}else{
				remoteViews.setImageViewResource(R.id.albumView, R.drawable.default_banner);
			}
			break;
		case TYPE_4X4:
				remoteViews.setImageViewBitmap(R.id.playButton, fullGameInfo.getCover());
			break;
		default:
			break;
		}
		
		
		remoteViews.setTextViewText(R.id.NomView, fullGameInfo.getTitle());
	}
	
	
	private static List<Iso> initListGames() throws XkeyException {
		List<Iso> listGames = new ArrayList<Iso>();	
		
				Xkey xkey = (Xkey)FilesUtils.loadFromSdCard(LoadingUtils.GAME_FOLDER_PATH);
				if (xkey == null){
					XkeyResult result = XkeyGamesUtils.listGames();
					if (!result.isShowError()){
						xkey = ConfigUtils.getConfig().getXkey();
					}else{
						throw new XkeyException(result.getMessageCode());
					}
				}
				listGames.addAll(xkey.getListeGames());
				Collections.sort(listGames, Iso.TitleComparator);
				ConfigUtils.getConfig().setListeAllGames(listGames);
			
		return listGames;
	}


	private static FullGameInfo getFullGame(Iso game) throws XkeyException {
		FullGameInfo fullGameInfo = null;
		fullGameInfo = LoadingUtils.loadGameFromSdCard(game);
		if (fullGameInfo == null){
			fullGameInfo = new FullGameInfo();
			fullGameInfo.setId(game.getId());
			fullGameInfo.setTitle(game.getTitle());
			try{
			LoadingUtils.addCoverToGame(fullGameInfo, ImageUtils.resizeCover(ConfigUtils.getConfig().getDefaultCover()));
			}catch(Exception e){
				throw new XkeyException(e);
			}
		}
		return fullGameInfo;
	}
}
