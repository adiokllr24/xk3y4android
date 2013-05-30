package xk3y.dongle.android.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;

public class ImageUtils {

	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCover(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		if (cover == null) {
			cover = ConfigUtils.getConfig().getDefaultCover();
		}
		try {
			// Resize the image
			int height = ConfigUtils.getConfig().getCoverHeight();
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCoverForGameDetails(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		try {
			double heigthDiviser = 1.2;
			if (ConfigUtils.getConfig().isTablet()) {
				heigthDiviser = 1.5;
			}
			// Resize the image
			int height = (int) (ConfigUtils.getConfig().getCoverHeight() * heigthDiviser);
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCoverForList(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		if (cover == null) {
			cover = ConfigUtils.getConfig().getDefaultCover();
		}
		try {
			// Resize the image
			int height = (int) (ConfigUtils.getConfig().getCoverHeight() * 0.4);
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeBanner(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		if (cover == null) {
			cover = ConfigUtils.getConfig().getDefaultBanner();
		}
		try {
			if (resizeCover != null) {
				// Resize the image
				int width  = ConfigUtils.getConfig().getScreenWidth();
				float tmp = ((float)((float)width / (float)cover.getWidth())) * cover.getHeight();
				//int newWidth = (int) (tmp * 0.9);
				int newHeight = (int) tmp;
	
				resizeCover = Bitmap.createScaledBitmap(cover, width, newHeight, true);
			}
		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Add text at top of the image
	 * @param img the image
	 * @param text the text add
	 * @return image with text
	 * @throws Exception 
	 */
	public static Bitmap addTextAtTopOfBitmap(Bitmap img, String text) throws Exception{
		Bitmap bmOverlay = img;
		try {
			if (ConfigUtils.getConfig().addCoverTitle()) {
				// create a mutable bitmap with the same size as the background image
				bmOverlay = Bitmap.createBitmap(img.getWidth(), img.getHeight() + 30, 
				    Bitmap.Config.ARGB_4444);
				// create a canvas on which to draw
				Canvas canvas = new Canvas(bmOverlay);
		
				Paint paint = new Paint();
				paint.setColor(Color.WHITE);
				paint.setTextSize(20);
		
				paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		
				// draw the text and the point
				canvas.drawText(text, 0, 20, paint);
				// overlay background
				canvas.drawBitmap(img, 0, 30, paint);
				
				//canvas.drawPoint(30.0f, 50.0f, paint);


			}
		} catch (Exception e) {
			throw e;
		}
		
		return bmOverlay;
	}

	/**
	 * Add text at top of the image
	 * 
	 * @param img
	 *            the image
	 * @param text
	 *            the text add
	 * @return image with text
	 * @throws Exception
	 */
	public static Bitmap addReflextionToBitmap(Bitmap img) throws Exception {
		Bitmap bitmapWithReflection = null;
		try {
			// The gap we want between the reflection and the original image
			final int reflectionGap = 4;
			// Get you bit map from drawable folder
			Bitmap originalImage = img;

			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			// Create a Bitmap with the flip matix applied to it.
			// We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			// Create a new bitmap with same width but taller to fit reflection
			bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 3), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(originalImage, 0, 0, null);
			// Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			// Create a shader that is a linear gradient that covers the
			// reflection
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);
			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			reflectionImage.recycle();
		} catch (Exception e) {
			throw e;
		}

		return bitmapWithReflection;
	}
	
	/**
	 * Permet de d�coder une image encoder en Base64
	 * @param la chaine repr�senatant l'image
	 * @return l'image
	 * @throws Exception exception
	 */
	public static Bitmap encodeImageFromBase64(String encodedImage) throws Exception {
		Bitmap decodeImage = null;
		try {
			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			decodeImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		} catch (Exception e) {
			decodeImage = null;
		}
		return decodeImage;
	}
	
	/**
	 * Permet d'encoder une image en Base 64
	 * @param path l'emplacement de l'image
	 * @return l'image sous forme de String
	 * @throws Exception exception
	 */
	public static String convertImageToBase64(Bitmap bm) throws Exception {
		String encodedImage = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray(); 
			
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			throw e;
		}
		return encodedImage;
	}
	
	
	// Clear bitmap
	public static void clearBitmap(Bitmap bm) {
		bm.recycle();
		System.gc();

	}
	
	
}
