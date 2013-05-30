package xk3y.dongle.android.utils;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.acra.ErrorReporter;

import com.google.gdata.util.common.base.StringUtil;

import android.content.Intent;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.ihm.theme.CoverFlowGamesActivity;
import xk3y.dongle.android.ihm.theme.ListGamesActivity;

public class XkeyGamesUtils {
	
	/**
	 * Call xKey to launch game from id
	 * @param idGame id of xkey game
	 * @return XKeyResult result from call
	 * @throws XkeyException 
	 */
	public static XkeyResult launchGame(final String idGame) throws XkeyException {
		try {

			XkeyResult result = new XkeyResult();
			
			String url = "http://" + ConfigUtils.getConfig().getIpAdress()
					+ "/launchgame.sh?" + idGame;

			String response = HttpServices.getInstance().getResponseFromUrl(url);
			
			
			
			if (response == null
					|| (response != null && response.equals(""))) {
				result.setShowError(true);
				result.setMessageCode(R.string.not_connected);
			} else { 
				// Generate xkey object from xml flow
				Reader reader = new StringReader(response);
				Xkey xkey = Xk3yParserUtils.getXkey(reader);
				
				/*
				if (xkey.getTrayState() == null) {
					String ipAdress = ConfigUtils.getConfig().getIpAdress();
					if (ipAdress == null || (ipAdress != null && ipAdress.length() == 0)) {
						result.setShowError(true);
						result.setMessageCode(R.string.no_ip);
						return result;
					} else {
						ErrorReporter.getInstance().putCustomData("\n\nGAME URL", "\n" + url);
						ErrorReporter.getInstance().putCustomData("\n\nXKEY RESPONSE", "\n" + response);
					}

				}*/
				
				// 3K3Y HAVE NO TRAYSTATE !!!!
				if (xkey != null && xkey.getTrayState() != null){
					// DVD drive is close
					if (xkey.getTrayState().equals("1")) {
						result.setShowError(true);
						// Game currently running
						if (xkey.getGuistate() != null && xkey.getGuistate().equals("2")) {
							result.setMessageCode(R.string.game_in_dvd_drive);
							
						// Game not running, try to eject dvd drive message	
						} else {
							result.setMessageCode(R.string.open_dvd_drive);
						}
					}
				} else {
					if (xkey != null) {
						
						// Game currently running
						if (xkey.getGuistate() != null && xkey.getGuistate().equals("2")) {
							result.setShowError(true);
							result.setMessageCode(R.string.game_in_dvd_drive);
						}
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new XkeyException(e);
		}
		
	}
	
	/**
	 * Call Xkey to list game
	 * @return XKeyResult result from call
	 * @throws XkeyException 
	 */
	public static XkeyResult listGames() throws XkeyException {
		
		final XkeyResult result = new XkeyResult();
		final StringBuffer debugMessage = new StringBuffer();
		try {
					
			result.getDebugMessage().append("List Games...");
			String ipAdress = ConfigUtils.getConfig().getIpAdress();
			if (ipAdress == null || (ipAdress != null && ipAdress.length() == 0)) {
				result.setShowError(true);
				result.setMessageCode(R.string.no_ip);
				return result;
			}
			String urlToListGames = "http://" + ipAdress
					+ "/data.xml";
			String xkeyInfo = HttpServices.getInstance().getResponseFromUrl(
					urlToListGames);
			
			ErrorReporter.getInstance().putCustomData("\n\nTHEME", String.valueOf(ConfigUtils.getConfig().getTheme()));
			ErrorReporter.getInstance().putCustomData("\n\nDATA_XML", "\n" + xkeyInfo);
			
			result.getDebugMessage().append("\nList Games OK");
			if (xkeyInfo == null
					|| (xkeyInfo != null && xkeyInfo.equals(""))) {
				
				result.getDebugMessage().append("\nNo Xkey Connexion");
				Xkey xkey = LoadingUtils.loadXkeyFromSdCard();
				if (xkey == null) {
					result.setShowError(true);
					result.setMessageCode(R.string.not_connected);
				} else {
					ConfigUtils.getConfig().setXkey(xkey);
				}
			} else {
				// Generate xkey object from xml flow
				result.getDebugMessage().append("\nConvert XML to Object...");
				Reader reader = new StringReader(xkeyInfo);
				Xkey xkey = Xk3yParserUtils.getXkey(reader);
				ConfigUtils.getConfig().setXkey(xkey);
				result.getDebugMessage().append("\nConvert XML to Object OK");
			}	
			
			if (!result.isShowError()) {
				List<Iso> listIsos = ConfigUtils.getConfig().getXkey().getListeGames();
				if (listIsos == null || (listIsos != null && listIsos.isEmpty())) {
					result.getDebugMessage().append("\nNo Game detected");
					result.setShowError(true);
					result.setMessageCode(R.string.no_games);
									
				} else {
					Collections.sort(listIsos, Iso.TitleComparator);
					ConfigUtils.getConfig().setListeAllGames(listIsos);
					ConfigUtils.getConfig().setCurrentPage(1);
							
									
					// Open list games window
					result.getDebugMessage().append("\nLaunch CoverFlow...");
								
				}
			}
			
			
		} catch (OutOfMemoryError E) {
			result.setShowError(true);
			result.setMessageCode(R.string.out_of_memory_error);
			//Set autoload banner to false
			ConfigUtils.getConfig().setAutoLoadBanners(false);
		} catch (Exception e) {
			throw new XkeyException(debugMessage.toString(), e);
			
		}
		
		return result;
		
	}

}
