package xk3y.dongle.android.dto;

import java.io.Serializable;
import java.util.Comparator;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ISO")
public class Iso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Element(name = "TITLE", required=false)
	private String title;
	@Element(name = "ID", required=false)
	private String id;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sort iso by title
	 */
	public static Comparator<Iso> TitleComparator = new Comparator<Iso>() {

		public int compare(Iso iso1, Iso iso2) {

			String title1 = iso1.getTitle().toUpperCase();
			String title2 = iso2.getTitle().toUpperCase();

			// ascending order
			return title1.compareTo(title2);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};
	
	
}
