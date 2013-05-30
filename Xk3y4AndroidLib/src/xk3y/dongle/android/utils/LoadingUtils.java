package xk3y.dongle.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xk3y.dongle.android.dto.FullGameInfo;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.dto.XmlGameInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class LoadingUtils {

	public static final String COVER_FOLDER_PATH = 
			Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ "/Xk3y4Android/Cover/";
	public static final String GAME_FOLDER_PATH = 
			Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ "/Xk3y4Android/Games/";
	

	/**
	 * Load one cover and resize
	 */
	public static FullGameInfo loadGameInfo(Iso game) throws Exception {
		Log.e("Error: ","loadGameInfo");
		FullGameInfo gameLoad = new FullGameInfo();
		gameLoad.setId(game.getId());
		gameLoad.setTitle(game.getTitle());
		try {

			FullGameInfo gameFromSd = null;
			// Try to get info from sdcard
			if (ConfigUtils.getConfig().isCacheData()) {
				gameFromSd = loadGameFromSdCard(gameLoad);
			} else {
				LoadingUtils.removeDataCache();
			}
			
			if (gameFromSd == null) {
				// Try to get info from xml
				XmlGameInfo gameInfo = loadGameFromXml(gameLoad);
				// if no info, try to load cover with png
				if (gameInfo == null) {
					loadGameCover(gameLoad);
				}
				
				if (ConfigUtils.getConfig().isCacheData()) {
					saveGameOnSdCard(gameLoad);
				}
			} else {
				gameLoad.setId(gameFromSd.getId());
				gameLoad.setTitle(gameFromSd.getTitle());
				gameLoad.setGender(gameFromSd.getGender());
				gameLoad.setTrailer(gameFromSd.getTrailer());
				gameLoad.setSummary(gameFromSd.getSummary());
				gameLoad.setBanner(gameFromSd.getBanner());
				gameLoad.setCover(gameFromSd.getCover());
				gameLoad.setOriginalCover(gameFromSd.getOriginalCover());
			}
		} catch (Exception e) {
			throw e;
		}
		return gameLoad;
	}
	
	/**
	 * Load a game info from xml
	 * @param game a game
	 * @return info of the fame, null if not found
	 * @throws Exception 
	 */
	public static XmlGameInfo loadGameFromXml(FullGameInfo game) throws Exception {
		Log.e("Error: ","loadGameFromXml");
		XmlGameInfo gameInfo = null;
		try {
			// Try to get xml Data Game
			String fileName = game.getId() + ".xml";
			String ip = ConfigUtils.getConfig().getIpAdress();
			String fileUrl = "http://" + ip + "/covers/" + fileName;

			// Get xml from http
			String xml = HttpServices.getInstance().getResponseFromUrl(fileUrl);

			// XML node keys
			String KEY_GAMEINFO = "gameinfo"; // parent node
			String KEY_TITLE = "title";
			String KEY_SUMMARY = "summary";
			String KEY_BANNER = "banner";
			String KEY_BOXART = "boxart";
			String KEY_INFO = "info";
			String KEY_INFOITEM = "infoitem";
			String KEY_GENRE = "Genre";
			String KEY_TRAILER = "Trailer";
			
			GameInfoParserUtils parser = new GameInfoParserUtils();
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			gameInfo = new XmlGameInfo();
			
			NodeList nl = doc.getElementsByTagName(KEY_GAMEINFO);
			// looping through all item nodes <item>
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				
				gameInfo.setTitle(parser.getValue(e, KEY_TITLE));
				gameInfo.setSummary(parser.getValue(e, KEY_SUMMARY));
				gameInfo.setBoxart(parser.getValue(e, KEY_BOXART));
				if (ConfigUtils.getConfig().loadBanner()) {
					gameInfo.setBanner(parser.getValue(e, KEY_BANNER));
				}
		
				NodeList info = e.getElementsByTagName(KEY_INFO);
				if (nl != null && info != null) {
					for (int i2 = 0; i2 < nl.getLength(); i2++) {
						Element e2 = (Element) info.item(i2);
						
						if (e2 != null) {
							String genre = parser.getValueByAttribute(e2, KEY_INFOITEM, KEY_GENRE);
							String trailer = parser.getValueByAttribute(e2, KEY_INFOITEM, KEY_TRAILER);
							gameInfo.setGenre(genre);
							gameInfo.setTrailer(trailer);
						}
					}
				}
			}

			if (gameInfo.getTitle() != null && !gameInfo.getTitle().equals("")
					&& !gameInfo.getTitle().equals("No Title")) {
				game.setTitle(gameInfo.getTitle());
			}
			
			if (gameInfo.getGenre() != null && !gameInfo.getGenre().equals("")) {
				game.setGender(gameInfo.getGenre());
			}
			
			if (gameInfo.getTrailer() != null && !gameInfo.getTrailer().equals("")) {
				game.setTrailer(gameInfo.getTrailer());
			}
			
			if (gameInfo.getSummary() != null && !gameInfo.getSummary().equals("")) {
				game.setSummary(gameInfo.getSummary());
			}
			
			game.setBanner(null);
			if (ConfigUtils.getConfig().loadBanner()) {
				Bitmap banner = ImageUtils.encodeImageFromBase64(gameInfo.getBanner());
				// If banner == null then no xml, no banner
				if (banner == null) {
					banner = ConfigUtils.getConfig().getDefaultBanner();
				}
				game.setBanner(ImageUtils.resizeBanner(banner));
			}
			
			// Convert base 64 text to Bitmap
			Bitmap cover = ImageUtils.encodeImageFromBase64(gameInfo.getBoxart());
			// If conver == null then no xml, no cover
			if (cover == null) {
				gameInfo = null;
			} else {
				addCoverToGame(game, ImageUtils.resizeCover(cover));
			}
			


		} catch (Exception e) {
			gameInfo = null;
			//throw e;
		}
		return gameInfo;
	}
	
	/**
	 * Load one cover and resize from hhtp
	 */
	public static void loadGameCover(FullGameInfo game) throws Exception  {
		Log.e("Error: ","loadGameCover");
		try {
			String imgName = game.getId() + ".jpg";
			String ip = ConfigUtils.getConfig().getIpAdress();
			String coverUrl = "http://" + ip + "/covers/" + imgName;

			// Get image from http
			Bitmap cover = HttpServices.getInstance().loadImage(coverUrl);
			
			// Set the Bitmap to the game
			addCoverToGame(game, ImageUtils.resizeCover(cover));
			
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Load one cover and resize from hhtp
	 */
	public static void addCoverToGame(FullGameInfo game, Bitmap cover) throws Exception {
		Log.e("Error: ",game.getTitle());
		Log.e("Error: ",String.valueOf(cover == null));
		try {
			// The bitmpa With title at top
			
			Bitmap coverWithTitle = ImageUtils.addTextAtTopOfBitmap(
					cover, game.getTitle());
			
			//resizeCover.recycle();
			
			// Set the original cover
			game.setOriginalCover(cover);
			
			if (ConfigUtils.getConfig().getTheme() == ConfigUtils.THEME_COVER_FLOW) {
				Log.e("Error: ","coverWithReflextion");
				// Set the cover with text and reflexion
				Bitmap coverWithReflextion = ImageUtils.addReflextionToBitmap(coverWithTitle);
				game.setCover(coverWithReflextion);
				coverWithTitle.recycle();
			} else if (ConfigUtils.getConfig().getTheme() == ConfigUtils.THEME_COVER_LIST) {
				game.setCover(ImageUtils.resizeCoverForList(game.getOriginalCover()));
			} else if (ConfigUtils.getConfig().getTheme() == ConfigUtils.THEME_COVER_FLOW_LIGHT) {
				// Set the cover with text without reflexion
				game.setCover(coverWithTitle);
			}

			//System.gc();
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Save a game on sd card
	 * @param game a game
	 */
	public static void saveGameOnSdCard(FullGameInfo game) {
		String savePath = GAME_FOLDER_PATH + game.getId() + ".data";
		FilesUtils.saveOnSdCard(game, savePath);
	}

	/**
	 * Save a game on sd card
	 * @param game a game
	 */
	public static void saveXkeyOnSdCard(Xkey xkey) {
		String savePath = GAME_FOLDER_PATH + "xkey.data";
		FilesUtils.saveOnSdCard(xkey, savePath);
	}
	
	
	
	/**
	 * Load a game from sd card
	 * @param game a game
	 */
	public static FullGameInfo loadGameFromSdCard(Iso game) {
		String savePath = GAME_FOLDER_PATH + game.getId() + ".data";
		FullGameInfo loadGame = (FullGameInfo) FilesUtils.loadFromSdCard(savePath);
		return loadGame;
	}
	
	/**
	 * Load xkey from sd card
	 */
	public static Xkey loadXkeyFromSdCard() {
		String savePath = GAME_FOLDER_PATH + "xkey.data";
		Xkey xkey = (Xkey) FilesUtils.loadFromSdCard(savePath);
		return xkey;
	}
	
	
	
	/**
	 * Remove data cache folder
	 * @throws Exception exception
	 */
	public static void removeDataCache() throws Exception {
		try {
			removeDataDir(GAME_FOLDER_PATH);
			removeDataDir(COVER_FOLDER_PATH);
			if (ConfigUtils.getConfig().getListeGames() != null) {
				for (FullGameInfo game : ConfigUtils.getConfig().getListeGames()) {
					LoadingUtils.clearBitmap(game.getBanner());
					LoadingUtils.clearBitmap(game.getCover());
					LoadingUtils.clearBitmap(game.getOriginalCover());
				}
			}
	    	System.gc();
		} catch (Exception e) {
			throw e;
		}
	} 
	
	/**
	 * Remove data cache folder
	 * @throws Exception exception
	 */
	public static void removeDataDir(String path) throws Exception {
		try {
			File dir = new File(path);
			if (dir.exists() && dir.isDirectory()) {
				String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir, children[i]).delete();
		        }
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception e) {
			throw e;
		}
	} 
	
	// Clear bitmap
	public static void clearBitmap(Bitmap bm) {
		if (bm != null) {
			bm.recycle();
		}
	}
	
	
}
