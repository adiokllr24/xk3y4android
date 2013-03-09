package xk3y.dongle.android.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



/**
 * Load settings app
 *
 * @author maloups
 *
 */
public final class HttpServices implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
	
	private static HttpServices instance;


	private HttpServices() {

	}
	
	public static HttpServices getInstance() {
		if (instance == null) {
			instance = new HttpServices();
		}
		return instance;
	}

	/**
	 * Return the hhtp result
	 * 
	 * @param url an url
	 * @return the http result in string
	 */
	public String getResponseFromUrl(String url) {
		String res = "";
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			res = sb.toString();
			//System.out.println(res);
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	/**
	 * Load a file from an url
	 * 
	 * @param url
	 *            an url
	 * @return a file
	 */
	public File loadFile(String coverUrl, File file) {

		//File file = new File(filePath);
		try {
			// set the download URL, a url that points to a file on the internet
			// this is the file to be downloaded
			URL url = new URL(coverUrl);

			// create the new connection
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// set up some things on the connection
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// and connect!
			urlConnection.connect();

			// this will be used to write the downloaded data into the file we
			// created
			FileOutputStream fileOutput = new FileOutputStream(file);

			// this will be used in reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();
			
			 //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	        }
	        //close the output stream when done
	        fileOutput.close();
	        inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return file;
	}
	
	/**
	 * Load a file from an url
	 * 
	 * @param url
	 *            an url
	 * @return a file
	 */
	public Bitmap loadImage(String coverUrl) {
		Bitmap img = null;
		//File file = new File(filePath);
		try {
			// set the download URL, a url that points to a file on the internet
			// this is the file to be downloaded
			URL url = new URL(coverUrl);

			// create the new connection
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// set up some things on the connection
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// and connect!
			urlConnection.connect();

			// this will be used in reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();
			img = BitmapFactory.decodeStream(inputStream);
	        inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return img;
	}

}
