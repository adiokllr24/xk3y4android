package xk3y.dongle.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FilesUtils {
	
	/**
	 * Save an object on sd card
	 * @param obj an object to serialize
	 * @param path the path where serialize object
	 */
	public static void saveOnSdCard(Object obj, String path) {
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
	
			os = new ObjectOutputStream(fos);
			os.writeObject(obj);
			os.close();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Load an object from sd card
	 * @param game a game
	 */
	public static Object loadFromSdCard(String path) {
		
		
		Object obj = null;
		FileInputStream  fis = null;
		ObjectInputStream is = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fis = new FileInputStream (file);
		
				is = new ObjectInputStream(fis);
				obj = is.readObject();
			}
		} catch (Exception e) {
			obj = null;
			e.getMessage();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return obj;
	}
	

}
