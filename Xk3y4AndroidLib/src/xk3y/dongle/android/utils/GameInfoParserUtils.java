package xk3y.dongle.android.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class GameInfoParserUtils {

	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			byte[] bytes = xml.getBytes("UTF-8");
			InputStream input = new ByteArrayInputStream(bytes);
			is.setByteStream(input);
			// is.setCharacterStream(new
			// StringReader(xml.getBytes("UTF-8").toString()));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}
	
	public String getValue(Element item, String str) {
	    NodeList n = item.getElementsByTagName(str);
	    return this.getElementValue(n.item(0));
	}
	 
	public String getValueByAttribute(Element item, String str, String attr) {
	    NodeList n = item.getElementsByTagName(str);
	    Element e;
	    for (int i = 0; i < n.getLength(); i++) {
	      e = (Element) n.item(i);
	      if (e.getAttribute("name").equals(attr)) {
	    	  String res = this.getElementValue(e);
	    	  return res;
	      }
	    }

	    return "";
	}
	
	public final String getElementValue( Node elem ) {
	         Node child;
	         if( elem != null){
	             if (elem.hasChildNodes()){
	                 for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                     if( child.getNodeType() == Node.TEXT_NODE  ){
	                         return child.getNodeValue();
	                     }
	                 }
	             }
	         }
	         return "";
	  }
}
