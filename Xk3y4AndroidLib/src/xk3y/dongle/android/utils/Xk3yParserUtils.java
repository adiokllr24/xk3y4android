package xk3y.dongle.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Item;
import xk3y.dongle.android.dto.Xkey;

/**
 * Parse an xk3y xml flow (data.xml) and convert it to Xkey Object
 * @author maloups
 *
 */
public class Xk3yParserUtils {

	/**
	 * Parse a data.xml
	 * 
	 * @param xml
	 *            a data.xml
	 * @return a xkey object reprerenting the xml flow
	 */
	public static Xkey getXkey(Reader reader) {
		// The Xkey object
		Xkey xkey = new Xkey();
		// The list of games In all partitions
		List<Iso> listeGames = new ArrayList<Iso>();
		// The list of the xkey informations
		List<Item> listeItems = new ArrayList<Item>();
		
		BufferedReader in = null;
		
		try {
			// Parse XML file
			in = new BufferedReader(reader);
			String line = "";
			while ((line = in.readLine()) != null) {
				String tmpElement = "";
				
				// ISO ELEMENT
				if (line.startsWith("<ISO>")) {
					String endElement = "</ISO>";
					while(!line.startsWith(endElement)) {
						tmpElement += line;
						line = in.readLine();
					}
					tmpElement += endElement;
					listeGames.add(getIsoObject(tmpElement));
				} 
				
				// GUISTATE ELEMENT
				else if (line.startsWith("<GUISTATE>")) {
					String str = line.replace("<GUISTATE>", "").replace("</GUISTATE>", "");
					xkey.setGuistate(str);
				}
				
				// EMERGENCY ELEMENT
				else if (line.startsWith("<EMERGENCY>")) {
					String str = line.replace("<EMERGENCY>", "").replace("</EMERGENCY>", "");
					xkey.setEmergency(str);
				}

				// TRAYSTATE ELEMENT
				else if (line.startsWith("<TRAYSTATE>")) {
					String str = line.replace("<TRAYSTATE>", "").replace("</TRAYSTATE>", "");
					xkey.setTrayState(str);
				}
				
				// ITEM ELEMENT
				if (line.startsWith("<ITEM")) {
					listeItems.add(getItemObject(line));
				} 
				
			}
			in.close();

			xkey.setListeGames(listeGames);
			xkey.setListeItems(listeItems);
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
		
		return xkey;
	}
	
	/**
	 * Get an iso object from xml data
	 * 
	 * @param xml
	 *            xmldata
	 * @return an iso object
	 */
	public static Iso getIsoObject(String xml) {
		Iso obj = null;
		try {
			Serializer serializer = new Persister();
			Reader reader = new StringReader(xml);
			obj = serializer.read(Iso.class, reader, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Get an iso object from xml data
	 * 
	 * @param xml
	 *            xmldata
	 * @return an iso object
	 */
	public static Item getItemObject(String xml) {
		Item obj = new Item();
		try {
			
			String line = xml.replace("</ITEM>", "");
			int pos = line.indexOf("\">");
			String value = line.substring(pos + 2);
			obj.setValue(value);
			
			String name = line.replace("\">" + value, "").replace("<ITEM NAME=\"", "");
			obj.setName(name);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
}
